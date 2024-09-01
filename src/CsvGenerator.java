import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CsvGenerator {

    private static final int NUMBER_OF_GAMES = 50;
    private static final int NUMBER_OF_GAMERS = 10;
    private static final int MAX_MOVES_PER_GAME = 10;
    private static Map<Integer, Boolean> gameFinished = new HashMap<>();
    private static Map<Integer, String> gameWinners = new HashMap<>();

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
        for (int i = 1; i <= NUMBER_OF_GAMES; i++) {
            LocalDateTime date = LocalDateTime.now().minusDays(i);
            String sequence = generateRandomSequence();
            boolean isFinished = gameFinished.getOrDefault(i, false);
            games.add(i + "," + date + "," + sequence + "," + isFinished);
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
            int numGamers = rand.nextInt(NUMBER_OF_GAMERS) + 1;
            for (int j = 0; j < numGamers; j++) {
                String username = "gamer" + (rand.nextInt(NUMBER_OF_GAMERS) + 1);
                boolean isWinner = username.equals(gameWinners.get(i));
                gameGamers.add(i + "," + i + "," + username + "," + isWinner);
            }
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
                boolean isWinningMove = bulls == 4;
                if (isWinningMove) {
                    gameFinished.put(i, true);
                    gameWinners.put(i, "gamer" + (rand.nextInt(NUMBER_OF_GAMERS) + 1));
                }
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
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (String line : lines) {
                writer.println(line);
            }
        }
    }
}
