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

    ServerSideGame game;

    public ServerSidePlayer(Socket socket, String player, ServerSideGame game) {
        this.socket = socket;
        this.player = player;
        this.game = game;
        try {

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + player);
            output.println("Waiting for opponent to connect");


        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }


    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }



    public void run(){
        try{
            output.println("All players connected");
            while (true){
                String command = input.readLine();

                String username;
                if (command.contains("player")){
                    username = command.replace("player ", "");
                    this.player = username;
                }else if (command.equals("MOVE")){
                    System.out.println(player + " connected");
                }else {
                    System.out.println("not yey"); // skriver ut ifall nåt skulle gå fel
                }
            }
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
