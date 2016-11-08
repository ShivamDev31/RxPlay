package com.shivamdev.rxplay.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by shivam on 3/11/16.
 */

public class RxPollingFunc1 implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final long time;
    private final TimeUnit timeUnit;

    public RxPollingFunc1(long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.delay(time, timeUnit);
    }
}
