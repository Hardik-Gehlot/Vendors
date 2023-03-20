package com.hardik.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.CallLog;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Register extends AppCompatActivity {

    EditText shopName;
    EditText vendorName;
    EditText category;
    EditText phoneNo;
    EditText email,password;
    Button register;
    TextView gotoLogin;
    private FirebaseAuth mAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Vendor/vendors");
    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        shopName = findViewById(R.id.shopName);
        vendorName = findViewById(R.id.vendorName);
        category = findViewById(R.id.category);
        phoneNo = findViewById(R.id.phoneNo);
        email = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerBtn);
        gotoLogin = findViewById(R.id.gotologin);
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Register.this);
                Dexter.withContext(Register.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                if (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                    return;
                                                }
                                                Task<Location> t = fusedLocationProviderClient.getLastLocation();
                                                t.addOnSuccessListener(new OnSuccessListener<Location>() {
                                                    @Override
                                                    public void onSuccess(Location location) {
                                                        if(location!=null){
                                                            double lat = location.getLatitude();
                                                            double lang = location.getLongitude();
                                                            Vendor ven = new Vendor(shopName.getText().toString(), vendorName.getText().toString(), phoneNo.getText().toString(), category.getText().toString(),email.getText().toString(), password.getText().toString(),lat,lang,mAuth.getCurrentUser().getUid());
                                                            ref.child(mAuth.getCurrentUser().getUid()).setValue(ven).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(Register.this,Dashboard.class));
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }else{
                                                task.getException();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(Register.this,Dashboard.class));
        }
    }
}