package com.darkness.sparkwomen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;

import androidx.core.view.ViewCompat;
import android.view.View;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FusedLocationProviderClient fusedLocationClient;
    String myLocation = "", numberCall;
    SmsManager manager = SmsManager.getDefault();
    private MaterialButton panicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        panicBtn = findViewById(R.id.panicBtn);
        panicBtn.setOnClickListener(this);
        setupPanicButton(); // ✅ Now panicBtn is initialized

        findViewById(R.id.fourth).setOnClickListener(this);
        findViewById(R.id.first).setOnClickListener(this);
        findViewById(R.id.second).setOnClickListener(this);
        findViewById(R.id.fifth).setOnClickListener(this);
        findViewById(R.id.sixth).setOnClickListener(this);
        findViewById(R.id.seventh).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fourth) {
            startActivity(new Intent(MainActivity.this, LawsActivity.class));
            MainActivity.this.finish();
        }else if(id == R.id.first){
            startActivity(new Intent(MainActivity.this, ContactActivity.class));
            MainActivity.this.finish();
        }else if(id == R.id.fifth){
            startActivity(new Intent(MainActivity.this, SelfDefenseActivity.class));
        } else if(id == R.id.second){
            startActivity(new Intent(MainActivity.this, SmsActivity.class));
            MainActivity.this.finish();
        } else if (id == R.id.sixth) {
        startActivity(new Intent(MainActivity.this, SafePlacesActivity.class));
        MainActivity.this.finish();
         } else if (id == R.id.seventh) {  // New block added
        startActivity(new Intent(MainActivity.this, MentalHealthActivity.class));
        MainActivity.this.finish();
        } else if (id == R.id.panicBtn)
    {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        myLocation = "http://maps.google.com/maps?q=loc:" + location.getLatitude() + "," + location.getLongitude();
                    } else {
                        myLocation = "Unable to Find Location :(";
                    }
                    sendMsg();
                });

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        numberCall = sharedPreferences.getString("firstNumber", "None");
        if (!numberCall.equalsIgnoreCase("None")) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + numberCall));
            startActivity(intent);
        }
    }




    }
    void sendMsg(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        Set<String> oldNumbers = sharedPreferences.getStringSet("enumbers", new HashSet<>());
        if(!oldNumbers.isEmpty()){
            for(String ENUM : oldNumbers)
                manager.sendTextMessage(ENUM,null,"Im in Trouble!\nSending My Location :\n"+myLocation,null,null);
        }
    }
    // In MainActivity.java
    private void setupPanicButton() {
        panicBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startCountdown();
                return true; // Consumes the long click event
            }
        });
    }

    private void startCountdown() {
        new CountDownTimer(5000, 1000) { // 5-second countdown, 1-second intervals
            @Override
            public void onTick(long millisUntilFinished) {
                panicBtn.setText("CANCEL? " + millisUntilFinished / 1000);
            }
            @Override
            public void onFinish() {
                makeEmergencyCall();

            }
        }.start();
    }

    private void makeEmergencyCall() {
        String emergencyNumber = "112"; // Default or fetch from SharedPreferences
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + emergencyNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
    }
}