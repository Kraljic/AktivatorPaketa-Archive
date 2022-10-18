package com.example.aktivatorpaketa.sms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import com.example.aktivatorpaketa.MainActivity;
import com.example.aktivatorpaketa.Option;

public final class SmsHelper {
    public static void send(Option option) {
        SmsManager smsManager = SmsManager.getDefault();
        for (String msg : option.getMessageList()) {
            smsManager.sendTextMessage(option.getNumber(), null, msg, null, null);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
