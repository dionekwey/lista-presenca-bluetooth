package com.example.listapresencabluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int SOLICITA_ATIVACAO_BT = 1;

    private BluetoothAdapter bluetooth_adapter = null;
    private ListView lista_dispositivos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
        lista_dispositivos = findViewById(R.id.lista_dispositivos);
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

    public void atualizar(View view) {
        limparLista();

        if (bluetooth_adapter == null) {
            showMessage(this.getString(R.string.mg_erro_bluetooth));
        } else if (!bluetooth_adapter.isEnabled()) {
            Intent ativa_bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativa_bluetooth, SOLICITA_ATIVACAO_BT);
        } else {
            listarDispositivos();
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void limparLista() {
        lista_dispositivos.setAdapter(null);
    }

    private void listarDispositivos() {
        ArrayAdapter<String> lista = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        Set<BluetoothDevice> dispositivos_disponiveis = bluetooth_adapter.getBondedDevices();

        if (dispositivos_disponiveis.size() > 0) {
            for (BluetoothDevice dispositivo : dispositivos_disponiveis) {
                String nome = dispositivo.getName();
                String address = dispositivo.getAddress();

                lista.add(nome + " [" + address + "]");
            }

            lista_dispositivos.setAdapter(lista);
        }
    }
}