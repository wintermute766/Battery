package ru.sberbank.learning.battery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ImageView batteryImageView;
    private TextView batteryPercentageView;
    private TextView batteryTechnologyView;
    private TextView batteryStatusView;

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int capacity = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            String tech = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

            float fPercent = ((float) level / (float) capacity) * 100f;
            int percent = Math.round(fPercent);

            int drawableLevel = percent * 100;
            batteryImageView.getDrawable().setLevel(drawableLevel);

            batteryPercentageView.setText(
                    getString(R.string.battery_percent_format,
                            percent));
            batteryTechnologyView.setText(tech);

            int state = getBatteryState(intent);
            batteryStatusView.setText(getResources().getStringArray(R.array.battery_states)[state]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryImageView = (ImageView) findViewById(R.id.battery_image);
        batteryPercentageView = (TextView) findViewById(R.id.battery_percentage);
        batteryTechnologyView = (TextView) findViewById(R.id.battery_technology);
        batteryStatusView = (TextView) findViewById(R.id.battery_status);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(batteryReceiver);
    }

    private boolean isBatteryPresent(Intent intent) {
        return intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true);
    }

    private int getBatteryState(Intent intent) {
        int state = 0;

        if (isBatteryPresent(intent)) {
            state = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN);
        }

        return state;
    }
}
