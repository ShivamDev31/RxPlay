package com.shivamdev.rxplay.operators;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.shivamdev.rxplay.R;
import com.shivamdev.rxplay.utils.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * Created by shivam on 13/8/16.
 */

/**
 * Common operators that will be covered in this fragment
 * 1. just
 * 2. from
 * 3. range
 * 4. debounce
 * 6. filter
 * 7.
 * 8.
 */

public class CommonOperators extends Fragment implements View.OnClickListener {

    private static final String TAG = CommonOperators.class.getSimpleName();

    private String[] text = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    private int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private TextView tvData;
    private TextView tvResult;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public static CommonOperators newInstance() {
        CommonOperators fragment = new CommonOperators();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_operators_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvData = (TextView) view.findViewById(R.id.tv_string);
        tvResult = (TextView) view.findViewById(R.id.tv_result);

        tvData.setText(Arrays.asList(text).toString());

        final Button bJust = (Button) view.findViewById(R.id.b_just);
        final Button bFrom = (Button) view.findViewById(R.id.b_from);
        final Button bCreate = (Button) view.findViewById(R.id.b_create);
        final Button bRange = (Button) view.findViewById(R.id.b_range);
        final Button bLast = (Button) view.findViewById(R.id.b_last);
        final Button bFilter = (Button) view.findViewById(R.id.b_filter);
        final Button bInterval = (Button) view.findViewById(R.id.b_interval);
        final Button bMap = (Button) view.findViewById(R.id.b_map);
        final Button bFlatMap = (Button) view.findViewById(R.id.b_flat_map);
        final Button bMerge = (Button) view.findViewById(R.id.b_merge);
        final Button bCombined = (Button) view.findViewById(R.id.b_combined);
        final EditText etEmailDebounce = (EditText) view.findViewById(R.id.et_email_debounce);
        bJust.setOnClickListener(this);
        bFrom.setOnClickListener(this);
        bCreate.setOnClickListener(this);
        bRange.setOnClickListener(this);
        bLast.setOnClickListener(this);
        bFilter.setOnClickListener(this);
        bInterval.setOnClickListener(this);
        bMap.setOnClickListener(this);
        bFlatMap.setOnClickListener(this);
        bMerge.setOnClickListener(this);
        bCombined.setOnClickListener(this);

        RxTextView.textChanges(etEmailDebounce)
                .debounce(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.log(TAG, "onError: " + e);
                    }

                    @Override
                    public void onNext(CharSequence target) {
                        if (EMAIL_ADDRESS.matcher(target).matches()) {
                            etEmailDebounce.setBackgroundColor(getResources().getColor(R.color.green));
                        } else {
                            etEmailDebounce.setBackgroundColor(getResources().getColor(R.color.red));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_just:
                just();
                break;
            case R.id.b_from:
                from();
                break;
            case R.id.b_create:
                create();
                break;
            case R.id.b_range:
                range();
                break;
            case R.id.b_last:
                last();
                break;
            case R.id.b_filter:
                filter();
                break;
            case R.id.b_interval:
                interval();
                break;
            case R.id.b_map:
                map();
                break;
            case R.id.b_flat_map:
                flatMap();
                break;
            case R.id.b_merge:
                merge();
                break;

            case R.id.b_combined:
                combined();
                break;
        }
    }

    private void just() {
        clear();
        Subscription subs = Observable.just(1, 2, 3, 4)
                .subscribe(integer -> {
                    tvResult.append(String.valueOf(integer) + "\n");
                });
        compositeSubscription.add(subs);
    }

    private void from() {
        clear();
        Subscription subs = Observable.from(Arrays.asList(text))
                .subscribe(s -> {
                    tvResult.append(s + "\n");
                });
        compositeSubscription.add(subs);
    }

    private void create() {
        clear();
        Observable<List<String>> observable = Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                subscriber.onNext(Arrays.asList(text));
                subscriber.onCompleted();
            }
        });

        observable
                .subscribe(strings -> {
                    tvResult.append(strings.toString());
                });

    }

    private void range() {
        clear();
        Subscription subs = Observable.from(Arrays.asList(text))
                .range(3, 3)
                .subscribe(s -> {
                    tvResult.append(s + "\n");
                });
        compositeSubscription.add(subs);
    }

    private void last() {
        clear();
        Subscription subs = Observable.from(Arrays.asList(text))
                .last()
                .subscribe(s -> {
                    tvResult.append(s + "\n");
                });
        compositeSubscription.add(subs);
    }

    private void filter() {
        clear();
        Subscription subs = Observable.from(Arrays.asList(text))
                .filter(s -> s.length() > 3)
                .subscribe(s -> {
                    tvResult.append(s + "\n");
                });
        compositeSubscription.add(subs);
    }


    private void map() {
        clear();
        Subscription subs = Observable.from(Arrays.asList(text))
                .map(String::length)
                .subscribe(s -> {
                    tvResult.append(s + "\n");
                });
        compositeSubscription.add(subs);
    }

    private void interval() {
        clear();
        Subscription subs = Observable.from(Arrays.asList(text))
                .delay(2, TimeUnit.SECONDS)
                .first()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    tvResult.append(s + "\n");
                });
        compositeSubscription.add(subs);
    }

    private void flatMap() {
        clear();
        Subscription subs = Observable.just("1/5/8", "1/9/11/58/16/", "9/15/56/49/21")
                .flatMap(string -> Observable.from(string.split("/")))
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return Integer.valueOf(s);
                    }
                })
                .map(integer -> integer * 5)
                .filter(integer -> integer > 20)
                .subscribe(integer -> {
                    tvResult.append(String.valueOf(integer) + "\n");
                });
        compositeSubscription.add(subs);
    }

    private void combined() {
        clear();
        Subscription subs = Observable.from(text)
                .map(s -> s.length())
                .filter(integer -> integer > 3)
                .subscribe(integer -> {
                    tvResult.append(String.valueOf(integer) + "\n");
                });
        compositeSubscription.add(subs);
    }

    private void merge() {
        clear();
        Subscription subs = Observable
                .merge(Observable.just(7, 8, 9, 10), Observable.just(1, 2, 3, 4))
                .subscribe(integer -> tvResult.append(integer + "\n"));
        compositeSubscription.add(subs);
    }

    private void clear() {
        tvResult.setText(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
