package com.codesui.footballlatest.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codesui.footballlatest.Adapter.FixturesAdapter;
import com.codesui.footballlatest.Adapter.LeagueFilterAdapter;
import com.codesui.footballlatest.Adapter.LeagueFixturesAdapter;
import com.codesui.footballlatest.Adapter.LeaguesAdapter;
import com.codesui.footballlatest.Adapter.PlayersAdapter;
import com.codesui.footballlatest.Adapter.StandingsAdapter;
import com.codesui.footballlatest.Adapter.TeamsAdapter;
import com.codesui.footballlatest.R;
import com.codesui.footballlatest.data.FixtureItem;
import com.codesui.footballlatest.data.League;
import com.codesui.footballlatest.data.Match;
import com.codesui.footballlatest.data.Player;
import com.codesui.footballlatest.data.Standing;
import com.codesui.footballlatest.data.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Api {
    Activity activity;
    Context context;

    public Api(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void loadCompetitions(String url, RecyclerView recyclerView, ProgressBar progressBar, TextView textEmpty) {
        List<League> leagueList = new ArrayList<>();
        LeaguesAdapter leaguesAdapter = new LeaguesAdapter(context, activity, leagueList);
        recyclerView.setAdapter(leaguesAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("competitions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            League item = new League(
                                    jsonArray.getJSONObject(i).getString("leagueId"),
                                    jsonArray.getJSONObject(i).getString("name"),
                                    jsonArray.getJSONObject(i).getString("shortName"),
                                    jsonArray.getJSONObject(i).getString("logo"));
                            leagueList.add(item);
                        }
                        leaguesAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        if (leagueList.isEmpty()) {
                            textEmpty.setText(R.string.no_leagues_found);
                            textEmpty.setVisibility(View.VISIBLE);
                        } else {
                            textEmpty.setVisibility(View.GONE); // Hide emptyText if data is present
                        }
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        if (leagueList.isEmpty()) {
                            textEmpty.setText(R.string.error_leagues);
                            textEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }, error -> {
                    //alertDialog.show();
                    progressBar.setVisibility(View.GONE);
                    if (leagueList.isEmpty()) {
                        textEmpty.setText(R.string.error_leagues);
                        textEmpty.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Auth-Token", "6f290c7d3caf4bb69f07dacaf7273267");
                params.put("Accept", "application/json");
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);
        requestQueue.add(jsonObjectRequest);
    }

    public void loadTeams(String url, RecyclerView recyclerView, ProgressBar progressBar, TextView textEmpty) {
        List<Team> teamList = new ArrayList<>();
        TeamsAdapter teamsAdapter = new TeamsAdapter(teamList);
        recyclerView.setAdapter(teamsAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(context);


        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Team team = new Team(
                                    jsonObject.getString("teamId"),
                                    jsonObject.getString("leagueId"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("shortName"),
                                    jsonObject.getString("logo"),
                                    jsonObject.getString("foundingDate"),
                                    false
                            );
                            teamList.add(team);
                        }
                        teamsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        if (teamList.isEmpty()) {
                            textEmpty.setText(R.string.no_teams_found);
                            textEmpty.setVisibility(View.VISIBLE);
                        } else {
                            textEmpty.setVisibility(View.GONE); // Hide emptyText if data is present
                        }

                    } catch (JSONException e) {
                        //dialogManager.showDialog();
                        progressBar.setVisibility(View.GONE);
                        if (teamList.isEmpty()) {
                            textEmpty.setText(R.string.no_teams_found);
                            textEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }, error -> {
                    //dialogManager.showDialog();
                    progressBar.setVisibility(View.GONE);
                    if (teamList.isEmpty()) {
                        textEmpty.setText(R.string.error_teams);
                        //textEmpty.setText(competitionId);
                        textEmpty.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Auth-Token", "6f290c7d3caf4bb69f07dacaf7273267");
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);
        requestQueue.add(jsonObjectRequest);
    }


    public void loadStandings(String url, RecyclerView recyclerView, ProgressBar progressBar, TextView textEmpty) {
        List<Standing> standingList = new ArrayList<>();
        StandingsAdapter standingsAdapter = new StandingsAdapter(standingList);
        recyclerView.setAdapter(standingsAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("standings").getJSONObject(0).getJSONArray("table");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Standing standing = new Standing(
                                    jsonObject.getString("position"),
                                    jsonObject.getJSONObject("team").getString("crest"),
                                    jsonObject.getJSONObject("team").getString("shortName"),
                                    jsonObject.getString("playedGames"),
                                    jsonObject.getString("won"),
                                    jsonObject.getString("draw"),
                                    jsonObject.getString("lost"),
                                    jsonObject.getString("goalDifference"),
                                    jsonObject.getString("points"));
                            standingList.add(standing);
                        }
                        standingsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        if (standingList.isEmpty()) {
                            textEmpty.setText(R.string.no_standings_found);
                            textEmpty.setVisibility(View.VISIBLE);
                        } else {
                            textEmpty.setVisibility(View.GONE); // Hide emptyText if data is present
                        }
                    } catch (JSONException e) {
                        //dialogManager.showDialog();
                        progressBar.setVisibility(View.GONE);
                        if (standingList.isEmpty()) {
                            textEmpty.setText(R.string.error_standings);
                            textEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }, error -> {
                    //dialogManager.showDialog();
                    progressBar.setVisibility(View.GONE);
                    if (standingList.isEmpty()) {
                        textEmpty.setText(R.string.error_standings);
                        textEmpty.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Auth-Token", "6f290c7d3caf4bb69f07dacaf7273267");
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);
        requestQueue.add(jsonObjectRequest);
    }

    public void loadPlayers(String url, RecyclerView recyclerView, ProgressBar progressBar, TextView textEmpty) {
        List<Player> playerList = new ArrayList<>();
        PlayersAdapter playersAdapter = new PlayersAdapter(playerList);
        recyclerView.setAdapter(playersAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        // Parse the "scorers" JSON array from the response
                        JSONArray jsonArray = response.getJSONArray("scorers");

                        // Loop through the array of scorers
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // Extract player details and create Player object
                            Player player = new Player(
                                    jsonObject.getJSONObject("player").getString("id"),
                                    jsonObject.getJSONObject("player").getString("name"),
                                    jsonObject.getString("goals"), // Consider converting to integer if necessary
                                    jsonObject.getJSONObject("team").getString("crest")
                            );

                            // Add the player to the list
                            playerList.add(player);
                        }
                        playersAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        if (playerList.isEmpty()) {
                            textEmpty.setText(R.string.no_topscoreres_found);
                            textEmpty.setVisibility(View.VISIBLE);
                        } else {
                            textEmpty.setVisibility(View.GONE); // Hide emptyText if data is present
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();  // Log the error
                        progressBar.setVisibility(View.GONE);
                        if (playerList.isEmpty()) {
                            textEmpty.setText(R.string.error_topscorers);
                            textEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }, error -> {
                    progressBar.setVisibility(View.GONE);
                    if (playerList.isEmpty()) {
                        textEmpty.setText(R.string.error_topscorers);
                        textEmpty.setVisibility(View.VISIBLE);
                    }
                }) {

            // Add required headers (e.g., Authorization token)
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Auth-Token", "6f290c7d3caf4bb69f07dacaf7273267"); // Keep your token secure!
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);
        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    public void loadMatches(String url, RecyclerView recyclerView, RecyclerView leagueFilterRecycler, ProgressBar progressBar, TextView textEmpty) {
        List<Match> matchList = new ArrayList<>();
        FixturesAdapter fixturesAdapter = new FixturesAdapter(context, activity, new ArrayList<>());
        recyclerView.setAdapter(fixturesAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Match match = new Match(
                                    jsonObject.getString("matchId"),
                                    jsonObject.getString("leagueId"),
                                    jsonObject.getString("leagueName"),
                                    jsonObject.getString("homeTeamId"),
                                    jsonObject.getString("homeTeamName"),
                                    jsonObject.getString("homeTeamLogo"),
                                    jsonObject.optInt("homeScore", 0),
                                    jsonObject.getString("awayTeamId"),
                                    jsonObject.getString("awayTeamName"),
                                    jsonObject.getString("awayTeamLogo"),
                                    jsonObject.optInt("awayScore", 0),
                                    jsonObject.optInt("status", -1),
                                    jsonObject.optLong("matchTime", 0L),
                                    jsonObject.optString("kickoff", "")
                            );

                            matchList.add(match);
                        }

                        // Sort by priority leagues
                        List<Integer> priorityLeagueIds = Arrays.asList(2000, 2001, 2018, 2021, 2016, 2015, 2014, 2002, 2019);
                        Collections.sort(matchList, (m1, m2) -> {
                            int i1 = priorityLeagueIds.indexOf(m1.getLeagueId());
                            int i2 = priorityLeagueIds.indexOf(m2.getLeagueId());

                            if (i1 != -1 && i2 != -1) return Integer.compare(i1, i2);
                            if (i1 != -1) return -1;
                            if (i2 != -1) return 1;
                            return m1.getLeagueName().compareTo(m2.getLeagueName());
                        });

                        // Build a unique list of leagues from matches
                        Set<String> seenLeagueIds = new HashSet<>();
                        List<League> leagueList = new ArrayList<>();

                        // Add "All Leagues" as the first item
                        leagueList.add(new League("", "All Leagues", "All", ""));

                        for (Match match : matchList) {
                            if (!seenLeagueIds.contains(match.getLeagueId())) {
                                leagueList.add(new League(
                                        match.getLeagueId(),
                                        match.getLeagueName(),
                                        match.getLeagueName(),
                                        match.getLeagueName()
                                ));
                                seenLeagueIds.add(match.getLeagueId());
                            }
                        }

                        // Set up horizontal RecyclerView
                        LeagueFilterAdapter leagueFilterAdapter = new LeagueFilterAdapter(leagueList, selectedLeagueId -> {
                            List<Match> filtered = selectedLeagueId.equals("")
                                    ? matchList
                                    : matchList.stream()
                                    .filter(m -> m.getLeagueId().equals(selectedLeagueId))
                                    .collect(Collectors.toList());

                            List<FixtureItem> groupedFiltered = FixturesAdapter.groupMatchesByLeague(filtered);
                            fixturesAdapter.updateData(groupedFiltered);
                        });

                        leagueFilterRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        leagueFilterRecycler.setAdapter(leagueFilterAdapter);


                        // Group and update adapter
                        List<FixtureItem> groupedList = FixturesAdapter.groupMatchesByLeague(matchList);
                        fixturesAdapter.updateData(groupedList);

                        progressBar.setVisibility(View.GONE);
                        textEmpty.setVisibility(groupedList.isEmpty() ? View.VISIBLE : View.GONE);
                        textEmpty.setText(R.string.no_matches_found);

                        if (matchList.isEmpty()) {
                            textEmpty.setText(R.string.no_matches_found);
                            textEmpty.setVisibility(View.VISIBLE);
                        } else {
                            textEmpty.setVisibility(View.GONE); // Hide emptyText if data is present
                        }

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        if (matchList.isEmpty()) {
                            textEmpty.setText(R.string.error_matches);
                            textEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }, error -> {
                    progressBar.setVisibility(View.GONE);
                    if (matchList.isEmpty()) {
                        textEmpty.setText(R.string.error_matches);
                        textEmpty.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Auth-Token", "6f290c7d3caf4bb69f07dacaf7273267");
                params.put("X-Unfold-Goals", "true");
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);
        requestQueue.add(jsonObjectRequest);
    }

    public void loadLeagueMatches(String url, RecyclerView recyclerView, ProgressBar progressBar, TextView textEmpty) {
        List<Match> matchList = new ArrayList<>();
        LeagueFixturesAdapter leagueFixturesAdapter = new LeagueFixturesAdapter(context, activity, matchList);
        recyclerView.setAdapter(leagueFixturesAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("matches");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Match match = new Match(
                                    jsonObject.getString("matchId"),
                                    jsonObject.getString("leagueId"),
                                    jsonObject.getString("leagueName"),
                                    jsonObject.getString("homeTeamId"),
                                    jsonObject.getString("homeTeamName"),
                                    jsonObject.getString("homeTeamLogo"),
                                    jsonObject.optInt("homeScore", 0),
                                    jsonObject.getString("awayTeamId"),
                                    jsonObject.getString("awayTeamName"),
                                    jsonObject.getString("awayTeamLogo"),
                                    jsonObject.optInt("awayScore", 0),
                                    jsonObject.optInt("status", -1),
                                    jsonObject.optLong("matchTime", 0L),
                                    jsonObject.optString("kickoff", "")
                            );

                            matchList.add(match);
                        }
                        leagueFixturesAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        if (matchList.isEmpty()) {
                            textEmpty.setText(R.string.no_matches_found);
                            textEmpty.setVisibility(View.VISIBLE);
                        } else {
                            textEmpty.setVisibility(View.GONE); // Hide emptyText if data is present
                        }

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        if (matchList.isEmpty()) {
                            textEmpty.setText(R.string.error_matches);
                            textEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }, error -> {
                    progressBar.setVisibility(View.GONE);
                    if (matchList.isEmpty()) {
                        textEmpty.setText(R.string.error_matches);
                        textEmpty.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Auth-Token", "6f290c7d3caf4bb69f07dacaf7273267");
                params.put("X-Unfold-Goals", "true");
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);
        requestQueue.add(jsonObjectRequest);
    }
}
