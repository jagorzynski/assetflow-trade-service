package com.sothrose.assetflow_trade_service.configuration;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class MarketDataServiceWebClientConfig {

  @Value("${market-data-service.base-url:http://localhost:8083}")
  private String marketDataServiceBaseUrl;

  @Bean
  public WebClient marketDataServiceWebClient(
      WebClient.Builder webClientBuilder,
      CircuitBreakerRegistry circuitBreakerRegistry,
      RetryRegistry retryRegistry,
      RateLimiterRegistry rateLimiterRegistry,
      BulkheadRegistry bulkheadRegistry,
      TimeLimiterRegistry timeLimiterRegistry) {

    var circuitBreaker = circuitBreakerRegistry.circuitBreaker("marketDataService");
    var retry = retryRegistry.retry("marketDataService");
    var rateLimiter = rateLimiterRegistry.rateLimiter("marketDataService");
    var bulkhead = bulkheadRegistry.bulkhead("marketDataService");
    var timeLimiter = timeLimiterRegistry.timeLimiter("marketDataService");

    return webClientBuilder
        .baseUrl(marketDataServiceBaseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .filter(
            (request, next) ->
                Mono.defer(() -> next.exchange(request))
                    .transformDeferred(RetryOperator.of(retry))
                    .transformDeferred(TimeLimiterOperator.of(timeLimiter))
                    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                    .transformDeferred(RateLimiterOperator.of(rateLimiter))
                    .transformDeferred(BulkheadOperator.of(bulkhead)))
        .build();
  }
}
