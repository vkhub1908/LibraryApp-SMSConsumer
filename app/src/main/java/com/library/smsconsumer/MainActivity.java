package com.library.smsconsumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.library.smsconsumer.service.KafkaConsumerService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Button startServiceBtn;
    private Button stopServiceBtn;
    private TextView statusText;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        checkAndRequestPermissions();
        setupListeners();
    }

    private void initializeViews() {
        startServiceBtn = findViewById(R.id.btn_start_service);
        stopServiceBtn = findViewById(R.id.btn_stop_service);
        statusText = findViewById(R.id.tv_status);
        serviceIntent = new Intent(MainActivity.this, KafkaConsumerService.class);
    }

    private void setupListeners() {
        startServiceBtn.setOnClickListener(v -> startConsumerService());
        stopServiceBtn.setOnClickListener(v -> stopConsumerService());
    }

    private void startConsumerService() {
        startService(serviceIntent);
        statusText.setText(R.string.service_listening);
        Toast.makeText(this, R.string.service_started, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Service started");
    }

    private void stopConsumerService() {
        stopService(serviceIntent);
        statusText.setText(R.string.service_stopped_text);
        Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Service stopped");
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.INTERNET
                        },
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permissions granted");
            } else {
                Toast.makeText(this, R.string.permissions_required, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
