package de.qaware.demo.cloudobservability;

import io.micrometer.core.instrument.Clock;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exemplars.DefaultExemplarSampler;
import io.prometheus.client.exemplars.tracer.otel_agent.OpenTelemetryAgentSpanContextSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Temporary instrumentation of PrometheusMeterRegistry
 * with Exemplar Sampler for Spring Boot Actuator.
 */
@Configuration
public class PrometheusExemplarSamplerConfiguration {
    @Bean
    public PrometheusMeterRegistry prometheusMeterRegistryWithExemplar(PrometheusConfig prometheusConfig,
                                                                       CollectorRegistry collectorRegistry, Clock clock) {
        return new PrometheusMeterRegistry(prometheusConfig, collectorRegistry, clock,
                // this will lead to ClassCastException if -javaagent:opentelemetry-javaagent.jar is missing!
                new DefaultExemplarSampler(new OpenTelemetryAgentSpanContextSupplier())
        );
    }
}
