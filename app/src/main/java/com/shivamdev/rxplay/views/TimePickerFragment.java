package com.shivamdev.rxplay.views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.shivamdev.rxplay.utils.DateTimeUtils;
import com.shivamdev.rxplay.utils.Logger;

import java.util.Calendar;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by shivam on 8/11/16.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = TimePickerFragment.class.getSimpleName();

    private PublishSubject<String> selectedTimeSubject;


    public static TimePickerFragment newInstance() {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TimePickerFragment() {
        selectedTimeSubject = PublishSubject.create();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final int mHour = calendar.get(Calendar.HOUR);
        final int mMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, mHour, mMinute, false);
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Logger.log(TAG, "onTimeSet: ");
        Calendar cal = Calendar.getInstance();
        cal.set(0, 0, 0, hour, minute);
        String selectedTime = DateTimeUtils.format(DateTimeUtils.Formats.HH_mm_aa, cal);
        selectedTimeSubject.onNext(selectedTime);
        selectedTimeSubject.onCompleted();
    }

    public Observable<String> getSelectedTime() {
        Logger.log(TAG, "getSelectedTime: ");
        return selectedTimeSubject;
    }
}