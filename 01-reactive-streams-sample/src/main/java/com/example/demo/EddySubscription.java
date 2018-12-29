package com.example.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class EddySubscription implements Subscription {

    Logger logger = LoggerFactory.getLogger(EddySubscription.class);

    private final ExecutorService executorService;
    private Subscriber<? super Integer> subscriber;
    private final AtomicInteger value;
    private AtomicBoolean isCanceled;

    public EddySubscription(Subscriber<? super Integer> subscriber, ExecutorService executorService) {
        this.subscriber = subscriber;
        this.executorService = executorService;

        value = new AtomicInteger();
        isCanceled = new AtomicBoolean(false);
    }

    @Override
    public void request(long n) {
        if(isCanceled.get()){
            return;
        }

        if(n < 0){
            //TODO:error
        }
        else{
            pushItems(n);
        }
    }

    @Override
    public void cancel() {

    }

    private void pushItems(long n){

        for(int i = 0; i < n; i++){

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    int v = value.incrementAndGet();


                    if(v > 1000){
                        logger.info("pushItem is over ");
                        subscriber.onComplete();
                    }
                    else{
                        logger.info("pushItem + " + v);
                        subscriber.onNext(v);
                    }

                }
            });
        }

    }
}
