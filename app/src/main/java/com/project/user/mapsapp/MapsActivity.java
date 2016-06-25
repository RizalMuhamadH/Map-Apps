package com.project.user.mapsapp;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, TextWatcher {

    private GoogleMap mMap;

    private EditText location_tf;
    private Button btn_search;
    ListView list_view;
    FloatingActionButton btn_type_map,btn_zoomIn,btn_zoomOut;

    List<Address>addressList;

    Geocoder geocoder;

    private ListAdapter adapter;

    private String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        location_tf = (EditText)findViewById(R.id.editText);
        list_view = (ListView)findViewById(R.id.listView);
        btn_search = (Button)findViewById(R.id.button);
        btn_type_map =(FloatingActionButton)findViewById(R.id.action_a);
        btn_zoomIn =(FloatingActionButton) findViewById(R.id.action_b);
        btn_zoomOut =(FloatingActionButton) findViewById(R.id.action_c);
        btn_type_map.setOnClickListener(this);
        btn_zoomIn.setOnClickListener(this);
        btn_zoomOut.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        location_tf.addTextChangedListener(this);

        location_tf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                list_view.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }
    public void AddressAutocomplete(String addressName){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        String [] form = {"feature","address"};

        List<String> locationNameList;
        list_view = (ListView)findViewById(R.id.listView);
        locationNameList = new ArrayList<String>();

        adapter = new SimpleAdapter(MapsActivity.this,list,R.layout.find_location,form, new int[]{R.id.txtFeature,R.id.txtAddress});

        list_view.setAdapter(adapter);

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            List<Address> addresslist = geocoder.getFromLocationName(addressName,5);
            if ((addresslist==null)||(addresslist.isEmpty())){
                list_view.setVisibility(View.GONE);
                Toast.makeText(MapsActivity.this, "Location Unknown", Toast.LENGTH_SHORT).show();
            }else{
                Address locatAddress = addresslist.get(0);
                if (locatAddress !=null){
                    list_view.setVisibility(View.GONE);
                    Toast.makeText(MapsActivity.this, "Found : " + addresslist.size(), Toast.LENGTH_SHORT).show();
                    locationNameList.clear();
                    for (Address i :addresslist){
                        HashMap<String,String> item = new HashMap<>();
                        if (i.getFeatureName()==null){
                            item.put("feature","Not Found");
                            item.put("address","Not Found");
                        }else {
                            item.put("feature",i.getFeatureName());
                            item.put("address",i.getAddressLine(0));
                        }
                        list.add(item);
                    }

                    list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            list_view.setVisibility(View.GONE);
                            HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
                            String result = (String)obj.get("feature");
                            location_tf.setText(result);
                        }
                    });
                }
            }
        } catch (IOException e){

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button :
                Address address;
                String location = location_tf.getText().toString();
                geocoder = new Geocoder(MapsActivity.this);
                try {
                    Log.e("LOCATION: ",location );
                    mMap.clear();
                    addressList=geocoder.getFromLocationName(location,1);
                    if (location !=null || !location.equals("")){
                        address=addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(location)
                                .snippet(String.valueOf(address.getCountryName()+" "+address.getAddressLine(0)+" "+address.getLatitude()+","+String.valueOf(address.getLongitude()))));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }else{
                        Toast.makeText(MapsActivity.this, "Location Unknown", Toast.LENGTH_SHORT).show();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case R.id.action_a :
                change();
                break;
            case R.id.action_b :
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case  R.id.action_c :
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }

    }
    public void change() {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            btn_type_map.setTitle("NORMAL");
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            btn_type_map.setTitle("SATELLITE");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        loc = location_tf.getText().toString();
        list_view = (ListView)findViewById(R.id.listView);
        if ((loc==null)||(loc=="")){
            list_view.setVisibility(View.GONE);
        }else{
            list_view.setVisibility(View.GONE);
            AddressAutocomplete(loc);
        }
    }
}
