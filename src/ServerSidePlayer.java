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

    ServerSideGame game; // protokoll
    int score;

    int currentScore = 0;
    static int playersReady = 0;
    int numberPLayersEnteredName;

    public int getCurrentScore() {
        return currentScore;
    }


    public ServerSidePlayer(Socket socket, String player, ServerSideGame game, int score) throws IOException {
        this.socket = socket;
        this.player = player;
        this.game = game;
        this.score = score;// testing this out for keeping track of score
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + player); // skickas till klienten
            output.println("WAITING " + player); // skickas till klienten
            System.out.println("Waiting for opponent to connect.");
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
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

    public void run() {
        try {
            output.println("PLAYERS_CONNECTED");
            System.out.println("All players connected");

            String fromClient, toClient;
            while ((fromClient = input.readLine()) != null) { // kommunicerar med klienten
                if (fromClient.startsWith("READY_TO_PLAY ")){
                    player = fromClient.substring(14);
                    System.out.println(fromClient);
                    System.out.println(player);
                    System.out.println(player + " is ready to play");
                    playersReady++;
                    System.out.println(playersReady);
                    if (playersReady == 2){
                        toClient = "PLAYERS_READY";
                        output.println(toClient);
                        System.out.println("PLAYERS_READY");
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
