package com.library.smsconsumer.util;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsHelper {
    private static final String TAG = "SmsHelper";

    public static void sendSms(Context context, String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.d(TAG, "SMS sent successfully to: " + phoneNumber);
        } catch (Exception e) {
            Log.e(TAG, "Failed to send SMS", e);
        }
    }
}
