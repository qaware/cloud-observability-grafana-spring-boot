# Cloud Observability with Grafana and Spring Boot

A Kubernetes observability demo based on
minikube. It demonstrates how to monitor a Spring Boot app using
traces (Tempo), metrics (Prometheus) and logs (Loki) all in Grafana.

[Check out this blog post for more details](https://blog.qaware.de/posts/cloud-observability-grafana-spring-boot/)

## Prerequisites

Please make sure the following tools are available as commands:

* bash
* [docker](https://docs.docker.com/get-docker/)
* [minikube](https://minikube.sigs.k8s.io/docs/start/)
* [helm](https://helm.sh/docs/intro/install/)
* [kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl)
* [k9s](https://github.com/derailed/k9s) (optional)

## Usage

Executing

```
./run-demo.sh
```

will start a minikube cluster, deploy [Grafana,
Prometheus](helm/kube-prometheus-stack), [Tempo](helm/tempo),
[Loki](helm/loki), [Promtail](helm/promtail) and a
[Spring Boot Demo App](spring-boot-app) using helm.

It binds a port-forward to Grafana at http://localhost:3000. Open the "Spring Boot Demo" dashboard in there.

Later on, you can clean up the demo with 
```
minikube delete --profile observability-demo
```

## Links & References

* https://github.com/grafana/tns
* https://github.com/micrometer-metrics/micrometer
* https://linuxczar.net/blog/2022/01/17/java-spring-boot-prometheus-exemplars/
* https://github.com/open-telemetry/opentelemetry-java-instrumentation
