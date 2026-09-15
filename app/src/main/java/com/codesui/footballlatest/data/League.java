package com.codesui.footballlatest.data;

public class League {
    private final String leagueId;
    private final String name;
    private final String shortName;
    private final String logo;

    public League(String leagueId, String name, String shortName, String logo) {
        this.leagueId = leagueId;
        this.name = name;
        this.shortName = shortName;
        this.logo = logo;
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

}
