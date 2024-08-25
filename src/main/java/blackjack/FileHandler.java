package blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler implements IFileHandler {

    @Override
    public DealerAI createDealerAI(String filename) throws FileNotFoundException {
        List<Integer> info = getInformationFromFile(filename);
        DealerAI tmpAI = new DealerAI();
        if (info.size() != tmpAI.getChances().size()) {//Standardstørrelsen er 9
            return new DealerAI();
        }
        return new DealerAI(info);
    }

    @Override
    public GameHistory createGameHistory(String filename) throws FileNotFoundException {
        List<Integer> info = getInformationFromFile(filename);
        GameHistory tmpHistory = new GameHistory();
        if (info.size() != tmpHistory.getSaveableInformation().size()) {//Standardstørrelsen er 5)
            return new GameHistory();
        }
        return new GameHistory(info);
    }

    @Override
    public void updateFile(String filename, List<Integer> updatedInformation) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(getFile(filename))) {
            for (Integer integer : updatedInformation) {
                writer.println(Integer.toString(integer));
            }
        }
        
    }

    private List<Integer> getInformationFromFile(String filename) throws FileNotFoundException { 
        List<Integer> information = new ArrayList<Integer>();
        try (Scanner scanner = new Scanner(getFile(filename))) {
            while (scanner.hasNextLine()) {
                try {
                    information.add(Integer.parseInt(scanner.nextLine()));
                } catch (Exception e) {
                    return new ArrayList<Integer>();
                }
            }
        }
        return information;
    } 

    public static File getFile(String filename) { //Trenger at den er public til testing
        return new File("src/main/resources/blackjack/savefiles/" + filename + ".txt");
    }
}
