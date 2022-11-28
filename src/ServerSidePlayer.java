import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSidePlayer extends Thread {

    ServerSidePlayer opponent;
    ServerSidePlayer currentPlayer;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    String player;
    String currentPlayerName;

    ServerSideGame game;
    Database serverDatabase;
    private MultiWriter multiWriter;
    int score;
    int currentScore = 0;
    boolean playerEnteredName = false;
    boolean playerReadyToPlay = false;
    String category; // går det bra att göra denna variabel statisk? Hur ska den påverka om man spelar flera par???
    String question; // går det bra att göra denna variabel statisk? Hur ska den påverka om man spelar flera par???
    StringBuilder builderWithAnswers; // går det bra att göra denna variabel statisk? Hur ska den påverka om man spelar flera par???

    static String answer;

    public int getCurrentScore() {
        return currentScore;
    }


    public ServerSidePlayer(Socket socket, String player, ServerSideGame game, MultiWriter multiWriter, Database database) throws IOException {
        this.socket = socket;
        this.player = player;
        this.game = game;
        this.multiWriter = multiWriter;
        this.serverDatabase = database;
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
                    System.out.println(playerEnteredName);// för att kontrollera att det fungerar korrekt
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
//                    if (playerReadyToPlay && this.getOpponent().playerReadyToPlay) {
//                        System.out.println("READY_TO_PLAY_BOTH");// för att kontrollera att det fungerar korrekt
//                        toClient = "READY_TO_PLAY_BOTH";
//                        for (PrintWriter writer : multiWriter.getWriters()) {
//                            writer.println(toClient);
//                        }
//                    }
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
                    game.getQuestionTest(); // does the exact same as before it is just contained in method to keep clean


                }else if (fromClient.startsWith("READY_TO_ANSWER ")) {
                    System.out.println(fromClient);
                    output.println(fromClient);
                }

                else if (fromClient.startsWith("QUESTION? ")) {
                    System.out.println(category);
                    output.println("QUESTION: " + question);
                    output.println("ANSWERS: " + builderWithAnswers);
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


// Repeatedly get commands from the client and process them.
           /* while (true) {
                String command = input.readLine();
                if (command.startsWith("MOVE")) {



                    int location = Integer.parseInt(command.substring(5));
                    if (game.legalMove(location, this)) {
                        output.println("VALID_MOVE");
                        output.println(game.hasWinner() ? "VICTORY"
                                : game.boardFilledUp() ? "TIE"
                                : "");
                    } else {
                        output.println("MESSAGE ?");
                    }
                } else if (command.startsWith("QUIT")) {
                    return;
                }
            /*while (true) {
                String command = input.readLine();
                if (command.equals("start")) {
                    game.newRound();
                    output.println("SCORE" + game.currentPlayer.score);
                    String username;
                    if (command.contains("player")) {
                        username = command.replace("player ", "");
                        this.player = username;
                    } else if (command.equals("MOVE")) {
                        System.out.println(player + " connected");
                    } else {
                        System.out.println("not yey"); // skriver ut ifall nåt skulle gå fel
                    }
                }
            }
        */
