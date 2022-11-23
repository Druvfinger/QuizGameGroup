import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSidePlayer extends Thread {

    ServerSidePlayer opponent;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    String player;

    ServerSideGame game; // protokoll
    int score;

    int currentScore = 0;

    public int getCurrentScore() {
        return currentScore;
    }


    public ServerSidePlayer(Socket socket, String player, ServerSideGame game, int score) {
        this.socket = socket;
        this.player = player;
        this.game = game;
        this.score = score;// testing this out for keeping track of score
        try {

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + player); // skickas till klienten
            output.println("Waiting for opponent to connect"); // skickas till klienten
            System.out.println("Waiting for opponent to connect.");


        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }

    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }


    public void run() {
        try {
            output.println("All players connected");
            System.out.println("All players connected");
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
        } catch (IOException e) {
            System.out.println(player + " died: " + e);*/
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
