package blackjack;

import java.io.FileNotFoundException;
import java.util.List;

public interface IFileHandler {
    
    DealerAI createDealerAI(String filename) throws FileNotFoundException; 

    GameHistory createGameHistory(String filename) throws FileNotFoundException; 

    void updateFile(String filename, List<Integer> updatedInformation) throws FileNotFoundException;

}
