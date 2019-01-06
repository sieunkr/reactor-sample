package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //public static <T> Flux<T> generate(Consumer<SynchronousSink<T>> generator)
        //test01();

        //public static <T,S> Flux<T> generate(Callable<S> stateSupplier,
        //                                     BiFunction<S,SynchronousSink<T>,S> generator)
        //test02();

        //public static <T,S> Flux<T> generate(Callable<S> stateSupplier,
        //                                     BiFunction<S,SynchronousSink<T>,S> generator,
        //                                     Consumer<? super S> stateConsumer)
        //test03();

        //test04();

        //test05();

        test06();

    }

    private void test01(){

    }

    private void test02(){

        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state);
                    if (state == 10) {
                        sink.complete();
                    }
                    return state + 1;
                });

        /*
        Flux<String> flux = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3*i);
                    if (i == 10) sink.complete();
                    return state;
                });
        */

        flux.subscribe(System.out::println,
                error -> System.out.println("에러"),
                () -> System.out.println("완료"));

    }

    private void test03(){
    }

    private void test04(){

        Consumer<FluxSink<Long>> consumer = emitter -> {
            long current = 1, prev = 0;
            AtomicBoolean stop = new AtomicBoolean( false);

            emitter.onDispose(()->{
                stop.set(true);
                System.out.println("******* Stop Received ****** ");
            });

            emitter.onRequest(con  -> {
                System.out.println(con);
            });

            while (current > 0) {
                emitter.next(current);
                System.out.println(" generated " + current);
                long next = current + prev;
                prev = current;
                current = next;
            }
            emitter.complete();
        };

        Flux<Long> fibonacciGenerator = Flux.create(consumer);

        List<Long> fibonacciSeries = new LinkedList<>();


        fibonacciGenerator.take(50).subscribe(t -> {
            System.out.println(" consuming " + t);
            fibonacciSeries.add(t);
        });



        /*
        fibonacciGenerator.subscribe(t -> {
                    System.out.println(" consuming " + t);
                },
                error -> System.out.println("에러"),
                () -> System.out.println("성공"),
                sub -> sub.request(5));
        */

        //System.out.println(fibonacciSeries);


    }

    interface MyEventListener<T> {
        void onDataChunk(List<T> chunk);
        void processComplete();
    }

    interface MyEventProcessor {
        void register(MyEventListener<String> eventListener);
        void dataChunk(String... values);
        void processComplete();
    }

    private MyEventProcessor myEventProcessor = new MyEventProcessor() {

        private MyEventListener<String> eventListener;
        private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        @Override
        public void register(MyEventListener<String> eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void dataChunk(String... values) {
            executor.schedule(() -> eventListener.onDataChunk(Arrays.asList(values)),
                    500, TimeUnit.MILLISECONDS);
        }

        @Override
        public void processComplete() {
            executor.schedule(() -> eventListener.processComplete(),
                    500, TimeUnit.MILLISECONDS);
        }
    };

    private void test05(){

        Flux<String> bridge = Flux.create(sink -> {
            myEventProcessor.register( // <4>
                    new MyEventListener<String>() { // <1>

                        public void onDataChunk(List<String> chunk) {
                            for(String s : chunk) {
                                sink.next(s); // <2>
                            }
                        }

                        public void processComplete() {
                            sink.complete(); // <3>
                        }
                    });
        });

        bridge.subscribe(System.out::println);

        myEventProcessor.dataChunk("abc", "def");

    }

    private void test06(){

        /*
        https://www.infoq.com/articles/reactor-by-example

        SomeFeed<PriceTick> feed = new SomeFeed<>();
        Flux<PriceTick> flux =
                Flux.create(emitter ->
                {
                    SomeListener listener = new SomeListener() {
                        @Override
                        public void priceTick(PriceTick event) {
                            emitter.next(event);
                            if (event.isLast()) {
                                emitter.complete();
                            }
                        }

                        @Override
                        public void error(Throwable e) {
                            emitter.error(e);
                        }};
                    feed.register(listener);
                }, FluxSink.OverflowStrategy.BUFFER);

        ConnectableFlux<PriceTick> hot = flux.publish();

        */

    }


}

