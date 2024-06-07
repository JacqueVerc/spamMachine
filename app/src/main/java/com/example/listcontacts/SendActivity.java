package com.example.listcontacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SendActivity extends AppCompatActivity {

    private Switch autoReplySwitch;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AppPrefs";
    private static final String AUTO_REPLY_KEY = "autoReply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Création de notre vue avec le switch de autoReply
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        autoReplySwitch = findViewById(R.id.switch_auto_reply);
        Button sendMessageButton = findViewById(R.id.button_send_message);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        autoReplySwitch.setChecked(sharedPreferences.getBoolean(AUTO_REPLY_KEY, false));
        // Gère le changement d'état du switch, enregistre son état
        autoReplySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(AUTO_REPLY_KEY, isChecked);
            editor.apply();
        });

        // Gère le bouton de spam + vérifie les droits d'acces a la telephonie
        sendMessageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                if (!ActivityCompat.shouldShowRequestPermissionRationale(SendActivity.this,
                        Manifest.permission.SEND_SMS)) {
                    ActivityCompat.requestPermissions(SendActivity.this, new String[]{Manifest.permission.SEND_SMS}, 0);
                } else {
                    sendMessagesToSelectedContacts();
                }
                // Message de retour a l'action de l'utilisateur
                Toast.makeText(SendActivity.this, "Message envoyé aux contacts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessagesToSelectedContacts() {
        // Envoi un message au contacts coché dans la page de chois des spams (inexistante)
        // Data de test pour le cas
        String[] selectedContacts = {"1234567890", "0987654321"};
        String message = "Hello, this is a test message";

        SmsManager smsManager = SmsManager.getDefault();
        for (String phoneNumber : selectedContacts) {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
        Toast.makeText(SendActivity.this, "Messages sent!", Toast.LENGTH_SHORT).show();
    }
}
