package com.library.smsconsumer.model;

import org.json.JSONObject;

public class SmsMessage {
    private String number;
    private String message;

    public SmsMessage(String number, String message) {
        this.number = number;
        this.message = message;
    }

    public static SmsMessage fromJson(String jsonString) throws Exception {
        JSONObject json = new JSONObject(jsonString);
        String number = json.getString("number");
        String message = json.getString("message");
        return new SmsMessage(number, message);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SmsMessage{" +
                "number='" + number + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
