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

                ServerSidePlayer playerOne = new ServerSidePlayer(ss.accept(), "PlayerOne", game);
                ServerSidePlayer playerTwo = new ServerSidePlayer(ss.accept(), "PlayerTwo", game);

                playerOne.setOpponent(playerTwo);
                playerTwo.setOpponent(playerOne);
                game.currentPlayer = playerOne;
                playerOne.start();
                playerTwo.start();
            }
        } finally {
            ss.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }
}
