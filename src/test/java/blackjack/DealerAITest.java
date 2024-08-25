package blackjack;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DealerAITest {
    private DealerAI dealerAI;

    //Bruker ikke beforeEach fordi jeg skal lage forskjellige konstruktører

    @Test
    @DisplayName("Tester standardkonstruktøren")
    public void TestStandardConstructor() {
        List<Integer> invalidValues = Arrays.asList(-1,2,3,4,5,6,7,8,9);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dealerAI = new DealerAI(invalidValues);
        }, "Kan ikke lage DealerAI med ugyldige verdier");
        dealerAI = new DealerAI();
        List<Integer> chances = dealerAI.getChances();
        List<Integer> standardValues = Arrays.asList(95, 90, 85, 80, 60, 40, 20, 10, 5);
        Assertions.assertEquals(9, chances.size());
        Assertions.assertEquals(standardValues, chances);
    }

    @Test
    @DisplayName("Tester den modifiserbare konstruktøren")
    public void TestModifiableConstructor() {
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        dealerAI = new DealerAI(values);
        List<Integer> chances = dealerAI.getChances();
        Assertions.assertEquals(9, chances.size());
        Assertions.assertEquals(values, chances);
    }

    @Test
    @DisplayName("Tester decideDraw, som avgjør om dealeren skal trekke")
    public void TestDecideDraw() { 
        dealerAI = new DealerAI();
        // Setter verdier til 100 eller 0 for å ikke ha tilfeldigheter. Vanligvis baseres sjansen for at den skal trekke, på sjansen ved hver verdi innenfor 12-20, ved hjelp av chances
        //Siden det er innkapsling i DealerAI som sier at verdiene må være mellom 1-99, så lager jeg en egen funksjon i DealerAI som kan sette verdiene til 100 og 0, men som KUN BRUKES AV DENNE TESTEREN
        //Setter sjansen for at den trekker ved en verdi på 12=100%, 13=100%, 14=100%, 15=100%, 16=100%, 17=0%, 18=0%, 19=0%, 20=0% 
        //Denne funksjonen som gjør dette kalles bare her
       
        dealerAI.setValuesToAbsolutesToUseInTesting();
        Person dealer = new Person();
        CardHand hand = dealer.getHand();
        hand.addCard(new Card('C', 10)); //Verdien til hånda er 10. Skal da trekke uansett siden det er under 12
        Assertions.assertTrue(dealerAI.decideDraw(dealer));
        hand.addCard(new Card('C', 4)); //Verdien til hånda er nå 14. Skal da trekke siden sjansen er satt til 100%
        Assertions.assertTrue(dealerAI.decideDraw(dealer));
        hand.addCard(new Card('C', 5)); //Verdien til hånda er 19. Skal da ikke trekke kort siden sjansen er satt til 0% 
        Assertions.assertFalse(dealerAI.decideDraw(dealer));
        hand.addCard(new Card('C', 6)); //Verdien til hånda er 25. Skal da ikke trekke uansett siden verdien er over 20
        Assertions.assertFalse(dealerAI.decideDraw(dealer));
    }

    @Test
    @DisplayName("Tester updateChances, både ved godt utfall (true) og dårlig utfall (false), og når verdien har truffet grenseverdiene 1 og 99")
    public void TestUpdateChances() {
        List<Integer> values = Arrays.asList(50, 50, 50, 50, 50, 50, 50, 1, 99);
        DealerAI dealerAI = new DealerAI(values);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dealerAI.updateChances(11, true);
        }, "Kan bare oppdatere for verdiene 12-20");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dealerAI.updateChances(21, false);
        }, "Kan bare oppdatere for verdiene 12-20");

        dealerAI.updateChances(12, true);
        Assertions.assertEquals(Arrays.asList(51, 50, 50, 50, 50, 50, 50, 1, 99), dealerAI.getChances());
        dealerAI.updateChances(13, false);
        Assertions.assertEquals(Arrays.asList(51, 49, 50, 50, 50, 50, 50, 1, 99), dealerAI.getChances());
        dealerAI.updateChances(19, false); //Prøver å senke fra 1 til 0, men det skal ikke gjøre noe. Den skal holde seg på 1
        Assertions.assertEquals(Arrays.asList(51, 49, 50, 50, 50, 50, 50, 1, 99), dealerAI.getChances());
        dealerAI.updateChances(20, true); //Prøver å heve fra 99 til 100, men det skal ikke gjøre noe. Den skal holde seg på 99
        Assertions.assertEquals(Arrays.asList(51, 49, 50, 50, 50, 50, 50, 1, 99), dealerAI.getChances());
    }

}
