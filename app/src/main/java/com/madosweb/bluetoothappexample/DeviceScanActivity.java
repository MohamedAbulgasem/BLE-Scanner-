package com.madosweb.bluetoothappexample;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.madosweb.bluetoothappexample.adapter.DeviceListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Abulgasem 2018/12/04
 */
public class DeviceScanActivity extends AppCompatActivity implements DeviceListAdapter.DeviceListAdapterOnClickHandler {

    @BindView(R.id.ble_devices_rv)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private final int ENABLE_BT_REQUEST = 333;
    private static final long SCAN_PERIOD = 15000;

    private BluetoothAdapter bluetoothAdapter;
    private DeviceListAdapter deviceListAdapter;
    private List<BluetoothDevice> devices;
    private boolean isScanning;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        ButterKnife.bind(this);
        setDeviceScanActivity();
    }

    private void setDeviceScanActivity() {
        setTitle(R.string.ble_devices_activity_title);
        handler = new Handler();
        setDevicesRecyclerView();
        setBluetoothAdapter();
    }

    /**
     *
     */
    private void setBluetoothAdapter() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager != null ? bluetoothManager.getAdapter() : null;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST);
        } else {
            scanLeDevice(true);
        }
    }

    /**
     * @param enable boolean
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            isScanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);

            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    if (deviceListAdapter.getDevices().isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DeviceScanActivity.this, "No BLE devices were found", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(DeviceScanActivity.this, MainActivity.class));
                            }
                        });

                    }
                }
            }, SCAN_PERIOD);
        } else {
            isScanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
        ///
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    deviceListAdapter.addDevice(device);
                    deviceListAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    /**
     *
     */
    private void setDevicesRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        deviceListAdapter = new DeviceListAdapter(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(deviceListAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_BT_REQUEST && resultCode == Activity.RESULT_OK) {
            setBluetoothAdapter();
        } else {
            Toast.makeText(DeviceScanActivity.this, "Bluetooth must be switched on", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}