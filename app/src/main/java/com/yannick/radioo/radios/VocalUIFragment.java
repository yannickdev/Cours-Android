package com.yannick.radioo.radios;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.yannick.radioo.R;
import com.yannick.radioo.RadioService;
import com.yannick.radioo.RetrofitManager;
import com.yannick.radioo.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class VocalUIFragment extends Fragment  implements RecognitionListener, TextToSpeech.OnInitListener {

    private final int REQUEST_SPEECH_RECOGNIZER = 10000;
    private static final String STATION = "STATION";
    private static final String STATE = "VOCAL";
    //Speaker speaker;

    private TextToSpeech tts;
    private static final String TAG = "SpeechListener";
    private List<Station> mListStations;
    private Station selectedStation;
    private SpeechRecognizer sr;
    private boolean isReady;

    private ViewPager viewPager;

    private Context context;
    private Retrofit retrofit;

    private OnFragmentInteractionListener mListener;

    public VocalUIFragment() {

    }

    public static VocalUIFragment newInstance() {
        return new VocalUIFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = this.getActivity().findViewById(R.id.view_pager);

        retrofit =RetrofitManager.getRetrofit();
        context = this.getContext();

        getSpeechRecognizer();
        tts = new TextToSpeech(VocalUIFragment.this.getContext(), VocalUIFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vocal_ui, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Station station);
    }

    private void getStationByName(String name){
        retrofit.create(RadioService.class)
                .getByName(name)
                .enqueue(new Callback<List<Station>>() {
                    @Override
                    public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                        if (response.isSuccessful()) {
                            mListStations = new ArrayList<>();
                            mListStations.addAll(response.body());

                            if(mListStations.size()!=0){
                                selectedStation = mListStations.get(0);
                                mListener.onFragmentInteraction(selectedStation);

//                                FragmentTransaction transaction=getFragmentManager().beginTransaction();
//                                PlayerFragment mfragment=new PlayerFragment();
//
//                                Bundle bundle=new Bundle();
//                                bundle.putParcelable(STATION,selectedStation);
//
//                                mfragment.setArguments(bundle);
//                                transaction.replace(R.id.fragment_vocal_ui, mfragment);
//                                transaction.commit();
//
//                                viewPager.setCurrentItem(2);
                            }
                            else{
                                Log.v(" getStationByName ","no radio with this name: ");
                                tts.stop();
                                tts.shutdown();
                                tts = new TextToSpeech(context, VocalUIFragment.this );
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
    public void onRmsChanged(float rmsdB){
        Log.d(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer){
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech(){
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
    public void onResults(Bundle results){
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        getStationByName(data.get(0).toLowerCase());//.replace(" ","%20")
    }
    @Override
    public void onPartialResults(Bundle partialResults){
        Log.d(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params){
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
            sr = SpeechRecognizer.createSpeechRecognizer(VocalUIFragment.this.getContext());
            sr.setRecognitionListener(this);
        }
        return sr;
    }

    public void setSpeechToTextListener(){
        int listenerResult =tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String utteranceId) {
                tts.stop();
                getActivity().runOnUiThread(new Runnable() {
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


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        sr = SpeechRecognizer.createSpeechRecognizer(VocalUIFragment.this.getContext());
//        sr.setRecognitionListener(this);
//
//        /*speaker = new Speaker(this);
//        speaker.setText("Donnez le nom d'une station");
//        speaker.speak();*/
//
//        tts = new TextToSpeech(VocalUIFragment.this.getContext(), this );
//        startListening();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
