package com.example.listapresencabluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetooth_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void atualizar(View view) {
        if (bluetooth_adapter == null) {
            Toast.makeText(this, this.getString(R.string.mg_erro_bluetooth), Toast.LENGTH_SHORT).show();
        } else if (!bluetooth_adapter.isEnabled()) {
            Intent ativa_bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativa_bluetooth, 1);
        } else {
            //TODO
        }
    }
}