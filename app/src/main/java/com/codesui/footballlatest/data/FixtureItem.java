package com.codesui.footballlatest.data;

public class FixtureItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_MATCH = 1;

    private final int type;
    private final Match match;
    private final String leagueId;
    private final String leagueName;
    private final String leagueLogo;

    public FixtureItem(int type, String leagueId, Match match, String leagueName, String leagueLogo) {
        this.type = type;
        this.leagueId = leagueId;
        this.match = match;
        this.leagueName = leagueName;
        this.leagueLogo = leagueLogo;
    }

    public int getType() {
        return type;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public Match getMatch() {
        return match;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public String getLeagueLogo() {
        return leagueLogo;
    }
}

