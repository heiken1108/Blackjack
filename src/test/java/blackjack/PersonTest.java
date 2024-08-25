package blackjack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersonTest {
    private Person person;

    @Test
    @DisplayName("Tester konstruktør")
    public void TestConstructor() {
        person = new Person();
        Assertions.assertTrue(person.getHand().getHand().size() == 0);
        Card card1 = new Card('C', 10);
        Card card2 = new Card('H', 5);
        CardHand hand = new CardHand();
        hand.addCard(card1);
        hand.addCard(card2);
        person.getHand().addCard(card1);
        person.getHand().addCard(card2);
        Assertions.assertEquals(hand.getHand().size(), person.getHand().getHand().size());
        for (int i = 0; i < hand.getHand().size(); i++) {
            Assertions.assertEquals(hand.getCard(i), person.getHand().getCard(i));
        }
    }

    @Test
    @DisplayName("Tester delegeringsfunksjonene")
    public void TestDelegatingFunctions() {
        person = new Person();
        Card card1 = new Card('C', 10);
        Card card2 = new Card('H', 5);
        person.getHand().addCard(card1);
        person.getHand().addCard(card2);
        Assertions.assertEquals(15, person.getValueOfHand());
        Assertions.assertEquals(2, person.getHandSize());
    }
}
