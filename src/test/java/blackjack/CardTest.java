package blackjack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CardTest {

    @Test
    @DisplayName("Tester konstruktøren")
    public void TestContructor() {
        //Tester at den vet når hvilke verdier som er lovlige
        Assertions.assertFalse(validCard('A', 1));
        Assertions.assertFalse(validCard('C', 14));
        Assertions.assertFalse(validCard('C', 0));
        Assertions.assertFalse(validCard('A', 0));
        Assertions.assertTrue(validCard('C', 1));
        Assertions.assertTrue(validCard('C', 13));
        Assertions.assertTrue(validCard('D', 7));

        //Tester om lovlige kort får verdiene sine
        testCard(new Card('C', 1), 'C', 1);
        testCard(new Card('D', 5), 'D', 5);
        testCard(new Card('D', 13), 'D', 13);
        testCard(new Card('H', 2), 'H', 2);

        //Tester om ulovlige kort får riktig feiltype
        Assertions.assertThrows(IllegalArgumentException.class, () -> { //https://howtodoinjava.com/junit5/expected-exception-example/
            new Card('U', 2);
        }, "Ugyldig sort");

        Assertions.assertThrows(IllegalArgumentException.class, () -> { 
            new Card('C', 14);
        }, "Ugyldig nummer");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Card('C', 0);
        }, "Ugyldig nummer");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Card('U', 0);
        }, "Ugyldig sort og nummer");
    }


    private boolean validCard(char suit, int value) {
        return ((suit == 'H' || suit == 'C' || suit == 'D' || suit == 'S') && (value >= 1 && value <= 13));
    }

    private void testCard(Card card, char suit, int value) {
        Assertions.assertEquals(card.getSuit(), suit);
        Assertions.assertEquals(card.getValue(), value);
    }
}
