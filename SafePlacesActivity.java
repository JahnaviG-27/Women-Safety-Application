package com.darkness.sparkwomen;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SafePlacesActivity extends AppCompatActivity {

    Button btnFindPolice, btnFindHospital, btnFindHelpline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_places);

        btnFindPolice = findViewById(R.id.btnFindPolice);
        btnFindHospital = findViewById(R.id.btnFindHospital);
        btnFindHelpline = findViewById(R.id.btnFindHelpline);

        btnFindPolice.setOnClickListener(v -> searchNearby("police station"));
        btnFindHospital.setOnClickListener(v -> searchNearby("hospital"));
        btnFindHelpline.setOnClickListener(v -> openHelplinePage());
    }

    private void searchNearby(String placeType) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(placeType));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void openHelplinePage() {
        // You can set any official helpline website link here
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ncw.nic.in/helplines"));
        startActivity(browserIntent);
    }
}
