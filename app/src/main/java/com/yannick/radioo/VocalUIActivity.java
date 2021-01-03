package com.yannick.radioo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.detectlanguage.DetectLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocalUIActivity extends AppCompatActivity implements RecognitionListener, TextToSpeech.OnInitListener{

    private final int REQUEST_SPEECH_RECOGNIZER = 10000;
    private TextView mTextView;
    //Speaker speaker;

    private TextToSpeech tts;
    private static final String TAG = "SpeechListener";
    private List<Station> mListStations;
    private Station selectedStation;
    private SpeechRecognizer sr;
    private boolean isReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocal_ui);

        /*sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(this);*/


        getSpeechRecognizer();

        /*speaker = new Speaker(this);
        speaker.setText("Donnez le nom d'une station");
        speaker.speak();*/

        tts = new TextToSpeech(this, this );
    }

    private void getStationByName(String name){
        // RadioService radioService = RetrofitManager.getRetrofit().create(RadioService.class);
        RetrofitManager
                .getRetrofit()
                .create(RadioService.class)
                .getByName(name)
                .enqueue(new Callback<List<Station>>() {
                    @Override
                    public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                        if (response.isSuccessful()) {

                            Log.v(" getStationByName ","onResponse: "+response.body());
                            mListStations = new ArrayList<>();
                            mListStations.addAll(response.body());

                            if(mListStations.size()!=0){
                                selectedStation = mListStations.get(0);

                                Log.v(" getStationByName ","selectedStation: "+mListStations.get(0));

                                Intent intent = new Intent(VocalUIActivity.this, VocalUIActivity.class);
                                intent.putExtra("STATION", selectedStation);
                                startActivity(intent);
                            }
                            else{
                                Log.v(" getStationByName ","no radio with this name: ");
                                tts.stop();
                                tts.shutdown();
                                tts = new TextToSpeech(VocalUIActivity.this, VocalUIActivity.this );
                                startListening();
                            }
                        }
                        else {
                            switch (response.code()) {
                                case 404:
                                    Log.v("error","not found");
                                    //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Log.v("error","server side error");
                                    // Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Log.v("error","unknown error");
                                    //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Station>> call, Throwable t) {
                        Log.v("response",t.getMessage());
                    }
                });
    }

    public void startListening(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.example.myapplication");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        sr.startListening(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) { }

    @Override
    public void onBeginningOfSpeech(){Log.d(TAG, "onBeginningOfSpeech");}

    @Override
    public void onRmsChanged(float rmsdB)
    {
        Log.d(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer)
    {
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech()
    {
        Log.d(TAG, "onEndofSpeech");
    }

    @Override
    public void onError(int error) {

//        tts = new TextToSpeech(VocalUIActivity.this, VocalUIActivity.this );
//        startListening();
        Log.d(TAG, "error " + error);
        String message;
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
    }

    @Override
    public void onResults(Bundle results)
    {
        Log.d(TAG, "onResults " + results);
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d(TAG, "onResults 1 " + data.get(0));

        getStationByName(data.get(0).toLowerCase());//.replace(" ","%20")

        Log.v("onResults 2",""+selectedStation);


    }
    @Override
    public void onPartialResults(Bundle partialResults)
    {
        Log.d(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params)
    {
        Log.d(TAG, "onEvent " + eventType);
    }

    public void speak(String text) {

        HashMap<String,String> ttsParams = new HashMap<String, String>();
        ttsParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "truc");

        int speechStatus = tts.speak(text, TextToSpeech.QUEUE_FLUSH, ttsParams);
        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }

    @Override
    public void onInit(int status) {
        setSpeechToTextListener();
        if (status == TextToSpeech.SUCCESS) {
            speak("Donnez le nom d'une station");
            isReady = true;
            int ttsLang = tts.setLanguage(Locale.FRANCE);
            if (ttsLang == TextToSpeech.LANG_MISSING_DATA|| ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language is not supported!");
            } else {
                Log.i("TTS", "Language Supported.");
            }
            Log.i("TTS", "Initialization success.");
        } else {
            isReady = false;
            Log.i("TTS", "TTS Initialization failed!");
        }
    }


    private SpeechRecognizer getSpeechRecognizer() {
        if (sr == null) {
            sr = SpeechRecognizer.createSpeechRecognizer(this);
            sr.setRecognitionListener(this);
        }
        return sr;
    }

    public void setSpeechToTextListener(){
        Log.v("XXX","ok entre setSpeechToTextListener");
        int listenerResult =tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String utteranceId) {

                Log.v("XXX", "TTS complete '");
                tts.stop();

                VocalUIActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        startListening();
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {
                Log.v("XXX", "TTS error");
            }

            @Override
            public void onStart(String utteranceId) {
                Log.v("XXX", "TTS start");
            }
        });

        Log.v("XXX","SpeechToTextListener value: "+listenerResult);

        if(listenerResult!= TextToSpeech.SUCCESS){
            Log.e("XXX", "failed to add utterance progress listener");
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(this);

        /*speaker = new Speaker(this);
        speaker.setText("Donnez le nom d'une station");
        speaker.speak();*/

        tts = new TextToSpeech(this, this );
        startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }


}
