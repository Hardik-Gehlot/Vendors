package com.hardik.vendors;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    Button menu;
    ImageButton direction;
    TextView shopname,name,phone;
    ArrayList<Vendor> vendors;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Vendor/vendors");
    FusedLocationProviderClient fusedLocationProviderClient;
    double userlat,userlang;

    private FirebaseAuth mAuthReq;
    DatabaseReference refRequests = FirebaseDatabase.getInstance().getReference("Vendor/requests");
    HashMap<String,Double> latlng = new HashMap<String, Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        vendors = new ArrayList<>();
        mAuthReq = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refRequests.push().setValue(latlng).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Map.this, "Request has been displayed to the vendors", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(Register.this,Dashboard.class));
                    }
                });
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getCurrentLocation(googleMap);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return true;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getCurrentLocation(GoogleMap googleMap) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Map.this);
        Dexter.withContext(Map.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    if (ActivityCompat.checkSelfPermission(Map.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Task<Location> t = fusedLocationProviderClient.getLastLocation();
                    t.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location!=null){
                                userlat = location.getLatitude();
                                userlang = location.getLongitude();
                                latlng.put("lat",userlat);
                                latlng.put("lang",userlang);
                                showOnMap(googleMap);
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


    private void showOnMap(GoogleMap googleMap){
        mMap = googleMap;
        ref.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Vendor v = dataSnapshot.getValue(Vendor.class);
                    assert v != null;
                    vendors.add(v);
                }
                LatLng latLng = new LatLng(userlat,userlang);
                mMap.addMarker(new MarkerOptions().position(latLng).title("you are here").snippet("you").icon(bitmapDescriptorFromVector(Map.this, R.drawable.ic_baseline_person_pin_circle_24)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                for(int i=0;i<vendors.size();i++){
                    Log.d("Mytag",""+i+" "+vendors.get(i));
                    LatLng ll = new LatLng(vendors.get(i).getLat(),vendors.get(i).getLang());
                    mMap.addMarker(new MarkerOptions().position(ll).title(vendors.get(i).getShopname()).snippet(i+""));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog,null);
                menu = v.findViewById(R.id.dialog_menu);
                direction = v.findViewById(R.id.dialog_direction);
                shopname = v.findViewById(R.id.dialog_shop_name);
                name= v.findViewById(R.id.dialog_name);
                phone = v.findViewById(R.id.dialog_phone);
                Vendor vendor = vendors.get(Integer.parseInt(marker.getSnippet()));
                shopname.setText(vendor.getShopname());
                name.setText(vendor.getName());
                phone.setText(vendor.getPhone());
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uid = vendor.getUid();
                        Intent i = new Intent(Map.this,MenuActivity.class);
                        i.putExtra("uid",uid);
                        startActivity(i);
                    }
                });
                direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: Directions to be shown
                    }
                });
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Map.this);
                bottomSheetDialog.setContentView(v);
                bottomSheetDialog.show();
                return true;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(25,75)));
    }
}