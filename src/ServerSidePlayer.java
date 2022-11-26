import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class
ServerSidePlayer extends Thread {

    ServerSidePlayer opponent;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    String player;
    ServerSideGame game;
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

    public int getScore() {
        return score;
    }

    public ServerSidePlayer getOpponent() {
        return opponent;
    }
    public void otherPlayerMoved(int score){
        output.println("Opponent played " + score);

        if (game.hasWinner()){
            output.println("You lost!");
        }else {
            if (game.isTie()){
                output.println("You tied");
            }else {output.println("How did we get here?!");}
        }
    }


    public void run() {
        try {
            output.println("All players connected");
            System.out.println("All players connected");
            while (true) {
                String command = input.readLine();
                if (command.startsWith("MOVE")){
                    game.isFirstRound(this);
                     if (game.hasPlayedRound(this)){
                         output.println("Round Played");
                         output.println(game.hasWinner() ? "Victory" : game.isTie() ? "Tie"
                                 : "");
                     }
                }

            }
        } catch (IOException e) {
            System.out.println(player + " died: " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
