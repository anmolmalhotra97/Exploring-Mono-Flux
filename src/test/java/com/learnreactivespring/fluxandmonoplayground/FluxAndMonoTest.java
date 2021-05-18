package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FluxAndMonoTest {

    @Test
    void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
//                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("After Error"))
                .log();

        stringFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println("Exception is: " + e)
                        , () -> System.out.println("Completed"));
    }

    @Test
    void fluxTestElements_WithoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }


    @Test
    void fluxTestElements_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectErrorMessage("Exception occurred")
//                .expectError(RuntimeException.class)
                .verify();
    }


    @Test
    void fluxTestElementsVariation_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .expectErrorMessage("Exception occurred")
//                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void fluxTestElementsCount_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Exception occurred")
//                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void monoTest(){
        Mono<String> stringMono = Mono.just("Spring");
        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    void monoTest_WithError() {
        StepVerifier.create(Mono.error(new RuntimeException("Exception occurred")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
