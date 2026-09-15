package com.codesui.footballlatest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codesui.footballlatest.R;
import com.codesui.footballlatest.Utility.DateUtils;
import com.codesui.footballlatest.activities.MatchActivity;
import com.codesui.footballlatest.ads.InterstitialManager;
import com.codesui.footballlatest.data.Match;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LeagueFixturesAdapter extends RecyclerView.Adapter<LeagueFixturesAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private final List<Match> matchList;
    InterstitialManager interstitialManager = new InterstitialManager();

    public LeagueFixturesAdapter(Context context, Activity activity, List<Match> list) {
        this.context = context;
        this.activity = activity;
        this.matchList = list;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final Match match = this.matchList.get(position);


        holder.textHome.setText(match.getHomeTeamName());
        holder.textAway.setText(match.getAwayTeamName());
        Picasso.get().load(match.getHomeTeamLogo()).placeholder(R.drawable.image5).error(R.drawable.image5).into(holder.homeImage);
        Picasso.get().load(match.getAwayTeamLogo()).placeholder(R.drawable.image5).error(R.drawable.image5).into(holder.awayImage);
        holder.textHomeResult.setText(match.getHomeScore());
        holder.textAwayResult.setText(match.getAwayScore());

        /*holder.time.setText(DateUtils.convertUtcToLocalTime(match.getDate()));
        if (match.getStatus().equals("LIVE") || match.getStatus().equals("FINISHED") || match.getStatus().equals("IN_PLAY") || match.getStatus().equals("PAUSED")) {
            if (match.getStatus().equals("FINISHED")) {
                holder.duration.setText(R.string.ft);
            } else {
                //holder.duration.setText(time);
                holder.duration.setText("🔥");
            }
        } else {
            if (match.getStatus().equals("SCHEDULED") || match.getStatus().equals("TIMED") || match.getStatus().equals("POSTPONED")) {
                holder.duration.setText("\uD83D\uDD52");
            } else {
                holder.duration.setText(match.getStatus());
            }
        }*/

        holder.itemView.setOnClickListener(view -> {
            Intent fixturesIntent = new Intent(LeagueFixturesAdapter.this.context, MatchActivity.class);
            fixturesIntent.putExtra("id", match.getMatchId());
            fixturesIntent.putExtra("status", match.getStatus());
            LeagueFixturesAdapter.this.context.startActivity(fixturesIntent);
            interstitialManager.showInterstitial(LeagueFixturesAdapter.this.activity);
        });

    }


    public int getItemCount() {
        return this.matchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textHome;
        private final TextView textAway;
        private final ImageView homeImage;
        private final ImageView awayImage;
        private final TextView textHomeResult;
        private final TextView textAwayResult;
        private final TextView duration;
        private final TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textHome = itemView.findViewById(R.id.textHome);
            this.textAway = itemView.findViewById(R.id.textAway);
            this.textHomeResult = itemView.findViewById(R.id.textHomeResult);
            this.textAwayResult = itemView.findViewById(R.id.textAwayResult);
            this.homeImage = itemView.findViewById(R.id.homeImage);
            this.awayImage = itemView.findViewById(R.id.awayImage);
            this.duration = itemView.findViewById(R.id.duration);
            this.time = itemView.findViewById(R.id.time);
        }
    }

}
