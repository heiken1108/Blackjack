package blackjack;

import java.util.ArrayList;
import java.util.List;

public class CardHand {
    private List<Card> hand;

    public CardHand() {
        this.hand = new ArrayList<Card>();
    }

    public void addCard(Card card) { //Er ikke mulig å lage tomme kort, så trenger ikke innkapsling for det
        hand.add(card);
    }

    public int getHandSize() { 
        return hand.size();
    }

    public Card getCard(int i) {
        if (i < 0 || i >= hand.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        return hand.get(i);
    }

    public int getValueOfHand() {
        int s = 0;
        int amountOfAces = 0;
        for (Card card : hand) {
            int cardValue = card.getValue();
            if (cardValue > 10) {
                s += 10;
            } else { 
                s += cardValue;
            }
            if (cardValue == 1) {
                amountOfAces += 1;
            }
        }
        while (amountOfAces > 0 && s+10 <= 21) {
            s += 10;
            amountOfAces -= 1;
        }
        return s;
    }

    public List<Card> getHand() { //Kun til tester
        return hand;
    }
}
