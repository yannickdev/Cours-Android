//package com.yannick.radioo;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.speech.SpeechRecognizer;
//import android.speech.tts.TextToSpeech;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
///*import com.google.common.base.Optional;
//import com.optimaize.langdetect.LanguageDetector;
//import com.optimaize.langdetect.LanguageDetectorBuilder;
//import com.optimaize.langdetect.i18n.LdLocale;
//import com.optimaize.langdetect.ngram.NgramExtractors;
//import com.optimaize.langdetect.profiles.LanguageProfile;
//import com.optimaize.langdetect.profiles.LanguageProfileReader;
//import com.optimaize.langdetect.text.CommonTextObjectFactories;
//import com.optimaize.langdetect.text.TextObject;
//import com.optimaize.langdetect.text.TextObjectFactory;*/
//
///*import com.optimaize.langdetect.LanguageDetector;
//import com.optimaize.langdetect.LanguageDetectorBuilder;
//import com.optimaize.langdetect.guava.base.Optional;
//import com.optimaize.langdetect.ngram.NgramExtractors;
//import com.optimaize.langdetect.profiles.LanguageProfileReader;
//import com.optimaize.langdetect.text.CommonTextObjectFactories;
//import com.optimaize.langdetect.text.TextObject;
//import com.optimaize.langdetect.text.TextObjectFactory;*/
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
///*import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
//import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
//import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentificationOptions;
//import com.google.firebase.ml.naturallanguage.languageid.IdentifiedLanguage;*/
//
//import com.detectlanguage.DetectLanguage;
//import com.detectlanguage.errors.APIError;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.net.URL;
//import java.nio.file.FileSystems;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import static java.util.Locale.getAvailableLocales;
//
//
//public class MainActivity extends Activity
//{
//    private TextView mTextView;
//    private Button toVocalUI;
//    private Button toGraphicalUI;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        requestRecordAudioPermission();
//
//        //toVocalUI = findViewById(R.id.btn_to_vocal_ui);
//       // toGraphicalUI = findViewById(R.id.btn_to_graphical_ui);
//
//        toVocalUI.setOnClickListener(new View.OnClickListener() {
//            Activity _this = MainActivity.this;
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(_this,VocalUIActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//
//       /* String text ="Bonjour tout le monde";
//        FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
//        languageIdentifier.identifyLanguage(text)
//                .addOnSuccessListener(
//                        new OnSuccessListener<String>() {
//                            @Override
//                            public void onSuccess(@Nullable String languageCode) {
//                                if (languageCode != "und") {
//                                    Log.i("firebase", "Language: " + languageCode);
//                                } else {
//                                    Log.i("firebase", "Can't identify language.");
//                                }
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Model couldn’t be loaded or other internal error.
//                                // ...
//                            }
//                        });*/
//
////        try {
////            List
////
////                    languageProfiles = new LanguageProfileReader().readAllBuiltIn(); //build language detector:
////            //
////            LanguageDetector languageDetector = LanguageDetectorBuilder
////                    .create(NgramExtractors.standard())
////                    .withProfiles(languageProfiles)
////                    .build(); //create a text object factory
////             TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText(); //query:
////             TextObject textObject = textObjectFactory.forText("my text");
////             Optional lang = languageDetector.detect(textObject);
////             Log.v("lang",lang.get().toString());
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//
//
//        //load all languages:
//        /*List<LanguageProfile> languageProfiles = null;
//        try {
//            languageProfiles = new LanguageProfileReader().readAllBuiltIn();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////build language detector:
//        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
//                .withProfiles(languageProfiles)
//                .build();
//
////create a text object factory
//        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();*/
//
////query:
//       /* TextObject textObject = textObjectFactory.forText("my text");
//        Optional<LdLocale> lang = languageDetector.detect(textObject);*/
//
//        //load all languages:
//        /*List<LanguageProfile> languageProfiles = null;
//        try {
//            languageProfiles = new LanguageProfileReader().readAllBuiltIn();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////build language detector:
//        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
//                .withProfiles(languageProfiles)
//                .build();
//
////create a text object factory
//        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
//
////query:
//        TextObject textObject = textObjectFactory.forText("bonjour tout le monde!");
//        Optional<LdLocale> lang = languageDetector.detect(textObject);
//        Log.v("language",lang.get().getLanguage());*/
//
////        try {
////            DetectorFactory.loadProfile(new DefaultProfile());
////            LanguageDetect.init("C:\\Users\\YANNICK\\Desktop\\Android_cours\\language-detection\\profiles");
////            ArrayList<Language> langs=LanguageDetect.detectLangs("hello world");
////            for(Language l:langs){
////                System.out.println(l.lang);
////            }
////        } catch (LangDetectException e) {
////            e.printStackTrace();
////        }
//
//       /* try {
//            String sample = "Comment vous appelez-vous?";
//            // Prepare the profile before
//            DetectorFactory.loadProfile(loadProfiles(getAvailableLocales()));
//            // Create the Detector
//            Detector d = DetectorFactory.create();
//            d.append(sample);
//
//            System.out.println(d.detect()); // Ouput: "fr"
//        } catch (LangDetectException e) {
//            e.printStackTrace();
//        }*/
//
//        toGraphicalUI.setOnClickListener(new View.OnClickListener() {
//            Activity _this = MainActivity.this;
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(_this,GraphicalUIActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        //FileSystems.getDefault().getPath("logs", "access.log");
//        Log.v("data directory",""+Environment.getDataDirectory());
//        Log.v("root directory",""+Environment.getRootDirectory());
//        String path = this.getApplicationContext().getFilesDir().getAbsolutePath();
//        Log.v("root directory",""+path);
//
//    }
//
//
//
//
//    private void requestRecordAudioPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            String requiredPermission = Manifest.permission.RECORD_AUDIO;
//            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
//                requestPermissions(new String[]{requiredPermission}, 101);
//            }
//        }
//    }
//
//
//   /* private void identifyPossibleLanguages(final String inputText) {
//        FirebaseLanguageIdentification languageIdentification =
//                FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
//        languageIdentification
//                .identifyPossibleLanguages(inputText)
//                .addOnSuccessListener(
//                        this,
//                        new OnSuccessListener<List<IdentifiedLanguage>>() {
//                            @Override
//                            public void onSuccess(List<IdentifiedLanguage> identifiedLanguages) {
//                                List<String> detectedLanguages =
//                                        new ArrayList<>(identifiedLanguages.size());
//                                for (IdentifiedLanguage language : identifiedLanguages) {
//                                    detectedLanguages.add(
//                                            String.format(
//                                                    Locale.US,
//                                                    "%s (%3f)",
//                                                    language.getLanguageCode(),
//                                                    language.getConfidence())
//                                    );
//                                }
//                                Log.v("lang",""+detectedLanguages);
//                               *//* outputText.append(
//                                        String.format(
//                                                Locale.US,
//                                                "\n%s - [%s]",
//                                                inputText,
//                                                TextUtils.join(", ", detectedLanguages)));*//*
//                            }
//                        })
//                .addOnFailureListener(
//                        this,
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e("error", "Language identification error", e);
//
//                            }
//                        });
//    }
//
//    private void identifyLanguage(final String inputText) {
//        FirebaseLanguageIdentification languageIdentification =
//                FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
//        languageIdentification
//                .identifyLanguage(inputText)
//                .addOnSuccessListener(
//                        this,
//                        new OnSuccessListener<String>() {
//                            @Override
//                            public void onSuccess(String s) {
//                                Log.v("lang",s);
//                            }
//                        })
//                .addOnFailureListener(
//                        this,
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e("error", "Language identification error", e);
//
//                            }
//                        });*/
//
//
//}
//
//
//
//
//
//
