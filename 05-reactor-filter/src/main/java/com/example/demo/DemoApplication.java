package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Iterator;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //test01();
        //test02();
        //test03();
        //test04();
        test05();
    }

    private void test01(){
        Flux<String> flux = Flux.just("blue", "green", "orange", "purple");

        flux.filter(color -> !color.equals("blue"))
                .subscribe(System.out::println);
    }

    private void test02(){

        Flux<String> flux = Flux.just("EDDY");
        flux.map(String::toLowerCase)
            .subscribe(System.out::println);
    }

    private void test03() {
        Iterator<String> iterable = Flux.just("eddy", "jung").toIterable().iterator();
        //iterable.
    }

    private void test04() {

        Flux<String> first = Flux.just("Eddy", "Alice");
        Flux<String> last = Flux.just("Kim", "Lee");

        Flux<String> fullName = Flux.zip(first, last)
                                    .map(t -> t.getT1() + " " + t.getT2());
        fullName.subscribe(System.out::println);
    }

    private void test05(){
        Flux<String> name = Flux.just("Eddy", "Alice", "A", "B", "C");

        //TODO: request(n) Backpressure 와 다른 점은??
        name.take(3)
                .subscribe(System.out::println);

    }
    
}

