package com.example.listcontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ListActivity extends AppCompatActivity {
    ArrayList<String> contacts = new ArrayList<String>();
    ContactAdapter adapter;
    Set<String> checkedContact;

    public boolean isPermissionGranted() {
        // Return true if user has given his permission to read incoming messages
        return ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestContactPermission() {
        // Ask user for his permission to read incoming messages
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        RecyclerView rc_view = findViewById(R.id.recycler);
        rc_view.setLayoutManager(new LinearLayoutManager(this));

        contacts.add("John Doe");
        contacts.add("John Doel");
        contacts.add("John Dodo");

        checkedContact = loadCheckedContacts();
        adapter = new ContactAdapter(contacts, checkedContact);
        rc_view.setAdapter(adapter);

        if (!isPermissionGranted()) {
            requestContactPermission();
        }

        // Requête
        ContentResolver resolver = getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Provider natif Android pour les informations relatives aux contacts
        Cursor cursor = resolver.query(uri, null, null, null);
        // On va maintenant parcourir la base de données tout en récupérant le nom des contacts
        // et en l'ajoutant à notre RecyclerView
        while (cursor.moveToNext()) {
            Integer column_index = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contacts.add(cursor.getString(column_index));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCheckedContacts(adapter.getCheckedContact());
    }

    private void saveCheckedContacts(Set<String> checkedContacts) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("checked_contacts", checkedContacts);
        editor.apply();
    }

    private Set<String> loadCheckedContacts() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getStringSet("checked_contacts", new HashSet<>());
    }
}