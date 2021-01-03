//package com.yannick.radioo;
//
//import android.net.Uri;
//import android.os.Bundle;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.tabs.TabLayout;
//import com.yannick.radioo.radios.PlayerFragment;
//import com.yannick.radioo.radios.RadioFragment;
//
//import com.yannick.radioo.ui.main.SectionsPagerAdapter;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.viewpager.widget.ViewPager;
//
//public class Playing2Activity extends AppCompatActivity implements RadioFragment.OnListFragmentInteractionListener, PlayerFragment.OnFragmentInteractionListener {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_playing2);
//
//       // BottomNavigationView navView = findViewById(R.id.nav_view);
////        // Passing each menu ID as a set of Ids because each
////        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
////        NavigationUI.setupWithNavController(navView, navController);
//
//      //  SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//
//        //pager.setAdapter(new PageAdapter(getSupportFragmentManager()));
//
////        ViewPager viewPager = findViewById(R.id.view_pager);
////        viewPager.setAdapter(sectionsPagerAdapter);
////        TabLayout tabs = findViewById(R.id.tabs);
////        tabs.setupWithViewPager(viewPager);
////
////        tabs.setTabMode(TabLayout.MODE_FIXED);
//
//        //Get ViewPager from layout
//        ViewPager pager = (ViewPager)findViewById(R.id.view_pager);
//        //Set Adapter PageAdapter and glue it together
//        pager.setAdapter(new SectionsPagerAdapter(this, getSupportFragmentManager()));
//
//        // 1 - Get TabLayout from layout
//        TabLayout tabs= findViewById(R.id.tabs);
//        // 2 - Glue TabLayout and ViewPager together
//        tabs.setupWithViewPager(pager);
//        // 3 - Design purpose. Tabs have the same width
//        tabs.setTabMode(TabLayout.MODE_FIXED);
//
//        //pager.setOffscreenPageLimit(3);
//
//
//       // FloatingActionButton fab = findViewById(R.id.fab);
//
//        /*fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });*/
//
//
//    }
//
//    @Override
//    public void onListFragmentInteraction(Station item) {
//
//    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
//}
