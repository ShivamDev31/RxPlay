package com.shivamdev.rxplay;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shivamdev.rxplay.fragments.PickerFragment;
import com.shivamdev.rxplay.utils.Constants;
import com.shivamdev.rxplay.utils.PrefManager;
import com.shivamdev.rxplay.utils.CommonUtils;
import com.shivamdev.rxplay.fragments.RetrofitFragment;
import com.shivamdev.rxplay.operators.CommonOperators;
import com.shivamdev.rxplay.service.GpsService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LinearLayout llButtonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long oldTime = PrefManager.getInstance().getLong(Constants.Shared.PRETTY_TIME);

        if (oldTime == 0) {
            PrefManager.getInstance().putLong(Constants.Shared.PRETTY_TIME, System.currentTimeMillis());
        }
        final Button bRetrofit = (Button) findViewById(R.id.b_retrofit);
        final Button bOperators = (Button) findViewById(R.id.b_operator);
        final Button bStartGpsService = (Button) findViewById(R.id.b_start_gps_service);
        final Button bStopGpsService = (Button) findViewById(R.id.b_stop_gps_service);
        final Button bPicker = (Button) findViewById(R.id.b_picker);
        llButtonsLayout = (LinearLayout) findViewById(R.id.ll_buttons_layout);
        bRetrofit.setOnClickListener(this);
        bOperators.setOnClickListener(this);
        bStartGpsService.setOnClickListener(this);
        bStopGpsService.setOnClickListener(this);
        bPicker.setOnClickListener(this);
    }

    private void addOperatorsFragment() {
        CommonOperators fragment = CommonOperators.newInstance();
        addFragment(fragment);
    }

    private void addRetrofitFragment() {
        RetrofitFragment fragment = RetrofitFragment.newInstance();
        addFragment(fragment);
    }


    private void addPickerFragment() {
        PickerFragment fragment = PickerFragment.newInstance();
        addFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideButtons(false);
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ll_fragment, fragment, TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_retrofit:
                hideButtons(true);
                addRetrofitFragment();
                break;
            case R.id.b_operator:
                hideButtons(true);
                addOperatorsFragment();
                break;
            case R.id.b_start_gps_service:
                startGpsService();
                break;
            case R.id.b_stop_gps_service:
                stopGpsService();
                break;
            case R.id.b_picker:
                hideButtons(true);
                addPickerFragment();
                break;
        }
    }

    private void startGpsService() {
        if (!CommonUtils.isServiceRunning(this, GpsService.class)) {
            Intent intent = new Intent(this, GpsService.class);
            startService(intent);
        }
    }

    private void stopGpsService() {
        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Intent intent = new Intent(this, GpsService.class);
        stopService(intent);

        if (CommonUtils.isServiceRunning(this, GpsService.class)) {
            stopService(intent);
        }
    }

    private void hideButtons(boolean hide) {
        if (hide) {
            llButtonsLayout.setVisibility(View.GONE);
        } else {
            llButtonsLayout.setVisibility(View.VISIBLE);
        }
    }
}