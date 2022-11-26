
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    protected final String host = "127.0.0.1";
    protected final int port = 54321;

    private Socket socket;
    static BufferedReader inReader;
    static PrintWriter outWriter;
    static String player;

    WelcomeScreen currentWelcomeScreen;
    GameScreen gameScreen;
    ServerSideGame game;

    public Client() throws IOException {
        socket = new Socket(host, port);
        outWriter = new PrintWriter(socket.getOutputStream(), true);
        inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void play() throws IOException {
        String response = "";
        try {
            while (true) {
                response = inReader.readLine();
                if (response.startsWith("WELCOME")) {
                    player = response.substring(8);
                    System.out.println(response);
                    System.out.println(player);
                    currentWelcomeScreen = new WelcomeScreen(player);
                    currentWelcomeScreen.userInfoTextField.setText("Just one moment " + player + "! We are waiting for your opponent to connect.");

                }
                else if (response.startsWith("WAITING")){
                    System.out.println("Waiting for opponent to connect.");
                }
                else if (response.equals("PLAYERS_CONNECTED")) {
                    System.out.println("All players connected. We are set to go.");
                }
                else if (response.equals("PLAYERS_READY")){
                    System.out.println(response);
                    currentWelcomeScreen.newGameButton.setBackground(WelcomeScreen.VERY_LIGHT_GREEN);
                    currentWelcomeScreen.newGameButton.setVisible(true);
                    currentWelcomeScreen.userInfoTextField.setText("We are set to go. Please continue to the game.");
                    currentWelcomeScreen.repaint();
                    currentWelcomeScreen.revalidate();
                }

                //else System.out.println("Something fishy is going on.");
                else System.out.println("We are missing something.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

            public static void main (String[]args) throws IOException {
                Client client = new Client();
                client.play();
            }

            public void makeLabelForScoreEachRound () {
                String name = "Round " + gameScreen.getCurrentRound() + ": " + game.getCurrentPlayer().getCurrentScore();
                //Try to make label for score keeping
            }
        }