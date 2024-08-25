package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class BlackjackController {
    //Labels with scores and winner
    @FXML
    private Label dealerScoreLabel, playerScoreLabel, winnerLabel;
    
    //Labels with suits and values of cards
    @FXML
    private Label dealerCard1Suit, dealerCard1Value, dealerCard2Suit, dealerCard2Value, dealerCard3Suit, dealerCard3Value, dealerCard4Suit, dealerCard4Value, playerCard1Suit, playerCard1Value, playerCard2Suit, playerCard2Value, playerCard3Suit, playerCard3Value, playerCard4Suit, playerCard4Value;

    private List<Label> dealerLabels = new ArrayList<>();
    private List<Label> playerLabels = new ArrayList<>();

    //Labels with scoreboard
    @FXML
    private Label overallScore, thisGameScore, streakScore, totalGamesPlayedScore, gamesPlayedThisSessionScore;

    //Boxes with cards
    @FXML
    private HBox dealerCard1, dealerCard2, dealerCard3, dealerCard4, playerCard1, playerCard2, playerCard3, playerCard4;

    private List<HBox> dealerCards = new ArrayList<>();
    private List<HBox> playerCards = new ArrayList<>();    
    

    //Boxes with playbuttons
    @FXML
    private HBox playingButtonsBox, restartButtonBox, beginButtonBox;

    //Buttons
    @FXML
    private Button drawCardButton, standButton, restartButton, beginButton;

    //Panes
    @FXML
    private Pane settingsPane;

    //Classes
    private GameLogic gameLogic = new GameLogic();
    
    


    //Functions
    @FXML
    private void initialize() {
        playerCards = Arrays.asList(playerCard1, playerCard2, playerCard3, playerCard4);
        dealerCards = Arrays.asList(dealerCard1, dealerCard2, dealerCard3, dealerCard4);
        playerLabels = Arrays.asList(playerCard1Suit, playerCard1Value, playerCard2Suit, playerCard2Value, playerCard3Suit, playerCard3Value, playerCard4Suit, playerCard4Value);
        dealerLabels = Arrays.asList(dealerCard1Suit, dealerCard1Value, dealerCard2Suit, dealerCard2Value, dealerCard3Suit, dealerCard3Value, dealerCard4Suit, dealerCard4Value);
        updateScoreboard();
    }

    @FXML
    private void beginGame() {
        gameLogic.getDealerHand();
        gameLogic.updateSavefile("DealerAIFile", gameLogic.getDealerAIChances());
        updateDealerCardsStartPhase();
        gameLogic.increaseGamesPlayed();
        updateScoreboard();
        playingButtonsBox.setVisible(true);
        beginButtonBox.setVisible(false);
        dealerScoreLabel.setText("Dealer: ?");
    }

    @FXML
    private void drawCard() {
        Person player = gameLogic.getPlayer();
        gameLogic.drawCard(player.getHand());
        updatePlayerHoldingValues();
        updatePlayerCards();
        if (gameLogic.tooHighValue(player.getHand())) {
            standAction();
        }
    }

    @FXML
    private void standAction() {
        winnerLabel.setVisible(true);
        winnerLabel.setText("Winner: " + gameLogic.getWinner() + "!");
        gameLogic.updateSavefile("GameHistoryFile", gameLogic.getGameHistory().getSaveableInformation());
        gameLogic.updateSavefile("DealerAIFile", gameLogic.getDealerAIChances());
        updateScoreboard();
        restartButtonBox.setVisible(true);
        playingButtonsBox.setVisible(false);
        updateDealerHoldingValues();
        updateDealerCardsStandPhase();
    }

    @FXML
    private void restartGame() {
        gameLogic.resetGame();
        updatePlayerHoldingValues();
        updateDealerHoldingValues();
        updatePlayerCards();
        updateDealerCardsStartPhase();
        beginButtonBox.setVisible(true);
        restartButtonBox.setVisible(false);
        winnerLabel.setVisible(false);
    }

    @FXML
    private void openSettings() {
        settingsPane.setVisible(true);
    }

    @FXML
    private void closeSettings() {
        settingsPane.setVisible(false);
    }

    @FXML
    private void resetOverallStandings() {
        gameLogic.resetOverallStandings();
        gameLogic.updateSavefile("GameHistoryFile", gameLogic.getGameHistory().getSaveableInformation());
        updateScoreboard();
    }

    @FXML
    private void resetAI() {
        gameLogic.resetAI();
        gameLogic.updateSavefile("DealerAIFile", gameLogic.getDealerAIChances());
    }

    @FXML
    private void resetGamesPlayed() {
        gameLogic.resetGamesPlayed();
        gameLogic.updateSavefile("GameHistoryFile", gameLogic.getGameHistory().getSaveableInformation());
        updateScoreboard();
    }
    

    private void updatePlayerHoldingValues() {
        playerScoreLabel.setText("Player: " + gameLogic.getHoldingValueString(gameLogic.getPlayer())); //Separate funksjoner fordi de skal gi forskjellig tekst, og brukes kun på ett sted
    }
    private void updateDealerHoldingValues() { //Separate funksjoner fordi de skal gi forskjellig tekst, og brukes kun på ett sted
        dealerScoreLabel.setText("Dealer: " + gameLogic.getHoldingValueString(gameLogic.getDealer()));
    }

    @FXML
    private void updatePlayerCards() {
        Person player = gameLogic.getPlayer(); 
        int cardHandSize = gameLogic.getHandSize(player);
        int cardHandSizeIndex = cardHandSize - 1;
        if (cardHandSize == 0) { 
            playerCards.stream().forEach(card -> card.setVisible(false));
            playerLabels.stream().forEach(label -> label.setText("?"));
        } else if (cardHandSize <= 4) {
            playerCards.get(cardHandSizeIndex).setVisible(true);
            playerLabels.get(cardHandSizeIndex*2).setText(gameLogic.getSuitString(player, cardHandSizeIndex));
            playerLabels.get(cardHandSizeIndex*2+1).setText(gameLogic.getValueString(player, cardHandSizeIndex));
        } else {
            playerLabels.get(6).setText(gameLogic.getSuitString(player, cardHandSizeIndex));
            playerLabels.get(7).setText(gameLogic.getValueString(player, cardHandSizeIndex));
        }
    }

    @FXML
    private void updateDealerCardsStartPhase() { 
        int cardHandSize = gameLogic.getHandSize(gameLogic.getDealer());
        if (cardHandSize == 0) {
            dealerCards.stream().forEach(card -> card.setVisible(false));
            dealerLabels.stream().forEach(label -> label.setText("?"));
        } else if (cardHandSize <= 4) {
            dealerCards.stream().filter(card -> dealerCards.indexOf(card) < cardHandSize).forEach(card -> card.setVisible(true));
        } else {
            dealerCards.stream().filter(card -> dealerCards.indexOf(card) < 4).forEach(card -> card.setVisible(true));
        }
    }
    @FXML
    private void updateDealerCardsStandPhase() {
        Person dealer = gameLogic.getDealer();
        int cardHandSize = gameLogic.getHandSize(dealer);
        if (cardHandSize <= 4) {
            for (int i = 0; i < cardHandSize; i++) {
                dealerLabels.get(i*2).setText(gameLogic.getSuitString(dealer, i));
                dealerLabels.get(i*2+1).setText(gameLogic.getValueString(dealer, i));
            }
        } else {
            for (int i = 0; i < 4; i++) {
                dealerLabels.get(i*2).setText(gameLogic.getSuitString(dealer, i));
                dealerLabels.get(i*2+1).setText(gameLogic.getValueString(dealer, i));
            }
            dealerLabels.get(6).setText(gameLogic.getSuitString(dealer, cardHandSize - 1));
            dealerLabels.get(7).setText(gameLogic.getValueString(dealer, cardHandSize - 1));
        }
    }

    @FXML
    private void updateScoreboard() {
        GameHistory gameHistory = gameLogic.getGameHistory();
        overallScore.setText("Player " + String.valueOf(gameHistory.getPlayerWinsInTotal()) + " - " + String.valueOf(gameHistory.getDealerWinsInTotal()) + " Dealer");
        thisGameScore.setText("Player " + String.valueOf(gameHistory.getPlayerWinsThisGame()) + " - " + String.valueOf(gameHistory.getDealerWinsThisGame()) + " Dealer");
        streakScore.setText(gameLogic.getStreakString()); //Egen string-funksjon siden den gjør mer enn å konvertere et tall, til forskjell fra de to under
        totalGamesPlayedScore.setText(String.valueOf(gameLogic.getTotalGames()));
        gamesPlayedThisSessionScore.setText(String.valueOf(gameLogic.getSessionGames()));

    }

}
