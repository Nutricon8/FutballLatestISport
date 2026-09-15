package com.codesui.footballlatest.fragments;

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

import com.codesui.footballlatest.Adapter.LeaguesAdapter;
import com.codesui.footballlatest.R;
import com.codesui.footballlatest.Utility.JsonResource;
import com.codesui.footballlatest.data.League;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaguesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leagues, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView textEmpty = view.findViewById(R.id.textEmpty);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        List<League> leagueList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        LeaguesAdapter leaguesAdapter = new LeaguesAdapter(requireContext(), requireActivity(),leagueList);

        try {
            progressBar.setVisibility(View.VISIBLE);
            JsonResource jsonResource = new JsonResource();
            // Load and parse the JSON file
            String jsonData = jsonResource.loadJSONFromRawResource(requireContext(), R.raw.leagues);
            JSONArray jsonArray = new JSONArray(jsonData);

            // Access elements from the JSONArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                League item = new League(
                        jsonObject.getString("leagueId"),
                        jsonObject.getString("name"),
                        jsonObject.getString("shortName"),
                        jsonObject.getString("logo"));
                leagueList.add(item);
            }

            recyclerView.setAdapter(leaguesAdapter);
            leaguesAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
            progressBar.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            textEmpty.setText(R.string.error_teams);
            textEmpty.setVisibility(View.VISIBLE);
        }


        /*
        String url = "https://api.football-data.org/v4/competitions";
        recyclerView.setLayoutManager(linearLayoutManager);
        Api api = new Api(getActivity(), getContext());
        api.loadCompetitions(url, recyclerView, progressBar, textEmpty);*/
    }

}