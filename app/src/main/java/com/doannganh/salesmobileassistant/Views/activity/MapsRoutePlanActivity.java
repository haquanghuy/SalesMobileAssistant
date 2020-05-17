package com.doannganh.salesmobileassistant.Views.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Manager.DirectionFinderService;
import com.doannganh.salesmobileassistant.Presenter.MapRoutePlanPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.model.DirectionFinder;
import com.doannganh.salesmobileassistant.model.MapPlaceItem;
import com.doannganh.salesmobileassistant.util.ConstantUtil;
import com.doannganh.salesmobileassistant.util.PermissionUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsRoutePlanActivity extends AppCompatActivity
        implements OnMapReadyCallback, DirectionFinderService.DirectionFinderListener {

    LocationManager mLocationManager;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    MapPlaceItem mapPlaceItemCus, mapPlaceItemHost;

    ImageButton imbRefresh;
    Button btnDirection;
    TextView txtNoteGPS;
    ProgressDialog progressDialog; // for direction

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    // get bundle
    int position = -1;
    String addressCustomer = "";

    private final long LOCATION_REFRESH_TIME = 5000;
    private final float LOCATION_REFRESH_DISTANCE = 2;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
//            mapPlaceItemHost.setLat(location.getLatitude());
//            mapPlaceItemHost.setLng(location.getLongitude());
//            mapFragment.getMapAsync(MapsRoutePlanActivity.this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_route_plan);

        AnhXa();
        LoadActionBar();
        GetActivity();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        EventClickButtonDirection();
        EventClickRefresh();
        EventClickTextGPS();

        if (!PermissionUtil.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                && !PermissionUtil.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MapsRoutePlanActivity.this);
            builder.setTitle(R.string.maprouteplan_needpermission_title);
            builder.setMessage(R.string.maprouteplan_needpermission_message);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MapsRoutePlanActivity.this, R.string.maprouteplan_nopermission, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PermissionUtil.showDialogPermission(MapsRoutePlanActivity.this, ConstantUtil.REQUEST_CODE_NO_CARE
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                }
            });
            builder.create().show();
            return;
        }

        new AsyncTaskLoadActi().execute();
        //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, mLocationListener);
    }

    private void AnhXa() {
        btnDirection = findViewById(R.id.btnMapsRoutePlanDirection);
        imbRefresh = findViewById(R.id.imbMapRoutePlanRefresh);
        txtNoteGPS = findViewById(R.id.txtShowNoteGPS);
    }

    private void EventClickTextGPS() {
        txtNoteGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil.showDialogGPS(MapsRoutePlanActivity.this);
                CheckGPS();
            }
        });
    }

    private void EventClickRefresh() {
        imbRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtil.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        && PermissionUtil.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    new AsyncTaskLoadActi().execute();
                } else {
                    PermissionUtil.showDialogPermission(MapsRoutePlanActivity.this, ConstantUtil.REQUEST_CODE_111
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                }
            }
        });
    }

    private void EventClickButtonDirection() {
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (PermissionUtil.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            && PermissionUtil.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        String origin = latitude + "," + longitude;

                        if(PermissionUtil.haveNetworkConnection(getApplicationContext())) {
                            new DirectionFinderService(MapsRoutePlanActivity.this
                                    , origin, addressCustomer)
                                    .execute();
                        } else PermissionUtil.showToastNetworkError(getApplicationContext());
                    } else PermissionUtil.showToastNotPermission(getApplicationContext());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void GetActivity() {
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("job_position");
        addressCustomer = bundle.getString("job_address");
    }

    private void LoadActionBar() {
        getSupportActionBar().setTitle(getString(R.string.maprouteplan_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void CheckGPS(){
        if(PermissionUtil.checkGPSEnable(mLocationManager)){
            txtNoteGPS.setVisibility(View.GONE);
        } else {
            txtNoteGPS.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_routeplan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuMapRoutePlanCreate){
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        } else if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ConstantUtil.REQUEST_CODE_111){
            if (grantResults.length > 0){
                // have a permission
                new AsyncTaskLoadActi().execute();
                mapFragment.getMapAsync(MapsRoutePlanActivity.this);
            } else {
                // don't permission
                Toast.makeText(this, R.string.maprouteplan_nopermission, Toast.LENGTH_SHORT).show();
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        CheckGPS();
        mMap = googleMap;

        if(mapPlaceItemCus != null){
            LatLng sydney = new LatLng(mapPlaceItemCus.getLat(), mapPlaceItemCus.getLng());
            destinationMarkers.add(mMap.addMarker(new MarkerOptions().position(sydney).title(mapPlaceItemCus.getAddress())));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        }

        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<DirectionFinder.Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (DirectionFinder.Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


    class AsyncTaskLoadActi extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MapsRoutePlanActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(!PermissionUtil.haveNetworkConnection(getApplicationContext())) {
                return null;
            }
            MapRoutePlanPresenter map = MapRoutePlanPresenter.Instance(getApplicationContext());
            mapPlaceItemCus = map.searchInfoFrom(addressCustomer);
            mapPlaceItemHost = new MapPlaceItem("Me", "Me", mapPlaceItemCus.getLat(), mapPlaceItemCus.getLng());
            return null;
        }

        @Override
        protected void onPostExecute(Void l) {
            progressDialog.dismiss();

            if(mapPlaceItemCus == null)
                PermissionUtil.showToastNetworkError(getApplicationContext());
            mapFragment.getMapAsync(MapsRoutePlanActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
