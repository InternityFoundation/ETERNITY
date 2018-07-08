package in.internity.crowdtracker.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.marcoscg.easypermissions.EasyPermissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.internity.crowdtracker.BuildConfig;
import in.internity.crowdtracker.Constants.Constants;
import in.internity.crowdtracker.DataModels.StationLngLatDataModal;
import in.internity.crowdtracker.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 68;
    @BindView(R.id.spinner_palces)
    Spinner spinnerPalces;
    @BindView(R.id.book_selected_btn)
    Button bookSelectedBtn;
    @BindView(R.id.selected_place_ll)
    LinearLayout selectedPlaceLl;
    @BindView(R.id.current_place_book_btn)
    Button currentPlaceBookBtn;
    @BindView(R.id.current_place_ll)
    LinearLayout currentPlaceLl;
    @BindView(R.id.longitude)
    EditText longitude;
    @BindView(R.id.latitude)
    EditText latitude;
    @BindView(R.id.costom_place_book_btn)
    Button costomPlaceBookBtn;
    @BindView(R.id.custom_place_ll)
    LinearLayout customPlaceLl;
    StationLngLatDataModal stationLngLatDataModal;
    private String[] List = {"Noida Sec 62","Abc","Alipur"};

    private ArrayList<StationLngLatDataModal> stationSpinnerList;

//    private RequestQueue requestQueue;
//    String url = "https://json_url/";

    String[] permissions = {EasyPermissions.ACCESS_FINE_LOCATION};
    int requestCode = 1344;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        stationSpinnerList = new ArrayList<>();
        configureSpinnerlist(stationSpinnerList);
        final ArrayAdapter stationSpinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, List);
        stationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        spinnerPalces.setAdapter(stationSpinnerAdapter);
        spinnerPalces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stationLngLatDataModal = stationSpinnerList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }


//        if (EasyPermissions.hasPermission(this, String.valueOf(permissions))) {
//
//        } else {
//            EasyPermissions.requestPermissions(this, permissions, requestCode);
//        }





    }

    private void configureSpinnerlist(ArrayList<StationLngLatDataModal> stationSpinnerList) {
        stationSpinnerList.add(new StationLngLatDataModal(77.3639,28.6208,"Noida Sec 62"));

        stationSpinnerList.add(new StationLngLatDataModal(77.371814,28.630415,"ABC"));
        stationSpinnerList.add(new StationLngLatDataModal(77.1331,28.7972,"Alipur"));

    }

    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            Toast.makeText(MainActivity.this,"Latitute :- "+mLastLocation.getLatitude()+"Longitute :- "+mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();
//
//                            String cityName = null;
//                            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//                            List<Address> addresses;
//                            try {
//                                addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
//                                        mLastLocation.getLongitude(), 1);
//                                Address address = addresses.get(0);
//                                ArrayList<String> addressFragments = new ArrayList<String>();
//
//                                latitude = mLastLocation.getLatitude();
//                                longitude = mLastLocation.getLongitude();
//
//                                // Fetch the address lines using getAddressLine,
//                                // join them, and send them to the thread.
//                                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                                    addressFragments.add(address.getAddressLine(i));
//                                }
//                                if (addresses.size() > 0) {
//                                    Log.d("TAG", "onComplete: " + addresses.get(0).toString());
//                                    System.out.println(addresses.get(0).getLocality());
//                                    city = addresses.get(0).getLocality();
//                                    state = addresses.get(0).getAdminArea();
//                                    country = addresses.get(0).getCountryName();
//                                    completeAddress = addresses.get(0).getAddressLine(0);
//                                    cityAndCountry = city + ", " + country;
//
//                                    LocationEv.setText(completeAddress);
//
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                                    + cityName;
//                            editLocation.setText(s);


//                            mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
//                                    mLatitudeLabel,
//                                    mLastLocation.getLatitude()));
//                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
//                                    mLongitudeLabel,
//                                    mLastLocation.getLongitude()));

                        } else {
                            Log.w("TAG", "getLastLocation:exception", task.getException());
//                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
//    private void showSnackbar(final String text) {
//        View container = findViewById(R.id.main_activity_container);
//        if (container != null) {
//            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
//        }
//    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("TAG", "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i("TAG", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("TAG", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("TAG", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    @OnClick({R.id.book_selected_btn, R.id.current_place_book_btn, R.id.costom_place_book_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.book_selected_btn:
                //spinner
                if(stationLngLatDataModal!=null)
                {
                    OpenStationShowingActivity(stationLngLatDataModal.getLatitude(),stationLngLatDataModal.getLongitude());
                }
                else
                {
                    Toast.makeText(this,"Please Select Location",Toast.LENGTH_LONG).show();
                }
                // intent.putExtra(Constants.Longitude,);
                break;
            case R.id.current_place_book_btn:

                if(mLastLocation!=null){
                    OpenStationShowingActivity(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                }else{
                    Toast.makeText(this,"Location Not Available Yet",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.costom_place_book_btn:
                if (checkValidate()) {
                    String Lng = longitude.getText().toString().trim();
                    String Lat = latitude.getText().toString().trim();
                    OpenStationShowingActivity(Double.parseDouble(Lat),Double.parseDouble(Lng));
                }
                break;
        }
    }

    private boolean checkValidate() {

        if (TextUtils.isEmpty(longitude.getText())) {
            longitude.setError("Please Add  valid entry");
            return false;
        } else {
            longitude.setError(null);
        }

        if (TextUtils.isEmpty(latitude.getText())) {
            latitude.setError("Please Add  valid entry");
            return false;
        } else {
            latitude.setError(null);
        }


        return true;
    }


    private void OpenStationShowingActivity(double lat,double longi) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent i = new Intent(this,StationShowingActivity.class);
        i.putExtra(Constants.Longitude,longi);
        i.putExtra(Constants.Latitude,lat);
        startActivity(i);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        int param_check = 0;
//        for (int i = 0; i < permissions.length; i++) {
//            String permission = permissions[i];
//            int grantResult = grantResults[i];
//
//            if (permission.equals(EasyPermissions.ACCESS_FINE_LOCATION)) {
//                if (grantResult == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(MainActivity.this, "ACCESS_FINE_LOCATION Granted", Toast.LENGTH_LONG).show();
//                }
//            }
//
//
//        }
//
//    }
}
