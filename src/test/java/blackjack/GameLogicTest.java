package blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameLogicTest {
    private GameLogic gameLogic;

    @BeforeEach
    public void setup() {
        gameLogic = new GameLogic();
    }

    @Test
    @DisplayName("Tester konstruktøren")
    public void TestConstructor() {
        //Hva skal testes her inne?
        Assertions.assertEquals(new Person().getClass(), gameLogic.getPlayer().getClass()); //Siden Person-klassen testes i en annen test, trenger man bare sjekke at det lages en Person der det skal
        Assertions.assertEquals(new Person().getClass(), gameLogic.getDealer().getClass()); //Siden Person-klassen testes i en annen test, trenger man bare sjekke at det lages en Person der det skal
        Assertions.assertEquals(new CardDeck().getClass(), gameLogic.getDeck().getClass()); //Siden CardDeck testes i en annen test, trenger man bare sjekke at det lages en CardDeck der det skal
    
        //Må ha mer her, bare vet ikke helt hva
        Assertions.assertEquals(new GameHistory().getClass(), gameLogic.getGameHistory().getClass());
        Assertions.assertEquals(new DealerAI().getClass(), gameLogic.getDealerAI().getClass());

        Assertions.assertEquals(9, gameLogic.getDealerAIChances().size());
        Assertions.assertEquals(5, gameLogic.getGameHistory().getSaveableInformation().size());
    }

    @Test
    @DisplayName("Tester at objektene endrer, og at denne delen av tilstanden da er resatt")
    public void TestResetGame() {
        Person playerOld = gameLogic.getPlayer();
        Person dealerOld = gameLogic.getDealer();
        CardDeck deckOld = gameLogic.getDeck();
        gameLogic.resetGame();
        Person playerNew = gameLogic.getPlayer();
        Person dealerNew = gameLogic.getDealer();
        CardDeck deckNew = gameLogic.getDeck();
        Assertions.assertEquals(playerOld.getClass(), playerNew.getClass());
        Assertions.assertEquals(dealerOld.getClass(), dealerNew.getClass());
        Assertions.assertEquals(deckOld.getClass(), deckNew.getClass());
        Assertions.assertFalse(playerOld.equals(playerNew));
        Assertions.assertFalse(dealerOld.equals(dealerNew));
        Assertions.assertFalse(deckOld.equals(deckNew));
    }

    @Test
    @DisplayName("Tester updateSaveFile og sjekker at man får feil hvis man prøver å legge inn feil mengde verdier") //Typen verdier sjekkes allerede i paramteret. Skriver man inn et feil filnavn lager den en ny fil som ikke nødvendigvis er feil, men den filen kommer bare ikke til å bli brukt senere
    public void TestUpdateSaveFile() { //Bruker egne txtfiler (TestDealerAI.txt og TestGameHistoryFile.txt) for å teste så jeg ikke endrer på de viktige filene
        List<Integer> validValuesDealerAI = Arrays.asList(1,2,3,4,5,6,7,8,9);
        List<Integer> validValuesGameHistory = Arrays.asList(1,2,3,4,5);
        List<Integer> invalidValuesDealerAI = Arrays.asList(1,2,3, -1, 5, 6, 7, 8, 9);
        List<Integer> invalidValuesGameHistory = Arrays.asList(1,2,3,-1, 5);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.updateSavefile("TestDealerAIFile", validValuesGameHistory); //Tester feil størrelsesmatch
        }, "Feil størrelse på updatedInformation");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.updateSavefile("TestGameHistoryFile", validValuesDealerAI); //Tester feil størrelsesmatch
        }, "Feil størrelse på updatedInformation");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.updateSavefile("TestDealerAIFile", invalidValuesDealerAI); //Tester ugyldige verdier
        }, "Ugyldige verdier i updatedInformation");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.updateSavefile("TestGameHistoryFile", invalidValuesGameHistory); //Tester ugyldige verdier
        }, "Ugyldige verdier i updatedInformation");

        //Sjekker at det man legger inn lagres, og blir med når man lager en ny DealerAI eller GameHistory

        gameLogic.updateSavefile("TestDealerAIFile", validValuesDealerAI);
        gameLogic.updateSavefile("TestGameHistoryFile", validValuesGameHistory);

        GameHistory testGameHistory = new GameHistory();
        DealerAI testDealerAI = new DealerAI();
        FileHandler fileHandler = new FileHandler();
        try {
            testGameHistory = fileHandler.createGameHistory("TestGameHistoryFile"); //Funksjon i FileHandler lager enten en standard, eller en som følger fila
            testDealerAI = fileHandler.createDealerAI("TestDealerAIFile");

        } catch (FileNotFoundException e) {
            System.out.println(e);
            
        }

        //Teste om verdiene faktisk blir oppdatert når man oppdaterer. Testen vil slå ut feil om verdiene ikke er oppdatert
        Assertions.assertEquals(validValuesGameHistory, testGameHistory.getSaveableInformation());
        Assertions.assertEquals(validValuesDealerAI, testDealerAI.getChances());
    }

    //Getterene nedenfor testes fordi de tar inn parameter og delegerer nedover og det da er greit å sjekke at det funker med parameteret

    @Test
    @DisplayName("Tester getHoldingValueString")
    public void TestGetHoldingValueString() {
        Person testPerson = gameLogic.getPlayer();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('H', 5);
        Card card3 = new Card('D', 12);
        assertEquals("0", gameLogic.getHoldingValueString(testPerson));
        testPerson.getHand().addCard(card1);
        assertEquals("10", gameLogic.getHoldingValueString(testPerson));
        testPerson.getHand().addCard(card2);
        assertEquals("15", gameLogic.getHoldingValueString(testPerson));
        testPerson.getHand().addCard(card3);
        assertEquals("25", gameLogic.getHoldingValueString(testPerson));
    }

    @Test
    @DisplayName("Tester getHoldingValueInt")
    public void TestGetHoldingValueInt() {
        Person testPerson = gameLogic.getPlayer();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('H', 5);
        Card card3 = new Card('D', 12);
        assertEquals(0, gameLogic.getHoldingValueInt(testPerson));
        testPerson.getHand().addCard(card1);
        assertEquals(10, gameLogic.getHoldingValueInt(testPerson));
        testPerson.getHand().addCard(card2);
        assertEquals(15, gameLogic.getHoldingValueInt(testPerson));
        testPerson.getHand().addCard(card3);
        assertEquals(25, gameLogic.getHoldingValueInt(testPerson));
    }

    @Test
    @DisplayName("Tester getHandSize")
    public void TestGetHandSize() {
        Person testPerson = gameLogic.getPlayer();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('H', 5);
        Card card3 = new Card('D', 12);
        assertEquals(0, gameLogic.getHandSize(testPerson));
        testPerson.getHand().addCard(card1);
        assertEquals(1, gameLogic.getHandSize(testPerson));
        testPerson.getHand().addCard(card2);
        assertEquals(2, gameLogic.getHandSize(testPerson));
        testPerson.getHand().addCard(card3);
        assertEquals(3, gameLogic.getHandSize(testPerson));
    }

    @Test
    @DisplayName("Tester drawCard")
    public void testDrawCard() {
        Person testPerson = gameLogic.getPlayer();
        CardHand testHand = testPerson.getHand();
        CardDeck testDeck = gameLogic.getDeck();
        Card topCard = testDeck.getDeck().get(0);
        Card secondTopCard = testDeck.getDeck().get(1);

        gameLogic.drawCard(testHand);
        Assertions.assertEquals(topCard, testHand.getCard(0));
        gameLogic.drawCard(testHand);
        Assertions.assertEquals(secondTopCard, testHand.getCard(1));
        
    }

    @Test
    @DisplayName("Tester hva getWinner returnerer")
    public void testGetWinner () {
        Person testPlayer = gameLogic.getPlayer();
        Person testDealer = gameLogic.getDealer();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('D', 13);
        Card card3 = new Card('H', 5);
        Card card4 = new Card('H', 1);
        Card card5 = new Card('D', 12);
        Card card6 = new Card('D', 2);
        Card card7 = new Card('C', 11);
        
        
        Assertions.assertEquals("It's a tie", gameLogic.getWinner());
        testPlayer.getHand().addCard(card1);
        Assertions.assertEquals("Player", gameLogic.getWinner());
        testDealer.getHand().addCard(card2);
        Assertions.assertEquals("It's a tie", gameLogic.getWinner());
        testDealer.getHand().addCard(card3);
        Assertions.assertEquals("Dealer", gameLogic.getWinner());
        testPlayer.getHand().addCard(card4);
        Assertions.assertEquals("Player", gameLogic.getWinner());
        testPlayer.getHand().addCard(card5);
        testPlayer.getHand().addCard(card6);
        Assertions.assertEquals("Dealer", gameLogic.getWinner());
        testDealer.getHand().addCard(card7);
        Assertions.assertEquals("It's a tie", gameLogic.getWinner()); //Begge utenfor
    }

    @Test
    @DisplayName("Tester getSuitString")
    public void testGetSuitString() {
        Person testPerson = gameLogic.getPlayer();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('D', 13);

        //0 kort i hånd
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.getSuitString(testPerson, 0);
        }, "Index utenfor");


        //1 kort i hånd
        testPerson.getHand().addCard(card1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.getSuitString(testPerson, 1);
        }, "Index utenfor");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.getSuitString(testPerson, -1);
        }, "Index utenfor");
        Assertions.assertEquals("Clubs", gameLogic.getSuitString(testPerson, 0));
    
        //2 kort i hånd
        testPerson.getHand().addCard(card2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.getSuitString(testPerson, 2);
        }, "Index utenfor");
        Assertions.assertEquals("Clubs", gameLogic.getSuitString(testPerson, 0));
        Assertions.assertEquals("Diamonds", gameLogic.getSuitString(testPerson, 1));
    }

    @Test
    @DisplayName("Tester getValueString")
    public void testGetValueString() {
        Person testPerson = gameLogic.getPlayer();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('D', 13);

        //0 kort i hånd
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.getValueString(testPerson, 0);
        }, "Index utenfor");


        //1 kort i hånd
        testPerson.getHand().addCard(card1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.getValueString(testPerson, 1);
        }, "Index utenfor");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.getValueString(testPerson, -1);
        }, "Index utenfor");
        Assertions.assertEquals("10", gameLogic.getValueString(testPerson, 0));
    
        //2 kort i hånd
        testPerson.getHand().addCard(card2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameLogic.getValueString(testPerson, 2);
        }, "Index utenfor");
        Assertions.assertEquals("10", gameLogic.getValueString(testPerson, 0));
        Assertions.assertEquals("K", gameLogic.getValueString(testPerson, 1));
    }

    @Test
    @DisplayName("Tester tooHighValue")
    public void testTooHighValue() {
        CardHand testHand = gameLogic.getPlayer().getHand();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('D', 13);
        Card card3 = new Card('H', 5);

        Assertions.assertFalse(gameLogic.tooHighValue(testHand));
        testHand.addCard(card1);
        Assertions.assertFalse(gameLogic.tooHighValue(testHand));
        testHand.addCard(card2);
        Assertions.assertFalse(gameLogic.tooHighValue(testHand));
        testHand.addCard(card3);
        Assertions.assertTrue(gameLogic.tooHighValue(testHand));

    }

    @Test
    @DisplayName("Tester getDealerHand") //getDealerHand er baser på tilfeldighet i flere ledd så kan ikke teste akkurat hva jeg forventer at hånda blir. Bare teste verdien innenfor et intervall
    public void testGetDealerHand() {
        gameLogic.getDealerHand();
        Assertions.assertTrue(testIntervallGetDealerHand(gameLogic.getDealer().getHand()));
        
    }
    private boolean testIntervallGetDealerHand(CardHand hand) {
        return (hand.getValueOfHand() >= 12 && hand.getValueOfHand() <= 30 ); //Må få minst 12 i verdi og maks 20+10 i verdi
    }

    @Test
    @DisplayName("Tester getStreakString")
    public void testGetStreakString() { //Må kalle getWinner for å oppdatere streaks siden de update-funksjonene er private
        Person testPlayer = gameLogic.getPlayer();
        Person testDealer = gameLogic.getDealer();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('D', 13);
        Card card3 = new Card('H', 5);
        Card card4 = new Card('H', 1);
        Card card5 = new Card('D', 12);
        Card card6 = new Card('D', 2);
        Card card7 = new Card('C', 11);

        gameLogic.getWinner();
        Assertions.assertEquals("No streaks", gameLogic.getStreakString());
        testPlayer.getHand().addCard(card1);
        gameLogic.getWinner();
        Assertions.assertEquals("Player: 1", gameLogic.getStreakString());
        testDealer.getHand().addCard(card2);
        testDealer.getHand().addCard(card3);
        gameLogic.getWinner();
        Assertions.assertEquals("Dealer: 1", gameLogic.getStreakString());
        testDealer.getHand().addCard(card5);
        testPlayer.getHand().addCard(card6);
        testPlayer.getHand().addCard(card7);
        gameLogic.getWinner();
        Assertions.assertEquals("No streaks", gameLogic.getStreakString());
    }

    @Test
    @DisplayName("Tester resetOverallStandings")
    public void testResetOverallStandings() { //Henter fra main fil her, men spørs hvor grundig man må teste
        gameLogic.resetOverallStandings();
        GameHistory gameHistory = gameLogic.getGameHistory();

        Assertions.assertEquals(0, gameHistory.getPlayerWinsInTotal());
        Assertions.assertEquals(0, gameHistory.getDealerWinsInTotal());
        Assertions.assertEquals(0, gameHistory.getPlayerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getDealerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getPlayerStreak());
        Assertions.assertEquals(0, gameHistory.getDealerStreak());

        gameLogic.getPlayer().getHand().addCard(new Card('C', 12));
        gameLogic.getWinner();

        Assertions.assertEquals(1, gameHistory.getPlayerWinsInTotal());
        Assertions.assertEquals(0, gameHistory.getDealerWinsInTotal());
        Assertions.assertEquals(1, gameHistory.getPlayerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getDealerWinsThisGame());
        Assertions.assertEquals(1, gameHistory.getPlayerStreak());
        Assertions.assertEquals(0, gameHistory.getDealerStreak());

        gameLogic.resetOverallStandings();
        Assertions.assertEquals(0, gameHistory.getPlayerWinsInTotal());
        Assertions.assertEquals(0, gameHistory.getDealerWinsInTotal());
        Assertions.assertEquals(0, gameHistory.getPlayerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getDealerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getPlayerStreak());
        Assertions.assertEquals(0, gameHistory.getDealerStreak());

        //Sjekker at det funker når for Dealer-verdiene også
        gameLogic.getDealer().getHand().addCard(new Card('C', 11));
        gameLogic.getDealer().getHand().addCard(new Card('C', 10));
        gameLogic.getWinner();

        Assertions.assertEquals(0, gameHistory.getPlayerWinsInTotal());
        Assertions.assertEquals(1, gameHistory.getDealerWinsInTotal());
        Assertions.assertEquals(0, gameHistory.getPlayerWinsThisGame());
        Assertions.assertEquals(1, gameHistory.getDealerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getPlayerStreak());
        Assertions.assertEquals(1, gameHistory.getDealerStreak());

        gameLogic.resetOverallStandings();
        Assertions.assertEquals(0, gameHistory.getPlayerWinsInTotal());
        Assertions.assertEquals(0, gameHistory.getDealerWinsInTotal());
        Assertions.assertEquals(0, gameHistory.getPlayerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getDealerWinsThisGame());
        Assertions.assertEquals(0, gameHistory.getPlayerStreak());
        Assertions.assertEquals(0, gameHistory.getDealerStreak());

    }

    

    @Test
    @DisplayName("Tester resetDealerAI")
    public void testResetDealerAI() { //Tester at det blir et nytt objekt, og at denne nye dealerAI-en har standardverdiene
        DealerAI oldDealerAI = gameLogic.getDealerAI();
        gameLogic.resetAI();
        DealerAI newDealerAI = gameLogic.getDealerAI();
        Assertions.assertFalse(oldDealerAI == newDealerAI);
        List<Integer> newDealerAIChances = newDealerAI.getChances();
        List<Integer> standardValue = Arrays.asList(95, 90, 85, 80, 60, 40, 20, 10, 5);
        Assertions.assertEquals(standardValue, newDealerAIChances);
    }

    @Test
    @DisplayName("Tester resetGamesPlayed, increaseGamesPlayed, getTotalGames og getSessionsGames samtidig da de kan alle kan testes med samme prosess")
    public void testResetGamesPlayed() {
        gameLogic.resetGamesPlayed();
        
        testResetGamesPlayedValues(0, gameLogic);

        gameLogic.increaseGamesPlayed();

        testResetGamesPlayedValues(1, gameLogic);

        gameLogic.resetGamesPlayed();

        testResetGamesPlayedValues(0, gameLogic);

        for (int i = 0; i < 5; i++) {
            gameLogic.increaseGamesPlayed();
        }

        testResetGamesPlayedValues(5, gameLogic);

        gameLogic.resetGamesPlayed();

        testResetGamesPlayedValues(0, gameLogic);
    }

    private void testResetGamesPlayedValues(int expected, GameLogic gameLogic) {
        Assertions.assertEquals(expected, gameLogic.getTotalGames());
        Assertions.assertEquals(expected, gameLogic.getSessionGames());
    }
    
}
