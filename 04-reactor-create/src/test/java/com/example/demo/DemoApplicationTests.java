package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test(expected = NullPointerException.class)
    public void 테스트01(){

        Flux.generate(null, (s, o) -> s, s -> {
        });

    }

    @Test
    public void fluxCreateBuffered() {
        //https://github.com/reactor/reactor-core/blob/master/reactor-core/src/test/java/reactor/core/publisher/FluxCreateTest.java
        AtomicInteger onDispose = new AtomicInteger();
        AtomicInteger onCancel = new AtomicInteger();
        Flux<String> created = Flux.create(s -> {
            s.onDispose(onDispose::getAndIncrement)
                    .onCancel(onCancel::getAndIncrement);
            s.next("test1");
            s.next("test2");
            s.next("test3");
            s.complete();
        });

        assertThat(created.getPrefetch()).isEqualTo(-1);

        StepVerifier.create(created)
                .expectNext("test1", "test2", "test3")
                .verifyComplete();

        assertThat(onDispose.get()).isEqualTo(1);
        assertThat(onCancel.get()).isEqualTo(0);
    }

    @Test
    public void fluxCreateBuffered2() {
        AtomicInteger cancellation = new AtomicInteger();
        AtomicInteger onCancel = new AtomicInteger();
        StepVerifier.create(Flux.create(s -> {
            s.onDispose(cancellation::getAndIncrement);
            s.onCancel(onCancel::getAndIncrement);
            s.next("test1");
            s.next("test2");
            s.next("test3");
            s.complete();
        }).publishOn(Schedulers.parallel()))
                .expectNext("test1", "test2", "test3")
                .verifyComplete();

        assertThat(cancellation.get()).isEqualTo(1);
        assertThat(onCancel.get()).isEqualTo(0);
    }

    @Test
    public void fluxCreateBufferedError() {
        Flux<String> created = Flux.create(s -> {
            s.next("test1");
            s.next("test2");
            s.next("test3");
            s.error(new Exception("test"));
        });

        StepVerifier.create(created)
                .expectNext("test1", "test2", "test3")
                .verifyErrorMessage("test");
    }

}

