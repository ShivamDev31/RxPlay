package com.shivamdev.rxplay.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shivamdev.rxplay.R;
import com.shivamdev.rxplay.utils.Constants;
import com.shivamdev.rxplay.utils.PrefManager;
import com.shivamdev.rxplay.rx.RxPollingFunc1;
import com.shivamdev.rxplay.utils.PrettyTimeAgo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by shivam on 3/11/16.
 */

public class PrettyTimeFragment extends Fragment {

    private static final String TAG = PrettyTimeFragment.class.getSimpleName();

    private static final long POLLING_TIME = 3 * 60 * 1000;

    @BindView(R.id.tv_pretty_time1)
    TextView tvPrettyTime1;

    @BindView(R.id.tv_pretty_time2)
    TextView tvPrettyTime2;

    @BindView(R.id.tv_pretty_time3)
    TextView tvPrettyTime3;

    @BindView(R.id.tv_pretty_time4)
    TextView tvPrettyTime4;

    public static PrettyTimeFragment newInstance() {
        PrettyTimeFragment fragment = new PrettyTimeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_pretty_time, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Observable.just(POLLING_TIME)
                .retryWhen(new RxPollingFunc1(POLLING_TIME, TimeUnit.MILLISECONDS))
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        setPrettyTime();
                    }
                });
    }

    private void setPrettyTime() {
        long oldTime = PrefManager.getInstance().getLong(Constants.Shared.PRETTY_TIME);
        String sundarTime2 = PrettyTimeAgo.getTimeAgo(oldTime);
        String sundarTime3 = DateUtils.getRelativeTimeSpanString(getActivity(), oldTime, true).toString();
        String sundarTime4 = DateUtils.getRelativeTimeSpanString(getActivity(), oldTime, false).toString();
        tvPrettyTime2.setText(sundarTime2);
        tvPrettyTime3.setText(sundarTime3);
        tvPrettyTime4.setText(sundarTime4);
    }
}