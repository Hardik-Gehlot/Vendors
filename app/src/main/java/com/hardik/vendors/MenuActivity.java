package com.hardik.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity {
String uid;
DatabaseReference ref;
ArrayList<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        items = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Vendor/items");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Item i = new Item();
                    Long l = dataSnapshot.getValue(Long.class);
                    i.setPrice(Integer.parseInt(String.valueOf(l)));
                    i.setName(dataSnapshot.getKey());
                    items.add(i);
                }
                customListViewAdapter numbersArrayAdapter = new customListViewAdapter(MenuActivity.this, items);

                // create the instance of the ListView to set the numbersViewAdapter
                ListView numbersListView = findViewById(R.id.listView);

                // set the numbersViewAdapter for ListView
                numbersListView.setAdapter(numbersArrayAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}