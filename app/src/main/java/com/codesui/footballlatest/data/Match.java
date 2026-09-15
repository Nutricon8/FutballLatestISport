package com.codesui.footballlatest.data;

public class Match {

    private final String matchId;
    private final String leagueId;
    private final String leagueName;
    private final String homeTeamId;
    private final String homeTeamName;
    private final String homeTeamLogo;
    private int homeScore;
    private final String awayTeamId;
    private final String awayTeamName;
    private final String awayTeamLogo;
    private int awayScore;
    private final int status;
    private final long matchTime;
    private final String kickoff;

    public Match(String matchId, String leagueId, String leagueName,
                 String homeTeamId, String homeTeamName, String homeTeamLogo, int homeScore,
                 String awayTeamId, String awayTeamName, String awayTeamLogo, int awayScore,
                 int status, long matchTime, String kickoff) {
        this.matchId = matchId;
        this.leagueId = leagueId;
        this.leagueName = leagueName;
        this.homeTeamId = homeTeamId;
        this.homeTeamName = homeTeamName;
        this.homeTeamLogo = homeTeamLogo;
        this.homeScore = homeScore;
        this.awayTeamId = awayTeamId;
        this.awayTeamName = awayTeamName;
        this.awayTeamLogo = awayTeamLogo;
        this.awayScore = awayScore;
        this.status = status;
        this.matchTime = matchTime;
        this.kickoff = kickoff;
    }

    public String getMatchId() { return matchId; }

    public String getLeagueId() { return leagueId; }

    public String getLeagueName() { return leagueName; }

    public String getHomeTeamId() { return homeTeamId; }

    public String getHomeTeamName() { return homeTeamName; }

    public String getHomeTeamLogo() { return homeTeamLogo; }

    public int getHomeScore() { return homeScore; }

    public void setHomeScore(int homeScore) { this.homeScore = homeScore; }

    public String getAwayTeamId() { return awayTeamId; }

    public String getAwayTeamName() { return awayTeamName; }

    public String getAwayTeamLogo() { return awayTeamLogo; }

    public int getAwayScore() { return awayScore; }

    public void setAwayScore(int awayScore) { this.awayScore = awayScore; }

    public int getStatus() { return status; }

    public long getMatchTime() { return matchTime; }

    public String getKickoff() { return kickoff; }
}