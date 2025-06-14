package com.darkness.sparkwomen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class OpenStreetMapActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_openstreetmap);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        IMapController mapController = map.getController();
        mapController.setZoom(15.0);

        // Example location (Delhi), later you can use dynamic location
        GeoPoint startPoint = new GeoPoint(28.644800, 77.216721);
        mapController.setCenter(startPoint);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); // needed for compass, my location overlays, etc
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  // needed for compass, my location overlays, etc
    }
}
