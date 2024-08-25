package blackjack;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameHistoryTest {
    private GameHistory gameHistory;

    //Bruker ikke beforeEach fordi jeg skal lage forskjellige konstruktører

    @Test
    @DisplayName("Tester standardkonstruktøren")
    public void TestStandardConstructor() {
        gameHistory = new GameHistory();
        Assertions.assertEquals(0, gameHistory.getPlayerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getDealerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getGamesPlayedThisSession());
        Assertions.assertEquals(5, gameHistory.getSaveableInformation().size());
        Assertions.assertEquals(Arrays.asList(0,0,0,0,0), gameHistory.getSaveableInformation());
    }

    @Test
    @DisplayName("Tester den modifiserbare konstruktøren")
    public void TestModifiableConstructor() {
        List<Integer> values = Arrays.asList(1,2,3,4,5);
        List<Integer> invalidValues = Arrays.asList(-1,2,3,4,5);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameHistory = new GameHistory(invalidValues);
        }, "Kan ikke lage gameHistory med ugyldige verdier");
        gameHistory = new GameHistory(values);
        Assertions.assertEquals(0, gameHistory.getPlayerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getDealerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getGamesPlayedThisSession());
        Assertions.assertEquals(5, gameHistory.getSaveableInformation().size());
        Assertions.assertEquals(values, gameHistory.getSaveableInformation());
    }

    @Test
    @DisplayName("Tester de tre setterene med enkeltverdier for feilmeldinger")
    public void TestSingleValueSetters() { 
        gameHistory = new GameHistory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            gameHistory.setPlayerWinsThisGame(-1);
        }, "Kan ikke sette negativ verdi");
        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            gameHistory.setDealerWinsThisGame(-1);
        }, "Kan ikke sette negativ verdi");
        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            gameHistory.setGamesPlayedThisSession(-1);
        }, "Kan ikke sette negativ verdi");

        gameHistory.setPlayerWinsThisGame(1);
        Assertions.assertEquals(1, gameHistory.getPlayerWinsThisGame());
        gameHistory.setDealerWinsThisGame(2);
        Assertions.assertEquals(2, gameHistory.getDealerWinsThisGame());
        gameHistory.setGamesPlayedThisSession(3);
        Assertions.assertEquals(3, gameHistory.getGamesPlayedThisSession());
    }

    @Test
    @DisplayName("Tester setterene til saveableInformation-listen")
    public void TestSaveableInformationSetters() {
        gameHistory = new GameHistory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            gameHistory.setPlayerWinsInTotal(-1);
        }, "Kan ikke sette negativ verdi");
        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            gameHistory.setDealerWinsInTotal(-1);
        }, "Kan ikke sette negativ verdi");
        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            gameHistory.setPlayerStreak(-1);
        }, "Kan ikke sette negativ verdi");
        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            gameHistory.setDealerStreak(-1);
        }, "Kan ikke sette negativ verdi");
        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            gameHistory.setTotalGamesPlayed(-1);
        }, "Kan ikke sette negativ verdi");

        gameHistory.setPlayerWinsInTotal(4);
        gameHistory.setDealerWinsInTotal(5);
        gameHistory.setPlayerStreak(6);
        gameHistory.setDealerStreak(7);
        gameHistory.setTotalGamesPlayed(8);
        Assertions.assertEquals(Arrays.asList(4,5,6,7,8), gameHistory.getSaveableInformation());
    }
}
