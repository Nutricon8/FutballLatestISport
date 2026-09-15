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
import com.codesui.footballlatest.activities.LeagueActivity;
import com.codesui.footballlatest.activities.MatchActivity;
import com.codesui.footballlatest.ads.InterstitialManager;
import com.codesui.footballlatest.data.FixtureItem;
import com.codesui.footballlatest.data.Match;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FixturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final Activity activity;
    private final List<FixtureItem> fixtureItems;
    private final InterstitialManager interstitialManager = new InterstitialManager();

    public FixturesAdapter(Context context, Activity activity, List<FixtureItem> fixtureItems) {
        this.context = context;
        this.activity = activity;
        this.fixtureItems = fixtureItems;
    }

    @Override
    public int getItemViewType(int position) {
        return fixtureItems.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FixtureItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture, parent, false);
            return new MatchViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FixtureItem item = fixtureItems.get(position);

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder header = (HeaderViewHolder) holder;
            header.leagueName.setText(item.getLeagueName());
            Picasso.get().load(item.getLeagueLogo()).placeholder(R.drawable.image5).error(R.drawable.image5).into(header.leagueLogo);

            holder.itemView.setOnClickListener(view -> {
                Intent leagueIntent = new Intent(FixturesAdapter.this.context, LeagueActivity.class);
                leagueIntent.putExtra("leagueName", item.getLeagueName());
                leagueIntent.putExtra("leagueId", item.getLeagueId());
                FixturesAdapter.this.context.startActivity(leagueIntent);
                interstitialManager.showInterstitial(FixturesAdapter.this.activity);
            });
        } else if (holder instanceof MatchViewHolder) {
            final Match match = item.getMatch();
            MatchViewHolder matchHolder = (MatchViewHolder) holder;

            matchHolder.textHome.setText(match.getHomeTeamName());
            matchHolder.textAway.setText(match.getAwayTeamName());
            Picasso.get().load(match.getHomeTeamLogo()).placeholder(R.drawable.image5).error(R.drawable.image5).into(matchHolder.homeImage);
            Picasso.get().load(match.getAwayTeamLogo()).placeholder(R.drawable.image5).error(R.drawable.image5).into(matchHolder.awayImage);
            matchHolder.textHomeResult.setText(match.getHomeScore());
            matchHolder.textAwayResult.setText(match.getAwayScore());
            //matchHolder.time.setText(DateUtils.convertUtcToLocalTime(match.getDate()));

            /*String status = match.getStatus();
            if (status.equals("LIVE") || status.equals("FINISHED") || status.equals("IN_PLAY") || status.equals("PAUSED")) {
                //matchHolder.duration.setText(status.equals("FINISHED") ? context.getString(R.string.ft) : "🔥");

                if (status.equals("FINISHED")) {
                    matchHolder.duration.setText(R.string.ft);
                } else {
                    if (match.hasMinute()) {
                        // Safe to use the string
                        matchHolder.duration.setText(match.getMinute());
                    } else {
                        matchHolder.duration.setText("TBD");
                    }
                }
            } else {

                if (status.equals("SCHEDULED") || status.equals("TIMED") || status.equals("POSTPONED")) {
                    matchHolder.duration.setVisibility(View.GONE);
                } else {
                    matchHolder.duration.setText(status);
                }
            }*/

            matchHolder.itemView.setOnClickListener(view -> {
                Intent fixturesIntent = new Intent(context, MatchActivity.class);
                fixturesIntent.putExtra("id", match.getMatchId());
                fixturesIntent.putExtra("status", match.getStatus());
                context.startActivity(fixturesIntent);
                interstitialManager.showInterstitial(activity);
            });
        }
    }

    public void updateData(List<FixtureItem> newItems) {
        fixtureItems.clear();
        fixtureItems.addAll(newItems);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return fixtureItems.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView textHome, textAway, textHomeResult, textAwayResult, duration, time;
        ImageView homeImage, awayImage;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            textHome = itemView.findViewById(R.id.textHome);
            textAway = itemView.findViewById(R.id.textAway);
            textHomeResult = itemView.findViewById(R.id.textHomeResult);
            textAwayResult = itemView.findViewById(R.id.textAwayResult);
            homeImage = itemView.findViewById(R.id.homeImage);
            awayImage = itemView.findViewById(R.id.awayImage);
            duration = itemView.findViewById(R.id.duration);
            time = itemView.findViewById(R.id.time);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView leagueName;
        ImageView leagueLogo;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueName = itemView.findViewById(R.id.leagueName);
            leagueLogo = itemView.findViewById(R.id.leagueLogo);
        }
    }

    public static List<FixtureItem> groupMatchesByLeague(List<Match> matches) {
        List<FixtureItem> result = new ArrayList<>();
        Set<String> seenLeagues = new HashSet<>();

        for (Match match : matches) {
            if (!seenLeagues.contains(match.getLeagueId())) {
                // Add header with league info (match is null here)
                result.add(new FixtureItem(
                        FixtureItem.TYPE_HEADER,
                        match.getLeagueId(),
                        match,
                        match.getLeagueName(),
                        null
                ));
                seenLeagues.add(match.getLeagueId());
            }

            // Add the actual match item
            result.add(new FixtureItem(
                    FixtureItem.TYPE_MATCH,
                    match.getLeagueId(),
                    match,
                    match.getLeagueName(),
                    null
            ));
        }

        return result;
    }

}
