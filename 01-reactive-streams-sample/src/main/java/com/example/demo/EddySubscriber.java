package com.example.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EddySubscriber implements Subscriber<Integer> {

    Logger logger = LoggerFactory.getLogger(EddySubscriber.class);

    private String name;
    private Integer count;
    private final Integer DEMAND_COUNT = 3;
    private Subscription subscription;

    public EddySubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        logger.info("subscriber - onSubscribe");

        count = DEMAND_COUNT;
        this.subscription = subscription;

        //TODO: 외부 메서드로 분리
        this.subscription.request(DEMAND_COUNT);
    }

    @Override
    public void onNext(Integer integer) {
        logger.info("subscriber - onNext");

        synchronized (this){
            count--;

            if(count == 0){
                logger.info("count is zero");
                count = DEMAND_COUNT;
                subscription.request(count);
            }

        }

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {
        logger.info("subscriber - onComplete");
    }
}
