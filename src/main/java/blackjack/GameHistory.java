package blackjack;

import java.util.Arrays;
import java.util.List;

public class GameHistory {
    private int playerWinsThisGame;
    private int dealerWinsThisGame;
    private int gamesPlayedThisSession;

    private List<Integer> saveableInformation = Arrays.asList(0, 0, 0, 0, 0);
    //Valgt å ikke lagre en økning i totalgamesplayed før en runde er helt over

    public GameHistory() {  //Konstruktør når det er noe galt med fillagringen
        this.playerWinsThisGame = 0;
        this.dealerWinsThisGame = 0;
        this.gamesPlayedThisSession = 0;
    }
    public GameHistory(List<Integer> info) { //Konstruktør når det ikke er noe galt med fillagringen
        //At størrelsen på info matcher størrelsen til saveableInformation sjekkes i FileHandler som initialiserer denne konstruktøren
        this();
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i) < 0) {
                throw new IllegalArgumentException("A saveable GameHistoryValue in info is below 0");
            }
            saveableInformation.set(i, info.get(i));   
        } 
    }

    public int getPlayerWinsThisGame() {
        return playerWinsThisGame;
    }
    public void setPlayerWinsThisGame(int playerWinsThisGame) {
        if (!validValue(playerWinsThisGame)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.playerWinsThisGame = playerWinsThisGame;
    }
    public int getDealerWinsThisGame() {
        return dealerWinsThisGame;
    }
    public void setDealerWinsThisGame(int dealerWinsThisGame) {
        if (!validValue(dealerWinsThisGame)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.dealerWinsThisGame = dealerWinsThisGame;
    }
    public int getGamesPlayedThisSession() {
        return gamesPlayedThisSession;
    }
    public void setGamesPlayedThisSession(int gamesPlayedThisSession) {
        if (!validValue(gamesPlayedThisSession)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.gamesPlayedThisSession = gamesPlayedThisSession;
    }

    public int getPlayerWinsInTotal() {
        return saveableInformation.get(0);
    }
    public void setPlayerWinsInTotal(int playerWinsInTotal) {
        if (!validValue(playerWinsInTotal)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.saveableInformation.set(0, playerWinsInTotal);
    }
    public int getDealerWinsInTotal() {
        return saveableInformation.get(1);
    }
    public void setDealerWinsInTotal(int dealerWinsInTotal) {
        if (!validValue(dealerWinsInTotal)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.saveableInformation.set(1, dealerWinsInTotal);
    }
    public int getPlayerStreak() {
        return saveableInformation.get(2);
    }
    public void setPlayerStreak(int playerStreak) {
        if (!validValue(playerStreak)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.saveableInformation.set(2, playerStreak);
    }
    public int getDealerStreak() {
        return saveableInformation.get(3);
    }
    public void setDealerStreak(int dealerStreak) {
        if (!validValue(dealerStreak)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.saveableInformation.set(3, dealerStreak);
    }
    public int getTotalGamesPlayed() {
        return saveableInformation.get(4);
    }
    public void setTotalGamesPlayed(int totalGamesPlayed) {
        if (!validValue(totalGamesPlayed)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.saveableInformation.set(4, totalGamesPlayed);
    }
    

    public List<Integer> getSaveableInformation() {
        return saveableInformation;
    }

    private boolean validValue(int value) {
        return value >= 0;
    }
}
