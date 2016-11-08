package com.shivamdev.rxplay.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shivamdev.rxplay.R;
import com.shivamdev.rxplay.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shivam on 8/11/16.
 */

public class PickerFragment extends Fragment {

    private static final String TAG = PickerFragment.class.getSimpleName();

    @BindView(R.id.tv_date_picker)
    TextView tvDatePicker;

    @BindView(R.id.tv_time_picker)
    TextView tvTimePicker;

    public static PickerFragment newInstance() {
        PickerFragment fragment = new PickerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picker, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.tv_date_picker)
    public void openDatePicker() {
        CommonUtils.openDatePicker(getActivity())
                .subscribe(selectedDate -> {
                    Log.d(TAG, "openDatePicker: " + selectedDate);
                    tvDatePicker.setText(selectedDate);
                });
    }

    @OnClick(R.id.tv_time_picker)
    public void openTimePicker() {
        CommonUtils.openTimePicker(getActivity())
                .subscribe(selectedTime -> {
                    Log.d(TAG, "openTimePicker: " + selectedTime);
                    tvTimePicker.setText(selectedTime);
                });
    }
}