package blackjack;

public class Card {
    private char suit;
    private int value;

    public Card(char suit, int value) {
        if (!validSuit(suit) || !validValue(value)) {
            throw new IllegalArgumentException("Illegal suit or value");
        }
        this.suit = suit;
        this.value = value;
    }

    public char getSuit() {
        return suit;
    }
    public int getValue() {
        return value;
    }

    private boolean validSuit(char suit) {
        return (suit == 'H' || suit == 'C' || suit == 'D' || suit == 'S'); //H = Hearts, C = Clubs, D = Diamonds, S = Spades
    }
    private boolean validValue(int value) {
        return (value >= 1 && value <= 13); //Ess defineres ved verdi 1
    }
}
