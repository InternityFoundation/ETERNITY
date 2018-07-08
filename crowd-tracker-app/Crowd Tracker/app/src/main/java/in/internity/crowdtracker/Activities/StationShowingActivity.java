package in.internity.crowdtracker.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.internity.crowdtracker.Adapters.StationAdapter;
import in.internity.crowdtracker.Constants.Constants;
import in.internity.crowdtracker.DataModels.StationDataModal;
import in.internity.crowdtracker.Helper.MyProgressDialog;
import in.internity.crowdtracker.R;

public class StationShowingActivity extends AppCompatActivity implements StationAdapter.StationClickListerner {

    private static final String URLTOCALL = "http://13.126.33.180/api/beststations";
    private static final String URLTOCALL_FOR_BOOK = "http://13.126.33.180/api/book";
   // "http://62cbebec.ngrok.io/api/book";
    @BindView(R.id.stationRv)
    RecyclerView stationRv;
    private MyProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    StationAdapter adapter;
    ArrayList<StationDataModal> stationDataModals;

    Double lat,longi;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_showing);
        ButterKnife.bind(this);
        stationDataModals = new ArrayList<>();
        adapter = new StationAdapter(stationDataModals,this);
        lat = getIntent().getDoubleExtra(Constants.Latitude,0);
        longi = getIntent().getDoubleExtra(Constants.Longitude , 0);
        linearLayoutManager = new LinearLayoutManager(this);
        stationRv.setAdapter(adapter);
        adapter.setStationClickListerner(this);
        stationRv.setLayoutManager(linearLayoutManager);




       //new AsyncLogin().execute();



        requestQueue = Volley.newRequestQueue(this);


        /*Post data*/
   Map<String, Object> jsonParams = new HashMap<String, Object>();

        jsonParams.put(Constants.Latitude, lat);
        jsonParams.put(Constants.Longitude, longi);
        jsonParams.put(Constants.TIME, System.currentTimeMillis());

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST,URLTOCALL,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray(Constants.DATA);

                            for(int i =0;i<dataArray.length();i++){
                                Log.d("Tag",dataArray.length()+"");
                                StationDataModal dataModal = new StationDataModal();

                                JSONObject data = dataArray.getJSONObject(i);
                                dataModal.setPopLevel(data.getInt(Constants.POPULATION_LEVEL));
                                dataModal.setStationCode(data.getString(Constants.STATION_CODE));
                                dataModal.setStationName(data.getString(Constants.STATION_NAME));
                                dataModal.setTime(data.getString(Constants.ESTIMATED_TIME_OF_ARRIVAL));
                                dataModal.setStationId(data.getString(Constants.Book_ID));
                                Log.d("Tag",dataModal.getStationCode()+"");
                                stationDataModals.add(dataModal);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Toast.makeText(StationShowingActivity.this,error.getMessage()+" ",Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        requestQueue.add(postRequest);






    }

    @Override
    public void stationItemClicked(String stationCode,String StationName) {

        fetchbooking(lat,longi,stationCode,StationName);
        //intent.putExtra()

    }

    private void fetchbooking(Double lat, Double longi, String station, final String stationName) {
        Map<String, Object> jsonParams = new HashMap<String, Object>();

        jsonParams.put(Constants.Latitude, lat);
        jsonParams.put(Constants.Longitude, longi);
        jsonParams.put(Constants.STATION_CODE,station );

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST,URLTOCALL_FOR_BOOK,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(Constants.DATA);
                                Log.d("Tag",jsonObject.length()+"");

//                            "data": {
//                                "location": [
//                                77.32868,
//                                        28.571007
//        ],
//                                "name": "Ramu",
//                                        "capacity": 1,
//                                        "destination": "NSET",
//                                        "_id": "5b4162fdbcc31d3fd7b50cb1",
//                                        "rickshawNumber": "UP 16 A 2102"
//                            }

                           String Driver_Name = jsonObject.getString(Constants.DRIVER_NAME);
                           int Capacity = jsonObject.getInt(Constants.CAPACITY);
                           String rikshawNum = jsonObject.getString(Constants.RICSHAW_NUM);
                           String book_id = jsonObject.getString(Constants.Book_ID);
                            openBook(Driver_Name,Capacity,rikshawNum,book_id,stationName);
                                //Log.d("Tag",dataModal.getStationCode()+"");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Toast.makeText(StationShowingActivity.this,"No  ",Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        requestQueue.add(postRequest);
    }

    private void openBook(String driver_name, int capacity, String rikshawNum, String book_id, String stationName) {
        Intent intent = new Intent(StationShowingActivity.this,BookActivity.class);
        intent.putExtra(Constants.Book_ID,book_id);
        intent.putExtra(Constants.RICSHAW_NUM,rikshawNum);
        intent.putExtra(Constants.STATION_NAME_FOR_INTENT,stationName);
        intent.putExtra(Constants.CAPACITY,capacity);
        intent.putExtra(Constants.DRIVER_NAME,driver_name);
        startActivity(intent);
    }


//    private class AsyncLogin extends AsyncTask<String, String, String> {
//        //  ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
//        HttpURLConnection conn;
//        URL url = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            showProgress(" ");
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                // Enter URL address where your json file resides
//                // Even you can make call to php file which returns json data
//                url = new URL(basepriceUrl + marketPlace + helperUrl);
//
//            } catch (MalformedURLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                return e.toString();
//            }
//            try {
//
//                // Setup HttpURLConnection class to send and receive data from php and mysql
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(READ_TIMEOUT);
//                conn.setConnectTimeout(CONNECTION_TIMEOUT);
//                conn.setRequestMethod("GET");
//
//                // setDoOutput to true as we recieve data from json file
//                conn.setDoOutput(true);
//
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//                return e1.toString();
//            }
//
//            try {
//
//                int response_code = conn.getResponseCode();
//
//                // Check if successful connection made
//                if (response_code == HttpURLConnection.HTTP_OK) {
//
//                    // Read data sent from server
//                    InputStream input = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                    StringBuilder result = new StringBuilder();
//                    String line;
//
//                    while ((line = reader.readLine()) != null) {
//                        result.append(line);
//                    }
//
//                    // Pass data to onPostExecute method
//                    return (result.toString());
//
//                } else {
//
//                    return ("unsuccessful");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return e.toString();
//            } finally {
//                conn.disconnect();
//            }
//
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // pdLoading.dismiss();
//            List<StationDataModal> data = new ArrayList<>();
//
//            if (result != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(result);
//
//                    double usd = jsonObj.getDouble("USD");
//                    double btc = jsonObj.getDouble("BTC");
//                    double eth = jsonObj.getDouble("ETH");
//
//
//
//                } catch (JSONException e) {
//                    Toast.makeText(StationShowingActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }
//    }
}




