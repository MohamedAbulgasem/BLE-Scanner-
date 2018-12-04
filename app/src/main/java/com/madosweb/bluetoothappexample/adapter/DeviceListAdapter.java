package com.madosweb.bluetoothappexample.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madosweb.bluetoothappexample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Abulgasem 2018/12/04
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {

    private List<BluetoothDevice> devices;
    private final DeviceListAdapterOnClickHandler itemClickHandler;

    public DeviceListAdapter(DeviceListAdapterOnClickHandler deviceListAdapterOnClickHandler) {
        devices = new ArrayList<>();
        this.itemClickHandler = deviceListAdapterOnClickHandler;
    }

    /**
     *
     */
    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View individualView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item, parent, false);
        return new DeviceListViewHolder(individualView);
    }

    /**
     *
     */
    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, int position) {
        holder.questionTextView.setText(devices.get(position).getName());
    }

    /**
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        if (devices.isEmpty()) return 0;
        return devices.size();
    }

    /**
     * @param device BluetoothDevice
     */
    public void addDevice(BluetoothDevice device) {
        if(device != null) devices.add(device);
    }

    /**
     * @return List<BluetoothDevice>
     */
    public List<BluetoothDevice> getDevices(){
        return devices;
    }

    /**
     *
     */
    public class DeviceListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView questionTextView;

        DeviceListViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.device_tv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickHandler.onItemClick(getAdapterPosition());
        }
    }

    public interface DeviceListAdapterOnClickHandler {
        void onItemClick(int position);
    }
}
