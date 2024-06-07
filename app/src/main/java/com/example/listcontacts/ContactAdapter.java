package com.example.listcontacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    ArrayList<String> contactDataset;
    Set<String> checkedContact;
    public ContactAdapter(ArrayList<String> dataset, Set<String> checkedContacts) {
        contactDataset = dataset;
        checkedContact = new HashSet<>();
    }

    // Création de notre vue
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView contact;
        private final CheckBox contactCheckbox;

        public ViewHolder(View v) {
            super(v);
            contact = (TextView) itemView.findViewById(R.id.contact_view);
            contactCheckbox = (CheckBox) itemView.findViewById(R.id.contact_checkbox);
        }

        public TextView getTextView() {
            return contact;
        }
        public CheckBox getCheckbox() {
            return contactCheckbox;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int pos){
        String contact = contactDataset.get(pos);
        viewHolder.getTextView().setText(contact);

        // Initialise notre checkbox
        viewHolder.getCheckbox().setChecked(checkedContact.contains(contact));
        // Si on check un contact, on l'ajoute a la liste des contact cochés
        viewHolder.getCheckbox().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedContact.add(contact);
            } else {
                checkedContact.remove(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactDataset.size();
    }

    public Set<String> getCheckedContact() {
        return checkedContact;
    }

}
