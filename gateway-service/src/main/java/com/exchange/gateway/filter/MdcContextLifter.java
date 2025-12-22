package com.exchange.gateway.filter;

import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;


public class MdcContextLifter<T> implements CoreSubscriber<T> {

    private final CoreSubscriber<T> coreSubscriber;

    public MdcContextLifter(CoreSubscriber<T> coreSubscriber) {
        this.coreSubscriber = coreSubscriber;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        coreSubscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T t) {
        copyToMdc();
        coreSubscriber.onNext(t);
    }

    @Override
    public void onError(Throwable throwable) {
        copyToMdc();
        coreSubscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        copyToMdc();
        coreSubscriber.onComplete();
    }

    @Override
    public Context currentContext() {
        return coreSubscriber.currentContext();
    }

    private void copyToMdc() {
        Context context = currentContext();
        if (context.isEmpty()) return;

        context.stream().forEach(entry ->
                MDC.put(entry.getKey().toString(), entry.getValue().toString())
        );
    }
}