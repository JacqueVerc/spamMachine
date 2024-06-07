package com.example.listcontacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Objects;

public class AutoReplyReceiver extends BroadcastReceiver {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String AUTO_REPLY_KEY = "autoReply";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean autoReply = sharedPreferences.getBoolean(AUTO_REPLY_KEY, false);

        // Si le switch autoReply est true et un sms est recu, on répond
        if (autoReply && Objects.equals(intent.getAction(), "android.provider.Telephony.SMS_RECEIVED")) {
            // Récupère les infos du message entrant et lui répond
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        String messageBody = smsMessage.getMessageBody();
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(sender, null, "Auto-reply: " + messageBody, null, null);
                        Toast.makeText(context, "Auto-replied to: " + sender, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
