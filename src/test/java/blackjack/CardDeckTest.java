package blackjack;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CardDeckTest {
    private CardDeck deck;
    private List<Card> deckList;
    private List<Card> realDeck;
    private CardHand hand;

    @BeforeEach
    public void setup() {
        deck = new CardDeck();
        deckList = deck.getDeck();
    }

    @Test
    @DisplayName("Tester konstrutøren")
    public void TestContructor() {
        //Sjekker at den har rett størrelse (52)
        //Sjekker at alle kortene i listen i decket, finnes i en vanlig kortstokk
        Assertions.assertEquals(52, deckList.size());
        realDeck = new ArrayList<Card>();
        for (int i = 1; i <= 13; i++) {
            realDeck.add(new Card('S', i));
        }
        for (int i = 1; i <= 13; i++) {
            realDeck.add(new Card('H', i));
        }
        for (int i = 1; i <= 13; i++) {
            realDeck.add(new Card('D', i));
        }
        for (int i = 1; i <= 13; i++) {
            realDeck.add(new Card('C', i));
        }
        Assertions.assertEquals(realDeck.size(), deckList.size()); //Tester indirekte at realDeck også er 52 i størrelse
        //Sjekker om det finnes et kort i deckList med samme verdier som i realDeck, for alle kort i deckList. Sjekker da indirekte at alle kort er forskjellige
        CheckIfTwoListsHasAllTheSameCards(deckList, realDeck);
    }

    @Test
    @DisplayName("Tester at drawCard tar det øverste kortet fra bunken, og legger det i en cardHand")
    public void testDrawCard() {
        hand = new CardHand();
        List<Card> deckWithoutTopCard = new ArrayList<Card>();
        for (int i = 1; i < deckList.size(); i++) {
            deckWithoutTopCard.add(deckList.get(i));
        }
        Card topCard = deckList.get(0);
        deck.drawCard(hand);
        deckList = deck.getDeck(); //Må redefinere listen med kort i deck siden den er endret
        CheckIfTwoListsHasAllTheSameCards(deckList, deckWithoutTopCard);

        //Sjekker at dette øverste kortet er lagt til i hand
        Assertions.assertEquals(topCard.getSuit(), hand.getCard(0).getSuit());
        Assertions.assertEquals(topCard.getValue(), hand.getCard(0).getValue());
    }

    private void CheckIfTwoListsHasAllTheSameCards(List<Card> listToCheck, List<Card> validList) {
        for (Card card : listToCheck) {
            boolean found = false;
            for (Card card2 : validList) {
                if (card.getSuit() == card2.getSuit() && card.getValue() == card2.getValue()) {
                    found = true;
                }
            }
            Assertions.assertTrue(found);
        }
    }
}
