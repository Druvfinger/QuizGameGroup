import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    int port = 54321;

    public Server() {
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Quiz game server is running");
            while (true) {
                MultiWriter multiWriter = new MultiWriter();
                ServerSideGame game = new ServerSideGame();
                Database database = new Database();

                ServerSidePlayer player1 = new ServerSidePlayer(ss.accept(), "Player1", game, multiWriter, database);
                ServerSidePlayer player2 = new ServerSidePlayer(ss.accept(), "Player2", game, multiWriter, database);

                player1.setOpponent(player2);
                player2.setOpponent(player1);

                game.setCurrentPlayer(player1);
                game.setOpponentPlayer(player2);

                System.out.println(game.getCurrentPlayer().player + " Connected");
                System.out.println(game.getOpponentPlayer().player + " Connected");

                player1.start();
                player2.start();
            }
        } catch (Exception e){
            System.out.println("Player Disconnected");
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}