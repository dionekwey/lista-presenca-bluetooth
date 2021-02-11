package com.example.listapresencabluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int SOLICITA_ATIVACAO_BT = 1;

    private BluetoothAdapter mBluetoothAdapter = null;
    private ListView mListaDispositivos = null;
    private BroadcastReceiver mReceiver = null;
    private ArrayAdapter<String> mLista = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListaDispositivos = findViewById(R.id.lista_dispositivos);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mLista = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListaDispositivos.setAdapter(mLista);

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();

                    mLista.add(deviceName + " [" + deviceAddress + "]");
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        /*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showMessage("ACCESS_COARSE_LOCATION não permitido");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            showMessage("ACCESS_FINE_LOCATION não permitido");
        }
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SOLICITA_ATIVACAO_BT && resultCode == Activity.RESULT_OK) {
            listarDispositivos();
        } else if (requestCode == SOLICITA_ATIVACAO_BT && resultCode != Activity.RESULT_OK) {
            showMessage(this.getString(R.string.mg_erro_bluetooth));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        //unregisterReceiver(mReceiver);
    }

    public void atualizar(View view) {
        mLista.clear();

        if (mBluetoothAdapter == null) {
            showMessage(this.getString(R.string.mg_erro_bluetooth));
        } else if (mBluetoothAdapter.isEnabled()) {
            listarDispositivos();
        } else {
            Intent ativa_bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativa_bluetooth, SOLICITA_ATIVACAO_BT);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void listarDispositivos() {
        mBluetoothAdapter.startDiscovery();

        if (mBluetoothAdapter.isDiscovering()) {
            showMessage("Buscando...");
        }

        /*
        Set<BluetoothDevice> dispositivosPareados = mBluetoothAdapter.getBondedDevices();

        if (dispositivosPareados.size() > 0) {
            for (BluetoothDevice dispositivo : dispositivosPareados) {
                String deviceName = dispositivo.getName();
                String deviceAddress = dispositivo.getAddress();

                mLista.add(deviceName + " [" + deviceAddress + "]");
            }
        }
         */
    }
}