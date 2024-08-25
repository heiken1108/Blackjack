package blackjack;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FileHandlerTest {
    private static FileHandler filehandler = new FileHandler();
    private DealerAI dealerAI;
    private GameHistory gameHistory;

    @BeforeEach
    public void setup() {
        dealerAI = null;
        gameHistory = null; //skal man ha dette?
    }
    

    @Test
    @DisplayName("Tester createDealerAI")
    public void testCreateDealerAI() {
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            dealerAI = filehandler.createDealerAI("hey");
        }, "Finner ikke filen");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dealerAI = filehandler.createDealerAI("InvalidValuesDealerAIFile");
        }, "Ugyldige verdier i fila");

        //Sjekke at en standard DealerAI har samme verdier som når man lager en dealerAI med feil mengde verdier i fil
        DealerAI standardDealerAI = new DealerAI();
        try {
            dealerAI = filehandler.createDealerAI("TestGameHistoryFile");
        } catch (FileNotFoundException e) {
            fail("Finner ikke filen");
            return;
        }
        Assertions.assertEquals(standardDealerAI.getChances(), dealerAI.getChances());
        Assertions.assertTrue(dealerAI != null);

        //Sjekke at en standard DealerAI har samme verdier som når man lager en dealerAI med verdier i fil som ikke alle er tall
        try {
            dealerAI = filehandler.createDealerAI("LetterInDealerAIFile");
        } catch (FileNotFoundException e) {
            fail("Finner ikke filen");
            return;
        }
        Assertions.assertEquals(standardDealerAI.getChances(), dealerAI.getChances());
        Assertions.assertTrue(dealerAI != null);

        try {
            dealerAI = filehandler.createDealerAI("TestDealerAIFile");
        } catch (Exception e) {
            fail("Finner ikke filen");
            return;
        }
        Assertions.assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9), dealerAI.getChances());
        Assertions.assertTrue(dealerAI != null);
        
        

    }

    @Test
    @DisplayName("Tester createGameHistory")
    public void testCreateGameHistory() {
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            gameHistory = filehandler.createGameHistory("hey");
        }, "Finner ikke filen");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameHistory = filehandler.createGameHistory("InvalidValuesGameHistoryFile");
        }, "Ugyldige verdier i fila");

        //Sjekke at en standard GameHistory har samme verdier som når man lager en gameHistory med feil mengde verdier i fil
        GameHistory standardGameHistory = new GameHistory();
        try {
            gameHistory = filehandler.createGameHistory("TestDealerAIFile");
        } catch (FileNotFoundException e) {
            fail("Finner ikke filen");
            return;
        }
        Assertions.assertEquals(standardGameHistory.getSaveableInformation(), gameHistory.getSaveableInformation());
        Assertions.assertTrue(gameHistory != null);

        //Sjekke at en standard GameHistory har samme verdier som når man lager en gameHistory med verdier i fil som ikke alle er tall
        try {
            gameHistory = filehandler.createGameHistory("LetterInGameHistoryFile");
        } catch (FileNotFoundException e) {
            fail("Finner ikke filen");
            return;
        }
        Assertions.assertEquals(standardGameHistory.getSaveableInformation(), gameHistory.getSaveableInformation());
        Assertions.assertTrue(gameHistory != null);

        try {
            gameHistory = filehandler.createGameHistory("TestGameHistoryFile");
        } catch (Exception e) {
            fail("Finner ikke filen");
            return;
        }
        Assertions.assertEquals(Arrays.asList(1,2,3,4,5), gameHistory.getSaveableInformation());
        Assertions.assertTrue(gameHistory != null);
    }

    @Test
    @DisplayName("Tester updateFile for DealerAI")
    public void testUpdateFileForDealerAI() { //Hentet inspirasjon til byte[]-delen fra todolist-example i students-repoet
        List<Integer> newInfo = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90);
        //Oppdaterer en fil som finnes
        try {
            filehandler.updateFile("TestDealerAIFile", newInfo);
        } catch (Exception e) {
            fail("Klarte ikke å oppdatere filen");
        }

        try { //Denne lager en ny fil, og skal brukes til å sammenligne med den gamle filen jeg har oppdatert over
            filehandler.updateFile("NewTestDealerAIFile", newInfo);
        } catch (FileNotFoundException e) {
            fail("Klarte ikke å lagre filen");
        }

        byte[] testDealerAIFile = null, newTestDealerAIFile = null;

        //Gjør filinnholdet om til byte[]
        try {
            testDealerAIFile = Files.readAllBytes(FileHandler.getFile("TestDealerAIFile").toPath());
        } catch (IOException e) {
            fail("Klarte ikke laste inn testfilen");
        }
        try {
            newTestDealerAIFile = Files.readAllBytes(FileHandler.getFile("NewTestDealerAIFile").toPath());
        } catch (Exception e) {
            fail("Klarte ikke laste inn den nye testfilen");
        }

        //Sjekker at innholdet er likt ved å konvetere bytene til Strings for å få en string med hvert av innholdene
        String testDealerAIFileContent = new String(testDealerAIFile, StandardCharsets.UTF_8);
        String newTestDealerAIFileContent = new String(newTestDealerAIFile, StandardCharsets.UTF_8);
        Assertions.assertEquals(newTestDealerAIFileContent, testDealerAIFileContent);
        Assertions.assertNotNull(testDealerAIFile);

        //Reverserer verdien til testfilen igjen før testen er over, og sjekker at de nå har forskjellige verdier
        try {
            filehandler.updateFile("TestDealerAIFile", Arrays.asList(1,2,3,4,5,6,7,8,9)); 
        } catch (Exception e) {
            fail("Klarte ikke oppdatere testfilen");
        }
        try {
            testDealerAIFile = Files.readAllBytes(FileHandler.getFile("TestDealerAIFile").toPath());
        } catch (IOException e) {
            fail("Klarte ikke laste inn testfilen");
        }
        testDealerAIFileContent = new String(testDealerAIFile, StandardCharsets.UTF_8);
        Assertions.assertNotEquals(newTestDealerAIFileContent, testDealerAIFileContent);
        Assertions.assertNotNull(testDealerAIFile);
    }

    @Test
    @DisplayName("Tester updateFile for GameHistory")
    public void testUpdateFileForGameHistory() { //Gjør det samme som i testUpdateFileForDealerAI
        List<Integer> newInfo = Arrays.asList(10, 20, 30, 40, 50);
        try {
            filehandler.updateFile("TestGameHistoryFile", newInfo);
        } catch (Exception e) {
            fail("Klarte ikke å oppdatere filen");
        }
        try {
            filehandler.updateFile("NewTestGameHistoryFile", newInfo);
        } catch (FileNotFoundException e) {
            fail("Klarte ikke å lagre filen");
        }

        byte[] testGameHistoryFile = null, newTestGameHistoryFile = null;

        try {
            testGameHistoryFile = Files.readAllBytes(FileHandler.getFile("TestGameHistoryFile").toPath());
        } catch (IOException e) {
            fail("Klarte ikke laste inn testfilen");
        }
        try {
            newTestGameHistoryFile = Files.readAllBytes(FileHandler.getFile("NewTestGameHistoryFile").toPath());
        } catch (Exception e) {
            fail("Klarte ikke laste inn den nye testfilen");
        }

        String testGameHistoryFileContent = new String(testGameHistoryFile, StandardCharsets.UTF_8);
        String newTestGameHistoryFileContent = new String(newTestGameHistoryFile, StandardCharsets.UTF_8);
        Assertions.assertEquals(newTestGameHistoryFileContent, testGameHistoryFileContent);
        Assertions.assertNotNull(testGameHistoryFileContent);
        try {
            filehandler.updateFile("TestGameHistoryFile", Arrays.asList(1,2,3,4,5)); 
        } catch (Exception e) {
            fail("Klarte ikke oppdatere testfilen");
        }
        try {
            testGameHistoryFile = Files.readAllBytes(FileHandler.getFile("TestGameHistoryFile").toPath());
        } catch (IOException e) {
            fail("Klarte ikke laste inn testfilen");
        }
        testGameHistoryFileContent = new String(testGameHistoryFile, StandardCharsets.UTF_8);
        Assertions.assertNotEquals(newTestGameHistoryFileContent, testGameHistoryFileContent);
        Assertions.assertNotNull(testGameHistoryFile);
    }

    @AfterAll
    static void deleteNewFiles() {
        File newTestDealerAIFile = FileHandler.getFile("NewTestDealerAIFile");
        newTestDealerAIFile.delete();
        File newTestGameHistoryFile = FileHandler.getFile("NewTestGameHistoryFile");
        newTestGameHistoryFile.delete();
    }
}
