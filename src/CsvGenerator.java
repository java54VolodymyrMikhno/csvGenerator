import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CsvGenerator {
    
    private static final int NUMBER_OF_GAMES = 50;
    private static final int NUMBER_OF_GAMERS = 10;
    private static final int MAX_MOVES_PER_GAME = 10;

    public static void main(String[] args) throws IOException {
        List<String> gamesCsv = generateGames();
        List<String> gamersCsv = generateGamers();
        List<String> gameGamersCsv = generateGameGamers();
        List<String> movesCsv = generateMoves();

        writeToFile("games.csv", gamesCsv);
        writeToFile("gamers.csv", gamersCsv);
        writeToFile("game_gamers.csv", gameGamersCsv);
        writeToFile("moves.csv", movesCsv);
    }

    private static List<String> generateGames() {
        List<String> games = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        for (int i = 1; i <= NUMBER_OF_GAMES; i++) {
            LocalDateTime date = LocalDateTime.now().minusDays(i);
            String formattedDate = date.format(formatter);
            String sequence = generateRandomSequence();
            boolean isFinished = i <= 40; 
            games.add(i + "," + formattedDate + "," + sequence + "," + isFinished);
        }
        return games;
    }

    private static List<String> generateGamers() {
        List<String> gamers = new ArrayList<>();
        for (int i = 1; i <= NUMBER_OF_GAMERS; i++) {
            String username = "gamer" + i;
            LocalDate birthdate = LocalDate.of(1990, 1, 1).plusYears(i);
            gamers.add(username + "," + birthdate);
        }
        return gamers;
    }

    private static List<String> generateGameGamers() {
        List<String> gameGamers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 1; i <= NUMBER_OF_GAMES; i++) {
            String username = "gamer" + (rand.nextInt(NUMBER_OF_GAMERS) + 1);
            boolean isWinner = rand.nextBoolean() && (i <= 40);
            gameGamers.add(i + "," + i + "," + username + "," + isWinner);
        }
        return gameGamers;
    }

    private static List<String> generateMoves() {
        List<String> moves = new ArrayList<>();
        Random rand = new Random();
        for (int i = 1; i <= NUMBER_OF_GAMES; i++) {
            int numberOfMoves = rand.nextInt(MAX_MOVES_PER_GAME) + 1;
            for (int j = 1; j <= numberOfMoves; j++) {
                String sequence = generateRandomSequence();
                int bulls = rand.nextInt(5); 
                int cows = rand.nextInt(5 - bulls);
                moves.add(j + "," + sequence + "," + bulls + "," + cows + "," + i);
            }
        }
        return moves;
    }

    private static String generateRandomSequence() {
        return String.valueOf(new Random().ints(0, 10)
                .distinct()
                .limit(4)
                .mapToObj(Integer::toString)
                .reduce("", (a, b) -> a + b));
    }

    private static void writeToFile(String fileName, List<String> lines) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for (String line : lines) {
            writer.write(line + "\n");
        }
        writer.close();
    }
}
