package com.shivamdev.rxplay.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shivamdev.rxplay.R;
import com.shivamdev.rxplay.common.RxApplication;
import com.shivamdev.rxplay.network.GitApi;
import com.shivamdev.rxplay.network.GitData;
import com.shivamdev.rxplay.utils.CommonUtils;
import com.shivamdev.rxplay.utils.Logger;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Shivam on 28-08-2016.
 */

public class RetrofitFragment extends Fragment {

    private static final String TAG = RetrofitFragment.class.getSimpleName();

    private RecyclerView rvGitList;
    private EditText etUsername;
    private ProgressBar pbLoader;

    @Inject
    public GitApi gitApi;
    private CompositeSubscription compositeSubscription;

    public static RetrofitFragment newInstance() {
        RetrofitFragment fragment = new RetrofitFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxApplication.getInstance().getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.retrofit_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compositeSubscription = new CompositeSubscription();

        rvGitList = (RecyclerView) view.findViewById(R.id.rv_git_list);
        etUsername = (EditText) view.findViewById(R.id.et_git_user);
        final Button bFetchRepo = (Button) view.findViewById(R.id.b_fetch_repo);
        pbLoader = (ProgressBar) view.findViewById(R.id.pb_loader);
        bFetchRepo.setOnClickListener(click -> {
            CommonUtils.hideKeyboard(getActivity());
            getRepoData();
        });

        rvGitList.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void getRepoData() {
        String userName = etUsername.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getActivity(), R.string.username_error, Toast.LENGTH_SHORT).show();
            return;
        }


        /**
         * EITHER ONCOMPLETE OR ONERROR WILL BE CALLED
         * TRY WITH WRONG GITHUB ID
         */

        pbLoader.setVisibility(View.VISIBLE);
        Subscription subs = gitApi.getUserRepos(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GitData>>() {
                    @Override
                    public void onCompleted() {
                        pbLoader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // In case of any error it will be handled here.
                        Logger.log(e.toString());
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<GitData> gitData) {
                        // set the data to Recycler view list here.
                        GitRepoAdapter adapter = new GitRepoAdapter(getActivity());
                        rvGitList.setAdapter(adapter);
                        adapter.refreshData(gitData);

                        Subscription subs = adapter.getClickListener()
                                .subscribe(aVoid -> {
                                    getRepoData();
                                });

                        compositeSubscription.add(subs);
                    }
                });

        compositeSubscription.add(subs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // To avoid leakage we need to unsubscribe the Subscription
        compositeSubscription.unsubscribe();
    }
}