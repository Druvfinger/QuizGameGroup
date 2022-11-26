import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    int port = 54321;
    private MultiWriter multiWriter = new MultiWriter();

    public Server() throws IOException {
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Quiz game server is running");
            while (true) {

                ServerSideGame game = new ServerSideGame();

                ServerSidePlayer player1 = new ServerSidePlayer(ss.accept(), "Player1", game, multiWriter);
                ServerSidePlayer player2 = new ServerSidePlayer(ss.accept(), "Player2", game, multiWriter);

                player1.setOpponent(player2);
                player2.setOpponent(player1);

                game.setCurrentPlayer(player1);
                game.setOpponentPlayer(player2);

                System.out.println(game.getCurrentPlayer().player); // kan skriva ut namn
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