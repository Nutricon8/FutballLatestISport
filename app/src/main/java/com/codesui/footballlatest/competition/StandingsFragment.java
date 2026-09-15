package com.codesui.footballlatest.competition;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codesui.footballlatest.Adapter.StandingsAdapter;
import com.codesui.footballlatest.R;
import com.codesui.footballlatest.activities.LeagueActivity;
import com.codesui.footballlatest.data.Standing;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandingsFragment extends Fragment {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView textEmpty;

    private List<Standing> totalStandings = new ArrayList<>();
    private List<Standing> homeStandings = new ArrayList<>();
    private List<Standing> awayStandings = new ArrayList<>();
    private StandingsAdapter standingsAdapter;
    private int currentTableType = 0; // 0 = Total, 1 = Home, 2 = Away

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_standings, container, false);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        textEmpty = view.findViewById(R.id.textEmpty);
        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        standingsAdapter = new StandingsAdapter(totalStandings);
        recyclerView.setAdapter(standingsAdapter);

        // Setup chip group for table type toggle
        Chip chipOverall = view.findViewById(R.id.chipOverall);
        Chip chipHome = view.findViewById(R.id.chipHome);
        Chip chipAway = view.findViewById(R.id.chipAway);

        chipOverall.setOnClickListener(v -> switchTableType(0));
        chipHome.setOnClickListener(v -> switchTableType(1));
        chipAway.setOnClickListener(v -> switchTableType(2));

        LeagueActivity competitionActivity = (LeagueActivity) getActivity();
        int competitionId = competitionActivity.competitionId;

        String url = "https://api.football-data.org/v4/competitions/" + competitionId + "/standings";
        loadAllStandings(url);
    }

    private void switchTableType(int type) {
        currentTableType = type;
        List<Standing> selectedList;

        switch (type) {
            case 1:
                selectedList = homeStandings;
                break;
            case 2:
                selectedList = awayStandings;
                break;
            default:
                selectedList = totalStandings;
                break;
        }

        standingsAdapter = new StandingsAdapter(selectedList);
        recyclerView.setAdapter(standingsAdapter);
        standingsAdapter.notifyDataSetChanged();
    }

    private void loadAllStandings(String url) {
        progressBar.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray standingsArray = response.getJSONArray("standings");

                        for (int i = 0; i < standingsArray.length(); i++) {
                            JSONObject standingObj = standingsArray.getJSONObject(i);
                            String type = standingObj.optString("type", "TOTAL");
                            JSONArray tableArray = standingObj.getJSONArray("table");

                            List<Standing> targetList;

                            switch (type) {
                                case "HOME":
                                    targetList = homeStandings;
                                    break;
                                case "AWAY":
                                    targetList = awayStandings;
                                    break;
                                default:
                                    targetList = totalStandings;
                                    break;
                            }

                            targetList.clear();
                            for (int j = 0; j < tableArray.length(); j++) {
                                JSONObject jsonObject = tableArray.getJSONObject(j);
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
                                targetList.add(standing);
                            }
                        }

                        // Show total standings by default
                        standingsAdapter = new StandingsAdapter(totalStandings);
                        recyclerView.setAdapter(standingsAdapter);
                        standingsAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        if (totalStandings.isEmpty()) {
                            textEmpty.setText(R.string.no_standings_found);
                            textEmpty.setVisibility(View.VISIBLE);
                        } else {
                            textEmpty.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        if (totalStandings.isEmpty()) {
                            textEmpty.setText(R.string.error_standings);
                            textEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }, error -> {
                    progressBar.setVisibility(View.GONE);
                    if (totalStandings.isEmpty()) {
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
        requestQueue.add(jsonObjectRequest);
    }
}