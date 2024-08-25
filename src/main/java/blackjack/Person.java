package blackjack;

public class Person {
    private CardHand hand;

    public Person() {
        hand = new CardHand();
    }

    public CardHand getHand() {
        return hand;
    }

    public int getValueOfHand() {
        return hand.getValueOfHand();
    }

    public int getHandSize() {
        return hand.getHandSize();
    }
}
