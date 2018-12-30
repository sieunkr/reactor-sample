package com.example.demo;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //test01();
        //test02();
        //test03();
        test04();
    }

    private void test01(){
        Flux<String> flux = Flux.just("에디킴", "아이린");
        flux.subscribe(System.out::println);
    }

    private void test02(){
        Flux<String> flux = Flux.just("에디킴", "아이린", "아이유", "수지");
        flux.subscribe(System.out::println,
                error -> System.out.println("에러"),
                () -> System.out.println("완료"));
    }

    private void test03(){
        Flux<String> flux = Flux.just("에디킴", "아이린", "아이유", "수지");
        flux.
                log().
                subscribe(System.out::println,
                error -> System.out.println("에러"),
                () -> System.out.println("완료"),
                sub -> sub.request(1));
    }

    private void test04(){

        Flux<String> flux = Flux.just("에디킴", "아이린", "아이유", "수지");
        flux.subscribe(new BaseSubscriber<String>() {

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
                //super.hookOnSubscribe(subscription);
            }

            @Override
            protected void hookOnNext(String value) {
                System.out.println(value);
                super.hookOnNext(value);
            }
        });

    }
}

