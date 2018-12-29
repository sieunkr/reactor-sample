package com.example.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private Subscription subscription;

    @Override
    public void run(String... args) throws Exception {
        test01();
        test02();
        test03();
        test04();
    }

    private void test01(){
        Flux<String> flux = Flux.just("에디킴", "아이린");
        flux.subscribe(new MySubscriber());
    }

    private void test02(){
        Flux<String> flux = Flux.fromArray(new String[]{"에디킴", "아이유", "아이린"});
        flux.subscribe(new MySubscriber());
    }

    private void test03(){
        Flux<String> flux = Flux.fromIterable(Arrays.asList("아이유", "아이린"));
        flux.subscribe(new MySubscriber());
    }

    private void test04(){
        Flux<String> flux = Flux.empty();
        flux.subscribe(new MySubscriber());
    }

    private class MySubscriber implements Subscriber<String>{

        @Override
        public void onSubscribe(Subscription s) {
            subscription = s;
            subscription.request(1);
        }

        @Override
        public void onNext(String name) {
            System.out.println(name);
            subscription.request(1);
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {
            System.out.println("완료");
        }
    }

}

