package com.demo.reactive.graphql.demo.infrastructure.spi.random;

import com.demo.reactive.graphql.demo.infrastructure.spi.random.model.RandomDogImageResult;
import com.demo.reactive.graphql.demo.infrastructure.spi.random.settings.RandomApiSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 */
@Component
@Slf4j
public class RandomApiClient {

    private final WebClient webClient;

    public RandomApiClient(RandomApiSettings settings) {
        this.webClient = WebClient.builder()
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .baseUrl(settings.getBaseUri())
                .build();
    }


    public Mono<RandomDogImageResult> getRandomImage(int iteration) {
        log.debug("Iteration {} ", iteration);
        return webClient.get()
                .retrieve()
                .bodyToMono(RandomDogImageResult.class)
                ;
    }


    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            // clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status: {}", clientResponse.statusCode());
            // clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientResponse);
        });
    }


}
