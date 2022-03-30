#!/usr/bin/env bash
set -euo pipefail

export MINIKUBE_IN_STYLE=false
MINIKUBE_PROFILE=observability-demo
echo ">>>> Starting minikube with profile $MINIKUBE_PROFILE..."

minikube start --profile $MINIKUBE_PROFILE
minikube profile $MINIKUBE_PROFILE

echo ">>>> Building & pushing Spring Boot Demo App..."
# sleep a bit as minikube's network might not be present immediately,
# failing the build with curl
sleep 15

eval "$(minikube -p $MINIKUBE_PROFILE docker-env)"
(cd spring-boot-app; docker build -q -t "spring-boot-app:latest" .)

helm_install() {
  local chart_name=$1
  local namespace=$chart_name
  if [[ -n "${2:-}" ]]; then
      namespace=$2
  fi
  local release_name=$chart_name
  if [[ -n "${3:-}" ]]; then
      release_name=$3
  fi
  echo ">>>> Installing helm chart $chart_name into namespace $namespace as release $release_name"
  kubectl create namespace "$namespace" --dry-run=client -o yaml | kubectl apply -f -
  (cd "helm/$chart_name"; \
    helm dependency update >/dev/null \
    && helm upgrade --namespace "$namespace" --install "$release_name" .)
}

helm_install kube-prometheus-stack
helm_install tempo
helm_install promtail
helm_install loki

helm_install spring-boot default spring-boot-demo-app1

echo ">>>> Waiting max 5min for deployments to finish...(you may watch progress using k9s)"
kubectl wait --for=condition=ready --timeout=5m pod -n kube-prometheus-stack -l app.kubernetes.io/name=grafana
# setup port forward for grafana
echo ">>>> Setting up port-forward (end with Ctrl-C), you can login to Grafana now at http://localhost:3000"
kubectl port-forward -n kube-prometheus-stack deployment/kube-prometheus-stack-grafana 3000:3000