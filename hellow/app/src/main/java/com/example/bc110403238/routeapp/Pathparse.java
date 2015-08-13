package com.example.bc110403238.routeapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import java.util.Locale;
import android.widget.Toast;

public class Pathparse extends Activity implements OnClickListener, OnInitListener {

    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;

    //create the Activity
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsepath);

        //get a reference to the button element listed in the XML layout
        Button speakButton = (Button)findViewById(R.id.speak);
        //listen for clicks
        speakButton.setOnClickListener(this);

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    //respond to button clicks
    public void onClick(View v) {

        //get the text entered
        EditText enteredText = (EditText)findViewById(R.id.enter);
        String words = enteredText.getText().toString();
        speakWords(words);
    }

    //speak the user text
    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    //setup TTS
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

}





/*
import org.json.JSONArray;
import org.json.JSONObject;

*/
/**
 * Created by bc110403238 on 10-Jun-15.
 *//*

public class Pathparse {

    public void myFunction(Object data){

        try {
            System.out.println("dakeetiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");


            String jsonString = "{ \"name\" : \"John\", \"age\" : \"20\", \"address\" : \"some address\" }";
            JSONObject jsonObject = new JSONObject(jsonString);
            int age = jsonObject.getInt("age");
            System.out.println(age);

            System.out.println("helooooooooooooo");
*/





            /*System.out.println(data);


            JSONObject jObj = new JSONObject((String) data);
            JSONArray jsonArray = jObj.optJSONArray("routes");

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.optString("northeast").toString());
                String name = jsonObject.optString("copyrights").toString();
                String salary = jsonObject.optString("lat").toString();

                System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"  + id +"    o" + name +"   mm" + salary);
            }

            //System.out.println("dakeetiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii" + surname + "00000000");

            // obj.get((String) data);


            /*obj = new JSONObject();
            JSONArray sportsArray = obj.getJSONArray("routes");
            JSONObject firstSport = sportsArray.getJSONObject(0);
            String name = firstSport.getString("bounds"); // basketball
            int id = firstSport.getInt("lat"); // 40
            JSONArray leaguesArray = firstSport.getJSONArray("routes");
            System.out.println("u are great but need       " + name + "    " + id +leaguesArray );
            System.out.println("dakeetiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
*/
           // JSONArray arr = obj.getJSONArray("routes");

            //System.out.println("u are great but need       " + arr);


            //for (String myArr : arr) {
  /*              for (int i = 0; i < arr.length(); i++) {
                String distance_text = arr.getJSONObject(i).getJSONObject("distance").getString("text");
                String duration_text = arr.getJSONObject(i).getJSONObject("duration").getString("text");
                String end_address = arr.getJSONObject(i).getString("end_address");


                System.out.println("dakeetiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii" + distance_text +"    "+ duration_text);
                //}
            }*/
 /*       }catch (Exception e){}


    }


}
*/