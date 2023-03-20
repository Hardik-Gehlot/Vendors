package com.hardik.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class Dashboard extends AppCompatActivity {
Button browse,update,signout,add;
EditText item,price;
FloatingActionButton addItem;
TextView shopName,name,email,phone;
FusedLocationProviderClient fusedLocationProviderClient;
DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Vendor");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        browse = findViewById(R.id.browseVendors);
        update = findViewById(R.id.updateLocation);
        signout = findViewById(R.id.signout);
        addItem = findViewById(R.id.addItem);
        shopName = findViewById(R.id.dashboardshopName);
        name = findViewById(R.id.dashboardname);
        email =findViewById(R.id.dashboardemail);
        phone = findViewById(R.id.dashboardphone);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Dashboard.this);
                Dexter.withContext(Dashboard.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Task<Location> t = fusedLocationProviderClient.getLastLocation();
                            t.addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if(location!=null){
                                        double userlat = location.getLatitude();
                                        double userlang = location.getLongitude();
                                        ref.child("vendors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("lat").setValue(userlat);
                                        ref.child("vendors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("lang").setValue(userlang).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Dashboard.this, "Location Updated Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
            }
        });
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Map.class));
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dashboard.this,Register.class));
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getLayoutInflater().inflate(R.layout.add_menu_sheet,null);
                item = v.findViewById(R.id.dialogitem);
                price = v.findViewById(R.id.dialog_price);
                add = v.findViewById(R.id.dialog_addbtn);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Dashboard.this);
                bottomSheetDialog.setContentView(v);
                bottomSheetDialog.show();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ref.child("items").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(item.getText().toString()).setValue(Integer.parseInt(price.getText().toString()))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Dashboard.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                            bottomSheetDialog.hide();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ref.child("vendors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Vendor v = snapshot.getValue(Vendor.class);
                shopName.setText(v.getShopname());
                name.setText(v.getName());
                email.setText(v.getEmail());
                phone.setText(v.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}