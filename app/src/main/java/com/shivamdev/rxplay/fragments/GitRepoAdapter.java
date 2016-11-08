package com.shivamdev.rxplay.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shivamdev.rxplay.R;
import com.shivamdev.rxplay.network.GitData;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by shivam on 13/8/16.
 */

public class GitRepoAdapter extends RecyclerView.Adapter<GitRepoAdapter.GitHolder> {


    private static final String TAG = GitRepoAdapter.class.getSimpleName();

    private PublishSubject<Void> mySubject;

    private Context mContext;
    private List<GitData> gitData;

    public GitRepoAdapter(Context context) {
        this.mContext = context;
        this.gitData = new ArrayList<>();
        mySubject = PublishSubject.create();
    }

    @Override
    public GitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.subject_fragment, parent, false);
        return new GitHolder(view);
    }

    @Override
    public void onBindViewHolder(GitHolder holder, int position) {
        holder.tvTitle.setText(mContext.getResources().getString(R.string.title) + gitData.get(position).repoName);
        holder.tvDesc.setText(mContext.getResources().getString(R.string.description) + gitData.get(position).repoDesc);
        holder.tvUrl.setText(mContext.getResources().getString(R.string.url) + gitData.get(position).htmlUrl);
    }

    @Override
    public int getItemCount() {
        return gitData.size();
    }

    Observable<Void> getClickListener() {
        Log.d(TAG, "getClickListener: ");
        return mySubject;
    }

    void refreshData(List<GitData> freshData) {
        gitData.clear();
        gitData.addAll(freshData);
        notifyDataSetChanged();
    }

    class GitHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDesc;
        private TextView tvUrl;

        GitHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            tvUrl = (TextView) view.findViewById(R.id.tv_url);

            view.setOnClickListener(v -> {
                mySubject.onNext(null);
                mySubject.onCompleted();
            });
        }
    }
}