import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public Server() throws IOException {
        int port = 54321;
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Quiz game server is running");
            while (true) {

                ServerSideGame game = new ServerSideGame();
                Database database = new Database();

                MultiWriter multiWriter = new MultiWriter();
                ServerSidePlayer player1 = new ServerSidePlayer(ss.accept(), "Player1", game, multiWriter, database);
                ServerSidePlayer player2 = new ServerSidePlayer(ss.accept(), "Player2", game, multiWriter, database);

                player1.setOpponent(player2);
                player2.setOpponent(player1);

                game.setCurrentPlayer(player1);
                game.setOpponentPlayer(player2);

                System.out.println(game.getCurrentPlayer().player);
                System.out.println(game.getOpponentPlayer().player);

                player1.start();
                player2.start();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }
}