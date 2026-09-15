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

import com.codesui.footballlatest.Adapter.DateAdapter;
import com.codesui.footballlatest.R;
import com.codesui.footballlatest.Utility.Api;
import com.codesui.footballlatest.data.DateModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FixturesFragment extends Fragment {
    RecyclerView dateRecyclerView;
    ProgressBar progressBar;
    DateAdapter dateAdapter;
    ArrayList<DateModel> dateList;
    String currentSelectedDate;//yyyy-MM-dd

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fixtures, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        TextView textEmpty = view.findViewById(R.id.textEmpty);

        dateRecyclerView = view.findViewById(R.id.dateRecyclerView);
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        dateList = generateDateList();

        // Set today as the default selected date (index 3)
        Date today = dateList.get(3).getFullDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentSelectedDate = formatter.format(today);

        // Initialize adapter with selected position = 3
        dateAdapter = new DateAdapter(dateList, 3); // Pass default selected index
        dateRecyclerView.setAdapter(dateAdapter);
        dateRecyclerView.scrollToPosition(3); // Center today if needed

        String url = String.format("https://isport-api-production.up.railway.app/api/isports/schedule?date=%s", currentSelectedDate);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView leagueFilterRecycler = view.findViewById(R.id.leagueFilterRecycler);

        recyclerView.setLayoutManager(linearLayoutManager);
        //leagueFilterRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        Api api = new Api(getActivity(), getContext());
        api.loadMatches(url, recyclerView, leagueFilterRecycler,progressBar, textEmpty);


        // Listen to date change
        dateAdapter.setOnDateSelectedListener(date -> {
            currentSelectedDate = formatter.format(date);
            String newUrl = String.format("https://isport-api-production.up.railway.app/api/isports/schedule?date=%s", currentSelectedDate);
            api.loadMatches(newUrl, recyclerView, leagueFilterRecycler,progressBar, textEmpty);
        });


    }

    private ArrayList<DateModel> generateDateList() {
        ArrayList<DateModel> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = -3; i <= 3; i++) {
            Calendar tempCal = (Calendar) calendar.clone();
            tempCal.add(Calendar.DAY_OF_YEAR, i);

            Date date = tempCal.getTime();

            String dayNumber = new SimpleDateFormat("d", Locale.getDefault()).format(date);
            String dayName = (i == 0) ? "Today" : new SimpleDateFormat("EEE", Locale.getDefault()).format(date);

            dates.add(new DateModel(dayNumber, dayName, date));
        }

        return dates;
    }


}