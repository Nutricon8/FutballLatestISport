package com.codesui.footballlatest.data;

public class Team {
    String teamId;
    String leagueId;
    String name;
    String shortName;
    String logo;
    String foundingDate;
    private boolean isFavorite;

    public Team(String teamId, String leagueId, String name, String shortName, String logo, String foundingDate, boolean isFavorite) {
        this.teamId = teamId;
        this.leagueId = leagueId;
        this.name = name;
        this.shortName = shortName;
        this.logo = logo;
        this.foundingDate = foundingDate;
        this.isFavorite = isFavorite;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLogo() {
        return logo;
    }

    public String getFoundingDate() {
        return foundingDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
