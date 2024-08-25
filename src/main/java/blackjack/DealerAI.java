package blackjack;

import java.util.Arrays;
import java.util.List;

public class DealerAI {
    private static int changePercentage = 1;
    private List<Integer> chances;

    public DealerAI() { //Konstruktør når det er noe galt med fillagringen
        this.chances = Arrays.asList(95, 90, 85, 80, 60, 40, 20, 10, 5);
                                     //12  13  14  15  16  17  18  19  20
    }
    public DealerAI(List<Integer> info) {  //Konstruktør når det ikke er noe galt med fillagringen
        //At info sin størrelse alltid matcher chances sin størrelse sjekkes i FileHandler som initialiserer denne konstruktøren
        for (Integer integer : info) {
            if (integer < 1 || integer > 99) {
                throw new IllegalArgumentException("Invalid value in info");
            }
        }
        this.chances = info;
    }

    public void setValuesToAbsolutesToUseInTesting() {
        //Denne brukes kun til å teste med absolutte verdier. Den sørger for at det hvertfall bare settes de verdiene jeg bestemmer, så den ikke kan sette noen verdier som vil utløse problemer
        chances = Arrays.asList(100, 100, 100, 100, 100, 0, 0, 0, 0);
    }

    public List<Integer> getChances() {
        return chances; //Denne er bare for å teste
    }

    public boolean decideDraw(Person dealer) {
        int dealerHandValue = dealer.getHand().getValueOfHand();
        if (dealerHandValue < 12) {
            return true;
        } else if (dealerHandValue < 21) {
            int chancesIndex = dealerHandValue - 12;
            double randNumber = Math.random() * 100;
            return (randNumber < chances.get(chancesIndex));
        } else {
            return false;
        }
    }
    public void updateChances(int atNumber, boolean result) { //result = true betyr opp, result = false betyr ned
        if (atNumber < 12 || atNumber > 20) {
            throw new IllegalArgumentException("Can't update that value");
        }
        int index = atNumber - 12;
            if (result && chances.get(index) < 100-changePercentage) { //Skal alltid være en mulighet for at den kan klatre seg opp eller gå nedover igjen. Skal unngå 0% eller 100% sjanse
                chances.set(index, chances.get(index) + changePercentage);
            } else if (!result && chances.get(index) > changePercentage) {
                chances.set(index, chances.get(index) - changePercentage);
            }
    }
    
}
