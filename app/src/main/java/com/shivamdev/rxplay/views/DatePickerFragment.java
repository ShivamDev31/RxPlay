package com.shivamdev.rxplay.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.shivamdev.rxplay.utils.DateTimeUtils;
import com.shivamdev.rxplay.utils.Logger;

import java.util.Calendar;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by shivam on 8/11/16.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DatePickerFragment.class.getSimpleName();
    private static final String MIN_DATE_KEY = "min_date_key";
    private static final String MAX_DATE_KEY = "max_date_key";

    private PublishSubject<String> selectedDateSubject;

    private long minDate;
    private long maxDate;


    public static DatePickerFragment newInstance(long minDate, long maxDate) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putLong(MIN_DATE_KEY, minDate);
        args.putLong(MAX_DATE_KEY, maxDate);
        fragment.setArguments(args);
        return fragment;
    }

    public DatePickerFragment() {
        selectedDateSubject = PublishSubject.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        minDate = getArguments().getLong(MIN_DATE_KEY);
        maxDate = getArguments().getLong(MAX_DATE_KEY);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final int mDate = calendar.get(Calendar.DATE);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mYear = calendar.get(Calendar.YEAR);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, mYear, mMonth, mDate);
        if (minDate > 0) {
            datePickerDialog.getDatePicker().setMinDate(minDate);
        }
        if (maxDate > 0) {
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
        }
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Logger.log(TAG, "onDateSet: ");
        final Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        String selectedDate = DateTimeUtils.format(DateTimeUtils.Formats.dd_MM_yyyy, cal);
        selectedDateSubject.onNext(selectedDate);
        selectedDateSubject.onCompleted();
    }

    public Observable<String> getSelectedDate() {
        Log.d(TAG, "getSelectedDate: ");
        return selectedDateSubject;
    }
}