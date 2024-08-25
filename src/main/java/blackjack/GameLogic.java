package blackjack;

import java.io.FileNotFoundException;
import java.util.List;

public class GameLogic {
    //Tilstander som resettes
    private Person player;
    private Person dealer;
    private CardDeck deck;

    //Tilstander som ikke resettes, men endres
    private IFileHandler fileHandler;
    private DealerAI dealerAI; 
    private GameHistory gameHistory; 

    public GameLogic() {
        player = new Person();
        dealer = new Person();
        deck = new CardDeck();
        fileHandler = new FileHandler();
        try {
            gameHistory = fileHandler.createGameHistory("GameHistoryFile"); //Funksjon i FileHandler lager enten en standard, eller en som følger fila
            dealerAI = fileHandler.createDealerAI("DealerAIFile"); //Funksjon i FileHandler lager enten en standard, eller en som følger fila
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    //Gettere. Settere er unødvendige med løsningen jeg har for resetting av GameLogic
    public Person getPlayer() {
        return player;
    }

    public Person getDealer() {
        return dealer;
    }

    public CardDeck getDeck() {
        return deck;
    }

    public IFileHandler getFileHandler() {
        return fileHandler;
    }

    public DealerAI getDealerAI() {
        return dealerAI;
    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }



    public void resetGame() {
        player = new Person(); //Resetter spilleren med å lage ny player
        dealer = new Person(); //Resetter dealeren med å lage ny dealer
        deck = new CardDeck(); //Resetter decket med å lage nytt deck
    }

    public void updateSavefile(String filename, List<Integer> updatedInformation) { //Sjekker for TestDealerAIFile og TestGameHistoryFile også for at testene ikke endrer på de viktige filene DealerAIFile og GameHistoryFile
        if(((filename.equals("DealerAIFile") || filename.equals("TestDealerAIFile")) && updatedInformation.size() != 9) || ((filename.equals("GameHistoryFile") || filename.equals("TestGameHistoryFile")) && updatedInformation.size() != 5)) {
            throw new IllegalArgumentException("Invalid values");
        }
        if (updatedInformation.stream().anyMatch(i -> i < 0)) {
            throw new IllegalArgumentException("Invalid value in updated Information");
        }

        try {
            fileHandler.updateFile(filename, updatedInformation);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    public String getHoldingValueString(Person person) {
        return String.valueOf(person.getValueOfHand());
    }

    public int getHoldingValueInt(Person person) {
        return person.getValueOfHand();
    }

    public int getHandSize (Person person) { 
        return person.getHandSize();
    }

    public List<Integer> getDealerAIChances() { 
        return dealerAI.getChances();
    }

    public void drawCard(CardHand hand) { 
        deck.drawCard(hand);
    }

    public String getWinner() { 
        int PlayerFinalScore = getHoldingValueInt(player);
        int DealerFinalScore = getHoldingValueInt(dealer);
        if (DealerFinalScore <= 21 && (PlayerFinalScore <= DealerFinalScore || PlayerFinalScore > 21) && DealerFinalScore != PlayerFinalScore) {
            if (DealerFinalScore >= 12 && DealerFinalScore < 21) {
                dealerAI.updateChances(DealerFinalScore, false); //Hvis dealeren står, men vinner, så blir det lavere sjanse for at han trekker kort på denne verdien neste gang
            }
            dealerWonUpdateGameHistory();
            return "Dealer"; 
        } else if (PlayerFinalScore <= 21 && (DealerFinalScore < PlayerFinalScore || DealerFinalScore > 21 && PlayerFinalScore != DealerFinalScore)) {
            if (DealerFinalScore >= 12 && DealerFinalScore < 21) {
                dealerAI.updateChances(DealerFinalScore, true); //Hvis dealeren står, men taper, vil den gøtse mer neste gang, så det er større sjanse for at den trekker et kort på denne verdien
            }
            playerWonUpdateGameHistory();
            return "Player";
        }
        removeStreaks();
        return "It's a tie";
    }

    public String getSuitString(Person person, int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Cant choose a negative card");
        }
        if (i >= getHandSize(person)) {
            throw new IllegalArgumentException("Cant choose a card with index higher than the size of the hand");
        }
        String s = String.valueOf(person.getHand().getCard(i).getSuit());
        if (s.equals("H")) {
            s = "Hearts";
        } else if (s.equals("C")) {
            s = "Clubs";
        } else if (s.equals("D")) {
            s = "Diamonds";
        } else if (s.equals("S")) {
            s = "Spades";
        }
        return s; 
    }
    public String getValueString(Person person, int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Cant choose a negative card");
        }
        if (i >= getHandSize(person)) {
            throw new IllegalArgumentException("Cant choose a card with index higher than the size of the hand");
        }
        String s = String.valueOf(person.getHand().getCard(i).getValue());
        if (s.equals("1")) {
            s = "A";
        } else if (s.equals("11")) {
            s = "Kn";
        } else if (s.equals("12")) {
            s = "Q";
        } else if (s.equals("13")) {
            s = "K";
        }
        return s;
    }
    

    public boolean tooHighValue(CardHand hand) {//Brukes i controlleren så den trengs som public
        return(hand.getValueOfHand() > 21);
    }

    public void getDealerHand() {
        while (dealerAI.decideDraw(dealer)) {
            int tmpValue = getHoldingValueInt(dealer);
            drawCard(dealer.getHand());
            if (tmpValue >= 12) { //Kan ikke gå over 21 når man har 11 eller mindre, så da vil det være 100% sjanse for at dealeren trekker
                if (!tooHighValue(dealer.getHand())) {
                    dealerAI.updateChances(tmpValue, true);
                } else {
                    dealerAI.updateChances(tmpValue, false);
                }
            }
        }
    }

    public String getStreakString() {
        int playerStreak = gameHistory.getPlayerStreak();
        int dealerStreak = gameHistory.getDealerStreak();
        if (playerStreak > dealerStreak) { //GameHistory sørger for at minst en av dem er 0
            return "Player: " + String.valueOf(playerStreak);
        } else if (playerStreak < dealerStreak) {
            return "Dealer: " + String.valueOf(dealerStreak);
        } else {
            return "No streaks";
        }
    }

    public void resetOverallStandings() {
        gameHistory.setPlayerWinsInTotal(0);
        gameHistory.setDealerWinsInTotal(0);
        gameHistory.setPlayerWinsThisGame(0);
        gameHistory.setDealerWinsThisGame(0);
        gameHistory.setPlayerStreak(0);
        gameHistory.setDealerStreak(0);
    }

    public void resetAI() {
        dealerAI = new DealerAI();
    }

    public void resetGamesPlayed() {
        gameHistory.setTotalGamesPlayed(0);
        gameHistory.setGamesPlayedThisSession(0);
    }

    public int getTotalGames() {
        return gameHistory.getTotalGamesPlayed();
    }
    public int getSessionGames() { 
        return gameHistory.getGamesPlayedThisSession();
    }

    public void increaseGamesPlayed() {
        gameHistory.setTotalGamesPlayed(gameHistory.getTotalGamesPlayed() + 1);
        gameHistory.setGamesPlayedThisSession(gameHistory.getGamesPlayedThisSession() + 1);
    }

    private void playerWonUpdateGameHistory() {
        gameHistory.setPlayerWinsInTotal(gameHistory.getPlayerWinsInTotal() + 1);
        gameHistory.setPlayerWinsThisGame(gameHistory.getPlayerWinsThisGame() + 1);
        gameHistory.setPlayerStreak(gameHistory.getPlayerStreak() + 1);
        gameHistory.setDealerStreak(0);
    }
    private void dealerWonUpdateGameHistory() {
        gameHistory.setDealerWinsInTotal(gameHistory.getDealerWinsInTotal() + 1);
        gameHistory.setDealerWinsThisGame(gameHistory.getDealerWinsThisGame() + 1);
        gameHistory.setDealerStreak(gameHistory.getDealerStreak() + 1);
        gameHistory.setPlayerStreak(0);
    }
    private void removeStreaks() {
        gameHistory.setPlayerStreak(0);
        gameHistory.setDealerStreak(0);
    }
}
