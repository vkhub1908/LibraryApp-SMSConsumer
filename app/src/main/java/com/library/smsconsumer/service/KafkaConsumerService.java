package com.library.smsconsumer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.library.smsconsumer.config.KafkaConfig;
import com.library.smsconsumer.model.SmsMessage;
import com.library.smsconsumer.util.SmsHelper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerService extends Service {
    private static final String TAG = "KafkaConsumerService";
    private KafkaConsumer<String, String> consumer;
    private Thread consumerThread;
    private volatile boolean isRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        startConsumingMessages();
        return START_STICKY;
    }

    private void startConsumingMessages() {
        if (isRunning) return;
        
        consumerThread = new Thread(() -> {
            try {
                Properties props = KafkaConfig.getConsumerProperties();
                consumer = new KafkaConsumer<>(props);
                consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC_NAME));
                isRunning = true;
                
                Log.d(TAG, "Connected to Confluent Cloud Kafka");

                while (isRunning) {
                    var records = consumer.poll(java.time.Duration.ofSeconds(1));
                    for (ConsumerRecord<String, String> record : records) {
                        Log.d(TAG, "Received message: " + record.value());
                        processSmsMessage(record.value());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in Kafka consumer", e);
            } finally {
                if (consumer != null) {
                    consumer.close();
                }
                isRunning = false;
            }
        });

        consumerThread.start();
    }

    private void processSmsMessage(String messageJson) {
        try {
            SmsMessage smsMessage = SmsMessage.fromJson(messageJson);
            Log.d(TAG, "Sending SMS to: " + smsMessage.getNumber());
            SmsHelper.sendSms(this, smsMessage.getNumber(), smsMessage.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Error processing message", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (consumerThread != null) {
            try {
                consumerThread.join(5000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error stopping consumer thread", e);
            }
        }
        Log.d(TAG, "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
