
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    protected final String host = "127.0.0.1";
    protected final int port = 54321;

    ServerSideGame game;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    WelcomeScreen welcomeScreen;
    ResultsScreen resultsScreen;
    GameScreen gameScreen;

    public Client() throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void play() throws IOException {
        String response = "";
        String player = "1";
        String opponentPlayer = "2";
        try {

            while (true) {

                response = in.readLine();
                if (response.startsWith("WELCOME")) {
                    player = response.substring(8, response.length() - 1);
                    welcomeScreen = new WelcomeScreen();
                    System.out.println(response);
                }
                else if (response.equals("Waiting for opponent to connect")){
                    System.out.println("Waiting for opponent to connect.");
                }
                else if (response.equals("All players connected")){
                    System.out.println("All players connected. We are set to go.");
                    /*System.out.println(userName);
                    /*out.println("start");
                    gameScreen = new GameScreen();
                    game.chooseCategory();
                } else if (response.startsWith("SCORE")) {
                    int score = Integer.parseInt(response.substring(5));
                    int temp = gameScreen.currentRound;
                    if (temp ==  1){
                        //resultsScreen.player1ResultLabel.setText(score);
                    }*/
                }
                else System.out.println("Something fishy is going on.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.play();
    }

    public void makeLabelForScoreEachRound() {
        String name = "Round " + gameScreen.getCurrentRound() + ": " + game.getCurrentPlayer().getCurrentScore();
        //Try to make label for score keeping
    }
}