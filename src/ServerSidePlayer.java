import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSidePlayer extends Thread {

    ServerSidePlayer opponent;
    ServerSidePlayer currentPlayer;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    String player;
    String currentPlayerName;


    ServerSideGame game;
    private MultiWriter multiWriter;
    Database database;
    int score;
    int currentScore = 0;
    boolean playerEnteredName = false;
    boolean playerAnsweredQuestions = false;
    boolean playerReadyToPlay = false;
    static String category; // går det bra att göra denna variabel statisk? Hur ska den påverka om man spelar flera par???
    static String question; // går det bra att göra denna variabel statisk? Hur ska den påverka om man spelar flera par???
    static StringBuilder builderWithAnswers; // går det bra att göra denna variabel statisk? Hur ska den påverka om man spelar flera par???
    static boolean drawnNextQuestion = false;
    String myPoints;

    public int getCurrentScore() {
        return currentScore;
    }

    public ServerSidePlayer(Socket socket, String player, ServerSideGame game, MultiWriter multiWriter, Database database) throws IOException {
        this.socket = socket;
        this.player = player;
        this.game = game;
        this.multiWriter = multiWriter;
        this.database = database;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            multiWriter.addWriter(output);
            output.println("WELCOME " + player); // skickas till klienten
            output.println("WAITING " + player); // skickas till klienten
            System.out.println("Waiting for opponent to connect.");
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }

    public ServerSideGame getGame() {
        return game;
    }

    public void setGame(ServerSideGame game) {
        this.game = game;
    }

    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }

    public ServerSidePlayer getOpponent() {
        return opponent;
    }

    public ServerSidePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ServerSidePlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public synchronized void run() {
        try {
            output.println("PLAYERS_CONNECTED");
            System.out.println("All players connected");

            String fromClient, toClient;
            while ((fromClient = input.readLine()) != null) { // kommunicerar med klienten

                if (fromClient.startsWith("ENTERED_NAME ")) {
                    player = fromClient.substring(13);
                    System.out.println(player + " has entered their name.");// för att kontrollera att det fungerar korrekt
                    this.playerEnteredName = true;
                    if (playerEnteredName && this.getOpponent().playerEnteredName) {
                        System.out.println("ENTERED_NAME_BOTH");// för att kontrollera att det fungerar korrekt
                        toClient = "ENTERED_NAME_BOTH";
                        for (PrintWriter writer : multiWriter.getWriters()) {
                            writer.println(toClient);
                        }
                    }
                }

                else if (fromClient.startsWith("MY_NAME ")) {
                    currentPlayerName = fromClient.substring(8);
                    for (PrintWriter writer : multiWriter.getWriters()) {
                        writer.println("MY_NAME " + player + " " + currentPlayerName);
                    }
                    System.out.println(currentPlayerName);
                }

                else if (fromClient.startsWith("READY_TO_PLAY ")) {
                    player = fromClient.substring(14);
                    System.out.println(player + " is ready to play.");// för att kontrollera att det fungerar korrekt
                    this.playerReadyToPlay = true;
                    output.println("READY_TO_PLAY");
                    //Används inte?:
                //    if (playerReadyToPlay && this.getOpponent().playerReadyToPlay) {
                //        System.out.println("READY_TO_PLAY_BOTH");// för att kontrollera att det fungerar korrekt
                //       toClient = "READY_TO_PLAY_BOTH";
                //        for (PrintWriter writer : multiWriter.getWriters()) {
                //            writer.println(toClient);
                //       }
                //    }
                }

                else if (fromClient.startsWith("CHOOSING_CATEGORY ")) {
                    for (PrintWriter writer : multiWriter.getWriters()) {
                        writer.println(fromClient);
                    }
                }

                else if (fromClient.startsWith("I_CHOSE ")) {
                    category = fromClient.substring(8);
                    for (PrintWriter writer : multiWriter.getWriters()) {
                        writer.println(fromClient);
                    }
                    question = game.getQuestionText(category);
                    System.out.println(question);// för att kontrollera att det fungerar korrekt
                    List<String> answers = game.getAnswersText(category); // använda direkt metod       !!!!!!!!!!!!!!!!
                    String rightAnswer = database.getCorrectAnswer(category); // NYTT (Det korrekta svaret)
                    builderWithAnswers = new StringBuilder();
                    for (String answer : answers) {
                        builderWithAnswers.append(answer).append(",");
                    }
                    builderWithAnswers.append(rightAnswer);           //NYTT
                    System.out.println(builderWithAnswers);// för att kontrollera att det fungerar korrekt
                }

                else if (fromClient.startsWith("READY_TO_ANSWER ")) {
                    System.out.println(fromClient);
                    output.println(fromClient);
                }

                else if (fromClient.startsWith("QUESTION? ")) {
                    System.out.println(category);
                    output.println("QUESTION: " + question);
                    output.println("ANSWERS: " + builderWithAnswers);
                }

                else if (fromClient.startsWith("NEXT_QUESTION? ")) {
                    System.out.println(drawnNextQuestion);
                    if (!drawnNextQuestion){
                        question = game.getQuestionText(category);
                        System.out.println(question);// för att kontrollera att det fungerar korrekt
                        List<String> answers = game.getAnswersText(category);
                        String rightAnswer = database.getCorrectAnswer(category); // NYTT (Det korrekta svaret)
                        builderWithAnswers.delete(0,builderWithAnswers.length()); // VIKTIGT! Tömmar StringBuilder (ta bort gamla svar)
                        for (String answer : answers) {
                            builderWithAnswers.append(answer).append(",");
                        }
                        builderWithAnswers.append(rightAnswer);           //NYTT
                        System.out.println(builderWithAnswers);// för att kontrollera att det fungerar korrekt
                    }
                    output.println("NEXT_QUESTION: " + question);
                    output.println("NEXT_ANSWERS: " + builderWithAnswers);

                    if (!drawnNextQuestion) { // VIKTIGT för att det ska ändras i drawnNextQuestion på rätt sätt
                        drawnNextQuestion = true;
                    }
                    else
                        drawnNextQuestion = false;
                }

                else if (fromClient.startsWith("BACK_TO_RESULTS ")) {
                    String playerInfoNumber = fromClient.substring(16,23);
                    myPoints = fromClient.substring(24);
                    System.out.println(playerInfoNumber + " " + myPoints);// för att kontrollera att det fungerar korrekt
                    this.playerAnsweredQuestions = true;
                    output.println("WAIT");

                    for (PrintWriter writer : multiWriter.getWriters()) {
                        writer.println("POINTS: " + playerInfoNumber + " " + myPoints);
                    }

                    if (playerAnsweredQuestions && this.getOpponent().playerAnsweredQuestions) {
                        System.out.println("Both players have answered questions.");// för att kontrollera att det fungerar korrekt
                        toClient = "ANSWERED_QUESTIONS_BOTH";
                        for (PrintWriter writer : multiWriter.getWriters()) {
                            writer.println(toClient);
                        }
                    }
                }

                else if (fromClient.startsWith("SHOW_ME_RESULTS ")) {
                    output.println("SHOW_RESULTS");
                }
            }
        } catch (RuntimeException ex) {
            System.out.println("Klienten har avbrutit programmet.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}