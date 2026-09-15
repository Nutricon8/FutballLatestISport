package com.codesui.footballlatest.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codesui.footballlatest.R;
import com.codesui.footballlatest.data.League;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LeagueFilterAdapter extends RecyclerView.Adapter<LeagueFilterAdapter.LeagueViewHolder> {

    private final List<League> leagueItems;
    private final OnLeagueClickListener listener;
    private int selectedPosition = 0; // "All Leagues" selected by default

    public interface OnLeagueClickListener {
        void onLeagueClick(String leagueId);
    }

    public LeagueFilterAdapter(List<League> leagueItems, OnLeagueClickListener listener) {
        this.leagueItems = leagueItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_league, parent, false);
        return new LeagueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueViewHolder holder, @SuppressLint("RecyclerView") int position) {
        League league = leagueItems.get(position);
        holder.leagueName.setText(league.getName());

        // Load logo or placeholder
        if (league.getLogo() != null && !league.getLogo().isEmpty()) {
            Picasso.get().load(league.getLogo()).into(holder.leagueLogo);
        } else {
            holder.leagueLogo.setImageResource(R.drawable.image2); // Placeholder
        }

        // Highlight selected item
        holder.itemView.setBackgroundResource(
                selectedPosition == position ? R.drawable.selected_league : R.drawable.unselected_league
        );

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
            listener.onLeagueClick(league.getLeagueId());
        });
    }

    @Override
    public int getItemCount() {
        return leagueItems.size();
    }

    static class LeagueViewHolder extends RecyclerView.ViewHolder {
        ImageView leagueLogo;
        TextView leagueName;

        public LeagueViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueLogo = itemView.findViewById(R.id.leagueImage);
            leagueName = itemView.findViewById(R.id.leagueName);
        }
    }
}


