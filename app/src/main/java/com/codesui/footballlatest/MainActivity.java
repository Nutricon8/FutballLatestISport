package com.codesui.footballlatest;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.codesui.footballlatest.Utility.NetworkChangeListener;
import com.codesui.footballlatest.Utility.RateManager;
import com.codesui.footballlatest.Utility.ShareManager;
import com.codesui.footballlatest.Utility.UrlManager;
import com.codesui.footballlatest.ads.AppOpenManager;
import com.codesui.footballlatest.ads.BannerManager;
import com.codesui.footballlatest.fragments.FixturesFragment;
import com.codesui.footballlatest.fragments.LeaguesFragment;
import com.codesui.footballlatest.fragments.LivescoresFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NetworkChangeListener networkChangeListener;
    AppOpenManager appOpenManager;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup toolbar with competition name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        appOpenManager = new AppOpenManager();
        appOpenManager.loadAd(this);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FixturesFragment()).commit();

        toolbar.setTitle("Fixtures");
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.leagues) {
                fragment = new LeaguesFragment();
                toolbar.setTitle("Leagues");
            } else if (itemId == R.id.livescores) {
                fragment = new LivescoresFragment();
                toolbar.setTitle("Live");
            } else if (itemId == R.id.more) {
                // Open the navigation drawer when "More" is clicked
                drawer.openDrawer(GravityCompat.START); // Use GravityCompat.START if the drawer is on the left side
                return true; // Return true to indicate event is handled
            } else {
                fragment = new FixturesFragment();
                toolbar.setTitle("Fixtures");
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            return true; // Return true to indicate event is handled
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        FrameLayout adViewContainer = findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(this, MainActivity.this, adViewContainer);
        bannerManager.loadBanner();

        //checkAndRequestNotificationPermission();
        //Intent serviceIntent = new Intent(this, ApiPollingService.class);
        //ContextCompat.startForegroundService(this, serviceIntent);
        //stopService(new Intent(this, ApiPollingService.class)); //You can stop it when needed

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        UrlManager urlManager = new UrlManager(MainActivity.this);
        int itemId = item.getItemId();
        if (itemId == R.id.nav_terms) {
            drawer.closeDrawer(GravityCompat.START);
            urlManager.openTerms();
            return true;
        } else if (itemId == R.id.nav_share) {
            drawer.closeDrawer(GravityCompat.START);
            ShareManager shareManager = new ShareManager(this);
            shareManager.shareApp();
            return true;
        } else if (itemId == R.id.nav_rate) {
            drawer.closeDrawer(GravityCompat.START);
            RateManager rateManager = new RateManager(this);
            rateManager.rate();
            return true;
        } else if (itemId == R.id.nav_more_apps) {
            drawer.closeDrawer(GravityCompat.START);
            urlManager.moreApps();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        networkChangeListener = new NetworkChangeListener();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChangeListener, filter);
        appOpenManager.showAdIfAvailable(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkChangeListener != null) {
            unregisterReceiver(networkChangeListener);
        }
    }

    /*private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS")
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{"android.permission.POST_NOTIFICATIONS"},
                        101
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted - you can now send notifications
            } else {
                // Permission denied - you might want to show a message
            }
        }
    }*/


}
