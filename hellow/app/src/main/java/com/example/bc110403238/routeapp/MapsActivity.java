package com.example.bc110403238.routeapp;

import android.location.Geocoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bc110403238.routeapp.Controller.SingltonClass;
import com.example.bc110403238.routeapp.Controller.SpeakOut;
import com.example.bc110403238.routeapp.Controller.secondSinglton;
import com.example.bc110403238.routeapp.Modal.MapDictionery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import org.json.*;

public class MapsActivity extends FragmentActivity implements View.OnClickListener
{
    private TextToSpeech myTTS;
    private TextView txtSpeechInput;
    private int MY_DATA_CHECK_CODE = 0;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String start = null;
    String end = null;
    private JSONObject jsonObject = null;
    MapDictionery mapDictionery = null;

    GoogleMap map;
    Location myLocation;
    boolean flag;
    ArrayList<LatLng> markerPoints;
    String TAG = "MAP";
    RadioButton rbDriving;
    RadioButton rbBiCycling;
    RadioButton rbWalking;
    String mode = "";
    String abc ;
    boolean gpsLocChange = false;



    public boolean netConnect()
    {
        ConnectivityManager cm;
        NetworkInfo info = null;
        try
        {
            cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            info = cm.getActiveNetworkInfo();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (info != null)
            return true;
        else
            return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Parse.initialize(this, "DrR1XgBY59LavpydF4P2nb0aXZuTEfqXRi0z47QG", "hEKl3K1kVS83oy3rjTn3JvVKWQYwj2u8Qj5xM7kn");

        // Specify an Activity to handle all pushes by defaul.
        PushService.setDefaultPushCallback(this,MapsActivity.class);



        if (abc == null){
            Toast.makeText(getApplicationContext(), "Please Speak Location!" , Toast.LENGTH_SHORT).show();
            promptSpeechInput();
        }

        rbWalking = (RadioButton) findViewById(R.id.rb_walking);
        rbDriving = (RadioButton) findViewById(R.id.rb_driving);
        rbBiCycling = (RadioButton) findViewById(R.id.rb_bicycling);
        markerPoints = new ArrayList<LatLng>();

        flag = netConnect();

        if (flag == true)
        {
            /*b1 = (Button) findViewById(R.id.camera);
            b1.setOnClickListener(this);*/
            /*b2 = (Button) findViewById(R.id.input);
            b2.setOnClickListener(this);*/
            /*setContentView(R.layout.activity_maps);
            b2 = (Button) findViewById(R.id.speak1);
            b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
            // TODO Auto-generated method stub
                    Intent intent = new Intent(MapsActivity.this,Pathparse.class);
                    startActivity(intent);
            });
            */

            // Getting reference to SupportMapFragment of the activity_main
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting Map for the SupportMapFragment
            map = fm.getMap();

            if (map != null) {

                // Enable MyLocation Button in the Map
                map.setMyLocationEnabled(true);

                // Get LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (locationManager != null) {

                    // Create a criteria object to retrieve provider
                    Criteria criteria = new Criteria();

                    // Get the name of the best provider
                    String provider = locationManager.getBestProvider(criteria, true);


                    // UPDATE LOCATION LISTENER /////////////////////////////////////////////////////////////////////


                    LocationListener locationListener = new LocationListener() {

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Toast.makeText(getApplication(),
                                    "Provider enabled: " + provider, Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Toast.makeText(getApplication(),
                                    "Provider disabled: " + provider, Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onLocationChanged(Location location) {
                            // Do work with new location. Implementation of this method will be covered later.
                            gpsLocChange = true;
/*

                            // Get latitude of the current location
                            double latitude = myLocation.getLatitude();

                            // Get longitude of the current location
                            double longitude = myLocation.getLongitude();

                            // Create a LatLng object for the current location
                            LatLng latLng = new LatLng(latitude, longitude);

                            float string = location.getAccuracy();
                            float string2 = location.getSpeed();
                            long string3 = location.distanceTo()


                            Toast.makeText(getApplication(),
                                    "you are at: " + latLng +"   " + string + "   " + string2  + "  ok", Toast.LENGTH_SHORT)
                                    .show();
*/


                            //double myStr = location.getLatitude();
                            //double myStr2 = location.getLongitude();


                            // Get latitude of the current location






                            double latitudeA = myLocation.getLatitude();

                            // Get longitude of the current location
                            double longitudeA = myLocation.getLongitude();

                            // Create a LatLng object for the current location
                            // LatLng latLng = new LatLng(latitude, longitude);

                            Location locationA = new Location("point A");

                            locationA.setLatitude(latitudeA);
                            locationA.setLongitude(longitudeA);

                            Location locationB = new Location("point B");
                            // Get latitude of the current location
                            //double latitudeB = myLocation.getLatitude();

                            // Get longitude of the current location
                            //double longitudeB = myLocation.getLongitude();


                            locationB.setLatitude(31.5772);
                            locationB.setLongitude(74.3363);

                            float distance = locationA.distanceTo(locationB);
                            System.out.println();

                            // String  myStr2 = location.distanceTo();
                            //String  myStr3 = location.getLatitude() + "    " + location.getLongitude();

                            Toast.makeText(getApplication(), "i am new location "  +"    " +
                                    distance , Toast.LENGTH_SHORT).show();

                        }
                    };

                    long minTime = 0; // Minimum time interval for update in seconds, i.e. 5 seconds.
                    long minDistance = 0; // Minimum distance change for update in meters, i.e. 10 meters.

                    locationManager.requestLocationUpdates(provider, minTime,
                            minDistance, locationListener);

                    // UPDATE LOCATION LISTENER /////////////////////////////////////////////////////////////////////


                    if (!provider.isEmpty()) {
                        Toast.makeText(getApplicationContext(), provider, Toast.LENGTH_SHORT).show();

                        // Get Current Location
                        //Location myLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);


                        myLocation = locationManager.getLastKnownLocation(provider);

                        if(myLocation != null) {

                            addMarkerOnMap(map, myLocation);
                            /*
                            // set map type
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            // Get latitude of the current location
                            double latitude = myLocation.getLatitude();

                            // Get longitude of the current location
                            double longitude = myLocation.getLongitude();

                            // Create a LatLng object for the current location
                            LatLng latLng = new LatLng(latitude, longitude);


                            markerPoints.add(latLng);
                            // Creating MarkerOptions
                            MarkerOptions options = new MarkerOptions();


                            // Setting the position of the marker
                            options.position(latLng);
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            map.addMarker(options);
                            */
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "MyLocation is Null !", Toast.LENGTH_SHORT).show();
                            if (!markerPoints.isEmpty())
                            {
                                markerPoints.clear();
                            }

                            if (map != null)
                            {
                                map.clear();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Location Provider", Toast.LENGTH_SHORT).show();
                        if (!markerPoints.isEmpty())
                        {
                            markerPoints.clear();
                        }

                        if (map != null)
                        {
                            map.clear();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Location Manager Null", Toast.LENGTH_SHORT).show();
                    if (!markerPoints.isEmpty())
                    {
                        markerPoints.clear();
                    }

                    if (map != null)
                    {
                        map.clear();
                    }
                }

                // Setting onclick event listener for the map
                map.setOnMapClickListener(new OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point)
                    {

                        // Already two locations
                        if (markerPoints.size() > 1)
                        {
                            markerPoints.clear();
                            map.clear();
                        }

                        // Adding new item to the ArrayList
                        markerPoints.add(point);

                        // Creating MarkerOptions
                        MarkerOptions options = new MarkerOptions();

                        // Setting the position of the marker
                        options.position(point);


                         // For the start location, the color of marker is GREEN and
                         // for the end location, the color of marker is RED.

                        if (markerPoints.size() == 1)
                        {
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }
                        else if (markerPoints.size() == 2)
                        {
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            //Intent intent = new Intent(MapsActivity.this, facedetect.class);
                            //startActivity(intent);
                        }
                        if (markerPoints.size() == 2)
                        {
                            Intent intent = new Intent(MapsActivity.this, facedetect.class);
                            startActivity(intent);
                        }
                        // Add new marker to the Google Map Android API V2
                        map.addMarker(options);


                        // Checks, whether start and end locations are captured
                        if (markerPoints.size() >= 2)
                        {
                            LatLng origin = markerPoints.get(0);
                            LatLng dest = markerPoints.get(1);

                            // Getting URL to the Google Directions API
                            String url = getDirectionsUrl(origin, dest);

                            if (!url.isEmpty())
                            {
                                DownloadTask downloadTask = new DownloadTask();

                                // Start downloading json data from Google Directions API
                                downloadTask.execute(url);
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "Direction URL is Empty!", Toast.LENGTH_SHORT).show();
                                if (!markerPoints.isEmpty())
                                {
                                    markerPoints.clear();
                                }

                                if (map != null)
                                {
                                    map.clear();
                                }
                            }
                        }
                    }
                });
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Network Unavailable !", Toast.LENGTH_LONG).show();
            if (!markerPoints.isEmpty())
            {
                markerPoints.clear();
            }

            if (map != null)
            {
                map.clear();
            }
        }
    }


    private void addMarkerOnMap(GoogleMap mapp, Location myLocationn)
    {
        // set map type
        mapp.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocationn.getLatitude();

        // Get longitude of the current location
        double longitude = myLocationn.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);


        markerPoints.add(latLng);
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();


        // Setting the position of the marker
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mapp.addMarker(options);
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";


        if (rbDriving.isChecked()) {
            mode = "mode=driving";
        }
        if (rbWalking.isChecked()) {
            mode = "mode=walking";
        }
        if (rbBiCycling.isChecked()) {
            mode = "mode=BiCycling";
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String CompleteUrl = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return CompleteUrl;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            System.out.print("Exception while downloading url" + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            String myStr = null;
            SingltonClass sc = null;
            sc = SingltonClass.getMyObject();
            secondSinglton sc2 = null;
            sc2 = secondSinglton.getMyObject();

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                jsonObject = new JSONObject(data);
                System.out.println(jsonObject);

                String json = jsonObject.optString("status");



                if (json.equals("OK"))
                {
                    JSONArray jsonArray = jsonObject.optJSONArray("routes");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONArray myArr = jsonObject1.optJSONArray("legs");

                        for (int j = 0; j < myArr.length(); j++)
                        {

                            JSONObject jsonObject2 = myArr.getJSONObject(j);
                            JSONArray secondArr = jsonObject2.optJSONArray("steps");

                            sc.duration =jsonObject2.getString("duration");
                            sc.start = jsonObject2.getString("start_address");
                            sc.end = jsonObject2.getString("end_address");
                            sc.distance =jsonObject2.getString("distance");

                            for (int k = 0; k < secondArr.length(); k++)
                            {
                                JSONObject jsonObject3 = secondArr.getJSONObject(k);
                                start = jsonObject3.getString("duration");
                                sc2.duration = jsonObject3.getString("duration");
                                //sc2.duration = jsonObject3.getJSONObject("duration").getString("text").toString();
                                sc2.distance = jsonObject3.getString("distance");
                                //sc2.html =  jsonObject3.getString("html_instructions");
                                String myString = stripHtml(jsonObject3.getString("html_instructions"));
                                //sc.list.add(myStr);
                                sc.myArray.add(myString);

                                System.out.println("i am html " + sc.myArray.get(k));
                                System.out.println(sc.myArray.size());

                            }



                            start = jsonObject2.getString("start_address");
                            end = jsonObject2.getString("end_address");

                            mapDictionery = new MapDictionery();
                            mapDictionery.setStart(start);
                            mapDictionery.setEnd(end);
                            myStr = mapDictionery.getStart();



                            System.out.print("okay " + myStr);
                            System.out.println(sc.myValue);
                            System.out.println();

                            //speakWords(sc.myValue);
                            //Intent intent = new Intent(MapsActivity.this, facedetect.class);
                            //startActivity(intent);
                        }

                        /*
                        mapDictionery = new MapDictionery();
                        mapDictionery.setMy_currentAddrss(curretn_address);
                        String a =mapDictionery.getMy_currentAddrss();
                        Toast.makeText(getApplicationContext(),a, Toast.LENGTH_SHORT).show();
                        */
                    }
                }
            } catch (Exception e)
            {
                Log.d("Background Task", e.toString());
            }


            Log.d("Data", "Returned Data is: " + data);
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        public String stripHtml(String html ) {
            return Html.fromHtml(html).toString();

        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try
            {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
                //routes.toString();
                //System.out.println(routes.get(0).contains("lat"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result)
        {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++)
            {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++)
                {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //===============================================================================================================
    public void onInit(int initStatus)
    {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS)
        {
            //**********
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR)
        {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            //speakWords("sorry text to speech failed");
        }
    }


    public void onClick(View v)
    {


    }


    public void speakWords(String speech)
    {
        System.out.println("ooooooooooooooooooooooooooooooo");
        System.out.println(speech);   //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("oye chachu kam kr lyyyyyyyyyyyyyy");
        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:
            {
                if (resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    abc = result.get(0);
                    System.out.println(abc + "ye rhi string");
                    Toast.makeText(getApplicationContext(),
                            abc,
                            Toast.LENGTH_SHORT).show();

                    if (!abc.isEmpty())
                    {
                        new GeocoderTask().execute(abc);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "'" + abc + "'" + " - Not Found",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "No String Found",
                            Toast.LENGTH_SHORT).show();
                    // speakWords("No string found");
                    abc = "";
                    promptSpeechInput();
                }
                break;
            }
        }
if (abc.equals(""))
{
    finish();
}
        if (abc.equals("next"))
        {
            Intent intent = new Intent(MapsActivity.this, facedetect.class);
            startActivity(intent);
        }
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                //the user has the necessary data - create the TTS
                // myTTS = new TextToSpeech(this, this);
            }
            else
            {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    private void promptSpeechInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        Object bolo = R.string.speech_prompt;
        try
        {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a)
        {
            Toast.makeText(getApplicationContext(),
             getString(R.string.speech_not_supported),
            Toast.LENGTH_SHORT).show();
        }
        System.out.print("");
    }



    // GEOCODER CLASS FOR SEARCHING ADDRESS ///////////////////////////////////////////////////////

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>
    {
        @Override
        protected List<Address> doInBackground(String... locationName)
        {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try
            {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0] + "Lahore" + "Pakistan", 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses)
        {
            if(addresses==null || addresses.size()==0)
            {
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                //speakWords("No location found");
                abc = "";
                promptSpeechInput();
            }

            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++)
            {
                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                LatLng latLng1 = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng1);
                markerOptions.title(addressText);

                markerPoints.add(latLng1);
                map.addMarker(markerOptions);



                // Locate the first location
                if(i==0)
                {
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
                    if (markerPoints.size() >= 2)
                    {

                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        if (!url.isEmpty())
                        {
                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);
                        } else
                        {
                            Toast.makeText(getBaseContext(), "Direction URL is Empty! Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
            }
            Intent intent = new Intent(MapsActivity.this, facedetect.class);
            startActivity(intent);
        }
    }
    // GEOCODER CLASS FOR SEARCHING ADDRESS ///////////////////////////////////////////////////////
}