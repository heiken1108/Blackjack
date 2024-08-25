package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CardDeck {
    private List<Card> deck = new ArrayList<Card>();

    public CardDeck() {
        List<Character> colors = Arrays.asList('S', 'H', 'D', 'C');
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= 13; j++) {
                Card card = new Card(colors.get(i), j);
                deck.add(card);
            }
        }
        randomShuffle();
    }

    public void drawCard(CardHand hand) {
        if (hand == null) {
            throw new IllegalArgumentException("The drawing hand is null");
        }
        Card drawnCard = deck.remove(0);
        hand.addCard(drawnCard);
    }

    private void randomShuffle() {
        List<Card> tmpDeck = deck;
        List<Card> newDeck = new ArrayList<Card>();
        while (tmpDeck.size() > 0) {
            Random rand = new Random();
            Card tmpCard = tmpDeck.remove(rand.nextInt(tmpDeck.size())); //https://www.baeldung.com/java-random-list-element
            newDeck.add(tmpCard);
        }
        this.deck = newDeck;
    }

    public List<Card> getDeck() { //Brukes kun til tester
        return deck;
    }
}
