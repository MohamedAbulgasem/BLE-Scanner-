package com.madosweb.bluetoothappexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Mohamed Abulgasem 2018/12/04
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
    }

    /**
     * @param view View
     */
    public void scanBleDevices(View view) {
        startActivity(new Intent(this, DeviceScanActivity.class));
    }

    /**
     * @param view View
     */
    public void exit(View view) {
        moveTaskToBack(true);
    }
}