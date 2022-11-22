import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    int port = 54321;

    public Server() throws IOException {
        ServerSocket ss = new ServerSocket(port);

        System.out.println("Quiz game server is running");
        try {
            while (true) {

                ServerSideGame game = new ServerSideGame();

                ServerSidePlayer player1 = new ServerSidePlayer(ss.accept(), "Player1", game, 0);
                ServerSidePlayer player2 = new ServerSidePlayer(ss.accept(), "Player2", game,0);

                player1.setOpponent(player2);
                player2.setOpponent(player1);
                game.currentPlayer = player1;
                player1.start();
                player2.start();
            }
        } finally {
            ss.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }
}
