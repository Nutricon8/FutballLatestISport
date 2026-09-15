package com.codesui.footballlatest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codesui.footballlatest.R;
import com.codesui.footballlatest.Utility.Api;
import com.codesui.footballlatest.ads.AppOpenManager;
import com.codesui.footballlatest.ads.BannerManager;
import com.codesui.footballlatest.ads.RewardedInterstitialManager;

public class FavoritesActivity extends AppCompatActivity {
    AppOpenManager appOpenManager;
    RewardedInterstitialManager rewardedInterstitialManager;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        progressBar = findViewById(R.id.progressBar);
        TextView textEmpty = findViewById(R.id.textEmpty);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rewardedInterstitialManager = new RewardedInterstitialManager(FavoritesActivity.this, this);
        rewardedInterstitialManager.loadAd();

        appOpenManager = new AppOpenManager();
        appOpenManager.loadAd(this);

        String url = "https://isport-api-production.up.railway.app/api/isports/teams?leagueId=133";
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        Api api = new Api(FavoritesActivity.this, this);
        api.loadTeams(url, recyclerView, progressBar, textEmpty);

        FrameLayout adViewContainer = findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(this, FavoritesActivity.this, adViewContainer);
        bannerManager.loadBanner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_icon) {
            Intent intent = new Intent(FavoritesActivity.this, StartedActivity.class);
            finish();
            rewardedInterstitialManager.showAdNow();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        appOpenManager.showAdIfAvailable(FavoritesActivity.this);
    }
}