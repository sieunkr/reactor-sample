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
        //test05();
        //test06();
        //test07();
        test08();
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

    private void test06(){

        Flux<String> names = Flux.just("kim", "lee");
        //names.map(name -> name.toUpperCase()).subscribe(System.out::println);
        names.map(String::toUpperCase).subscribe(System.out::println);
    }

    private void test07(){

        Flux<Integer> integerFlux = Flux.just(1, 2, 3);
        integerFlux.flatMap(i -> Flux.range(0, i)).subscribe(System.out::println);
    }

    private void test08(){
        Flux<String> flux = Flux.just("I like you","I hate you");
        flux.flatMap(s -> Flux.fromArray(s.split(" ")))
                .subscribe(System.out::println);
    }

    


}

