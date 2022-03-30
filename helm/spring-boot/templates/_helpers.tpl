{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart. Use .Release.Name for simplicity.
*/}}
{{- define "app.name" -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Expand the service name (for re-use in cron jobs).
*/}}
{{- define "app.servicename" -}}
{{ printf "%s" (include "app.name" .) }}
{{- end -}}

{{/*
App deployment labels
*/}}
{{- define "app.labels" -}}
{{ include "app.selectorLabels" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "app.selectorLabels" -}}
app.kubernetes.io/name: {{ include "app.name" . }}
{{- end -}}