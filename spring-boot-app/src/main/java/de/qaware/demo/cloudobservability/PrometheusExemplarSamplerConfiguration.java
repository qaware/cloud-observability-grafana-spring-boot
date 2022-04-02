package de.qaware.demo.cloudobservability;

import io.micrometer.core.instrument.Clock;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exemplars.DefaultExemplarSampler;
import io.prometheus.client.exemplars.tracer.otel_agent.OpenTelemetryAgentSpanContextSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Temporary instrumentation of PrometheusMeterRegistry with
 * Exemplar Sampler for Spring Boot Actuator. Required as long as
 * https://github.com/spring-projects/spring-boot/pull/30472 isn't merged.
 */
@Configuration
public class PrometheusExemplarSamplerConfiguration {
    @Bean
    public PrometheusMeterRegistry prometheusMeterRegistryWithExemplar(PrometheusConfig prometheusConfig,
                                                                       CollectorRegistry collectorRegistry, Clock clock) {
        return new PrometheusMeterRegistry(prometheusConfig, collectorRegistry, clock,
                // this will lead to ClassCastException if -javaagent:opentelemetry-javaagent.jar is missing!
                new DefaultExemplarSampler(new OpenTelemetryAgentSpanContextSupplier() {
                    // Only return traceId if trace is actually sampled, as otherwise exemplar is useless
                    // Can be removed once https://github.com/prometheus/client_java/pull/766 is merged & released
                    @Override
                    public String getTraceId() {
                        if (!Span.current().getSpanContext().isSampled()) {
                            return null;
                        }
                        return super.getTraceId();
                    }
                })
        );
    }
}
