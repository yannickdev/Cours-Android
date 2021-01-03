package com.yannick.radioo;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.yannick.radioo.radios.FavouriteFragment;
import com.yannick.radioo.radios.PlayerFragment;
import com.yannick.radioo.radios.PodcastFragment;
import com.yannick.radioo.radios.RadioFragment;
import com.yannick.radioo.radios.SearchFragment;
import com.yannick.radioo.radios.VocalUIFragment;
import com.yannick.radioo.ui.main.SectionsPagerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainTestActivity extends AppCompatActivity implements RadioFragment.OnListFragmentInteractionListener,
                                                                   PlayerFragment.OnFragmentInteractionListener,
                                                                   SearchFragment.OnFragmentInteractionListener,
                                                                   PodcastFragment.OnListFragmentInteractionListener,
                                                                   FavouriteFragment.OnListFragmentInteractionListener,
                                                                   VocalUIFragment.OnFragmentInteractionListener
                                                                   {


    private Station selectedStation;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private static int STATE = 1;
    PlayerFragment pf;

    private static final String TAG = MainTestActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private TextToSpeech tts;

    private boolean isReady;

    Locale current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        requestRecordAudioPermission();
        FloatingActionButton fab = findViewById(R.id.fab);

        //determination de la locale courante
//        Log.v("locale from app",current.getCountry());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int presentFragmentId = getVisibleFragment().getId();
                Fragment mfragment = null;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //voir car peut être bien d'avoir vue graphique et vocal simultanée: pas de STATE. Dans cas fab permettre le passage au vocal.
                if (STATE == 0) {
                    STATE = 1;
                    mfragment = new RadioFragment();
                } else if (STATE == 1) {
                    STATE = 0;
                    FragmentManager fragmentManager = MainTestActivity.this.getSupportFragmentManager();
                    PlayerFragment pf = (PlayerFragment) fragmentManager.findFragmentById(R.id.player_fragment);
                    if(pf!=null) pf.stop();
                    mfragment = new VocalUIFragment();
                }
                transaction.replace(presentFragmentId, mfragment);
                transaction.commit();
                viewPager.setCurrentItem(0);
            }
        });
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainTestActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public void onListFragmentInteraction(Station station) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("STATION", station);
        pf = new PlayerFragment();
        pf.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.player_fragment, pf).commit();
        viewPager.setCurrentItem(2);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Favourite favourite) {
        Log.v("ok entre favourite ",""+favourite);
        Bundle bundle = new Bundle();
        bundle.putParcelable("FAVOURITE", favourite);
        pf = new PlayerFragment();
        pf.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.player_fragment, pf).commit();
        viewPager.setCurrentItem(2);
    }

    @Override
    public void onListFragmentInteraction(Podcast podcast) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("PODCAST", podcast);
        pf = new PlayerFragment();
        pf.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.player_fragment, pf).commit();
        viewPager.setCurrentItem(2);
    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Station station) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("STATION", station);
        pf = new PlayerFragment();
        pf.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.player_fragment, pf).commit();
        viewPager.setCurrentItem(2);
    }

    /**
     * Checks whether the app has SMS permission.
     */
    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted. Enable the SMS button.
           // enableSmsButton();
            Log.d(TAG, "permission already granted");
        }
    }

    /**
     * Processes permission request codes.
     *
     * @param requestCode  The request code passed in requestPermissions()
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.
                   // enableSmsButton();
                    Log.d(TAG, "permission already granted");
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission), Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                   // disableSmsButton();
                }
            }
        }
    }

    /**
     * Defines a string (destinationAddress) for the phone number
     * and gets the input text for the SMS message.
     * Uses SmsManager.sendTextMessage to send the message.
     * Before sending, checks to see if permission is granted.
     *
     * @param view View (message_icon) that was clicked.
     */
    public void smsSendMessage(View view, String destinationAddress, String scAddress, String smsMessage) {
        // Set the destination phone number.
        // Get the text of the sms message.
        // Set the service center address if needed, otherwise null.
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.

        /*
         *
         * ecrire le message a envoyer avec text to speech
         *
         * */
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Check for permission first.
        checkForSmsPermission();
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage, sentIntent, deliveryIntent);
    }

    Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
