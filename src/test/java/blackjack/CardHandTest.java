package blackjack;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CardHandTest {
    //Ikke poeng i å teste konstruktøren, den er uansett bare en tom Liste
    private CardHand hand;

    @BeforeEach
    private void setup() {
        hand = new CardHand();
    }

    @Test
    @DisplayName("Tester addCard")
    public void TestAddCard() {
        //0 kort
        List<Card> handList = hand.getHand();
        Assertions.assertEquals(0, handList.size());
        //1 kort
        Card card1 = new Card('C', 1);
        hand.addCard(card1);
        handList = hand.getHand();
        Assertions.assertEquals(1, handList.size());
        Assertions.assertEquals(card1, handList.get(0));
        //2 kort
        Card card2 = new Card('C', 2);
        hand.addCard(card2);
        handList = hand.getHand();
        Assertions.assertEquals(2, handList.size());
        Assertions.assertEquals(card2, handList.get(1));
    }


    @Test
    @DisplayName("Tester getCard for riktig kort og ugyldige indexer")
    public void TestGetCard() {
        Card card1 = new Card('C', 1);
        Card card2 = new Card('C', 2);
        hand.getHand().add(card1);
        hand.getHand().add(card2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            hand.getCard(-1);
        }, "Ugyldig index");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            hand.getCard(1000);
        }, "Ugyldig index");

        Assertions.assertEquals(card1, hand.getCard(0));
        Assertions.assertEquals(card2, hand.getCard(1));
    }

    @Test
    @DisplayName("Tester getValueOfHand og sjekker at varierer essene sine verdier til å være under 21, og at 11, 12 og 13 får verdi 10")
    public void TestGetValueOfHand() {
        Card card1 = new Card('C', 1); //Ess teller som 11
        Card card2 = new Card('H', 12); //12 (Dame) teller som 10
        Card card3 = new Card('D', 1); //Esset teller nå som 1
        Card card4 = new Card('S', 1); //Flere ess, begge teller som 1
        Card card5 = new Card('C', 10); //Går over 21
        Card card6 = new Card('C', 13); //13 (Konge) teller som 10
        hand.addCard(card1);
        Assertions.assertEquals(11, hand.getValueOfHand());
        hand.addCard(card2);
        Assertions.assertEquals(21, hand.getValueOfHand());
        hand.addCard(card3);
        Assertions.assertEquals(12, hand.getValueOfHand());
        hand.addCard(card4);
        Assertions.assertEquals(13, hand.getValueOfHand());
        hand.addCard(card5);
        Assertions.assertEquals(23, hand.getValueOfHand());
        hand.addCard(card6);
        Assertions.assertEquals(33, hand.getValueOfHand());
        
    }
}
