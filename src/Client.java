
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    protected final String host = "127.0.0.1";
    protected final int port = 54321;
    Socket socket;

    static BufferedReader inReader;
    static PrintWriter outWriter;
    static String player;
    WelcomeScreen welcomeScreen;
    ResultsScreen resultsScreen;
    GameScreen gameScreen;
    ChooseCategoryScreen categoryScreen;
    ServerSideGame game; //
    String currentPlayerName;
    String opponentName;
    String chosenCategory;
    boolean myTurnToChoose = false;
    int currentRoundNumber = 1;

    public Client() throws IOException {
        socket = new Socket(host, port);
        outWriter = new PrintWriter(socket.getOutputStream(), true);
        inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        game = new ServerSideGame(); //
    }

    public void play() {
        String response;
        try {
            while (true) {
                response = inReader.readLine();

                // Sparar spelarens player number, visar välkomstskärm
                if (response.startsWith("WELCOME")) {
                    player = response.substring(8);
                    if (player.equals("Player1")) {
                        myTurnToChoose = true;
                    }
                    welcomeScreen = new WelcomeScreen(player);
                    welcomeScreen.userInfoTextField.setText("Just one moment " + player + "! We are waiting for your opponent.");
                } else if (response.startsWith("WAITING")) {
                    System.out.println("Waiting for opponent to connect.");
                } else if (response.equals("PLAYERS_CONNECTED")) {
                    System.out.println("All players connected. We are set to go.");
                    // tilldelar namn till denna spelare och till spelarens opponent
                } else if (response.startsWith("MY_NAME ")) {
                    String playerNumber = response.substring(8, 15);
                    String playerName = response.substring(16);
                    if (playerNumber.equals(player)) {
                        currentPlayerName = playerName;
                    } else opponentName = playerName;
                }

                // väntar tills både spelare skrivit in sitt namn, sedan gör "Fortsätt" knapp synlig
                else if (response.equals("ENTERED_NAME_BOTH")) {
                    welcomeScreen.newGameButton.setBackground(Constants.VERY_LIGHT_GREEN);
                    welcomeScreen.newGameButton.setVisible(true);
                    welcomeScreen.userInfoTextField.setText("We are set to go. Please continue to the game.");
                    welcomeScreen.repaint();
                    welcomeScreen.revalidate();

                    // visar resultat-skärm som ser olika ut beroende på vems tur att välja det är
                } else if (response.equals("READY_TO_PLAY")) {
                    System.out.println("My turn to choose: " + myTurnToChoose); // för att kontrollera vem som väljer
                    if (myTurnToChoose) {
                        welcomeScreen.setVisible(false);
                        resultsScreen = new ResultsScreen(player, currentPlayerName, opponentName);
                        resultsScreen.theirTurnLabel.setText("Your Turn");
                        resultsScreen.infoField.setText("Your turn to choose a category. Click \"Fortsätt\" to continue.");
                    } else {
                        welcomeScreen.setVisible(false);
                        resultsScreen = new ResultsScreen(player, currentPlayerName, opponentName);
                        resultsScreen.theirTurnLabel.setText("Their Turn");
                        resultsScreen.goOnButton.setVisible(false);
                        resultsScreen.infoField.setText("Please wait while your opponent is choosing a category.");
                    }

                } else if (response.startsWith("CHOOSING_CATEGORY ")) {
                    if (myTurnToChoose) {
                        resultsScreen.setVisible(false);
                        categoryScreen = new ChooseCategoryScreen(player, currentPlayerName);
                    }

                } else if (response.startsWith("I_CHOSE ")) {
                    chosenCategory = response.substring(8);
                    System.out.println(chosenCategory); // för att kontrollera att det fungerar korrekt
                    resultsScreen.listOfCategoryLabels.get(currentRoundNumber - 1).setText(chosenCategory);
                    resultsScreen.listOfCategoryLabels.get(currentRoundNumber - 1).setForeground(Color.WHITE);
                    resultsScreen.listOfCategoryLabels.get(currentRoundNumber - 1).setFont(new Font("Sans Serif", Font.BOLD, 25));
                    resultsScreen.categoryPanel.removeAll();
                    for (JLabel label : resultsScreen.listOfCategoryLabels) resultsScreen.categoryPanel.add(label);
                    resultsScreen.goOnButton.setVisible(true);
                    resultsScreen.infoField.setText("Your game experience is ready. Please continue.");
                    resultsScreen.theirTurnLabel.setText("Time to play!");
                    resultsScreen.revalidate();

                    if (myTurnToChoose) {
                        categoryScreen.setVisible(false);
                        myTurnToChoose = false;         // NYTT Reglerar spelarnas tur att välja
                    } else myTurnToChoose = true;         // NYTT Reglerar spelarnas tur att välja

                    resultsScreen.setVisible(true); // viktigt för den spelaren som har just valt kategorin


                } else if (response.startsWith("READY_TO_ANSWER ")) {
                    resultsScreen.setVisible(false);
                    outWriter.println("QUESTION? " + chosenCategory);

                } else if (response.startsWith("QUESTION: ")) {
                    String question = response.substring(10);
                    System.out.println(question);// för att kontrollera att det fungerar korrekt
                    gameScreen = new GameScreen(player, currentPlayerName, opponentName, chosenCategory);
                    gameScreen.currentCategory = chosenCategory;
                    gameScreen.questionLabel.setText(question);
                    gameScreen.revalidate();

                } else if (response.startsWith("ANSWERS: ")) {
                    String answers = response.substring(9);
                    System.out.println(answers);// för att kontrollera att det fungerar korrekt
                    String[] answersArray = answers.split(",");
                    for (int i = 0; i < answersArray.length - 1; i++) {
                        System.out.println(answersArray[i]);// för att kontrollera att det fungerar korrekt
                        gameScreen.buttonList.get(i).setText(answersArray[i]);
                    }
                    gameScreen.rightAnswer = answersArray[answersArray.length - 1]; // NYTT (Det korrekta svaret)
                    System.out.println(gameScreen.rightAnswer);
                    gameScreen.revalidate();
                    
                } else if (response.startsWith("I ANSWERED")) { // NYTT -->
                    int positionOfLastBlankspace = response.lastIndexOf(" "); // NYTT test
                    String playerName = response.substring(positionOfLastBlankspace); // NYTT test
                    System.out.println(playerName); // NYTT test
                    boolean p1Played = false;
                    boolean p2Played = false;
                    if (playerName.equals("Player1")) {
                        p1Played = true;
                        gameScreen.infoField.setText("Waiting for other player to answer.");
                    }
                    if (playerName.equals("Player2")) {
                        p2Played = true;
                        gameScreen.infoField.setText("Waiting for other player to answer.");
                    }
                    if (p1Played && p2Played) {
                        gameScreen.goOnButton.setEnabled(true);
                    }
// END of NYTT
                } else if (response.startsWith("NEXT_QUESTION: ")) {
                    String question = response.substring(15);
                    System.out.println(question);// för att kontrollera att det fungerar korrekt
                    gameScreen.questionLabel.setText(question);
                    gameScreen.changeInfoField();
                    gameScreen.revalidate();

                } else if (response.startsWith("NEXT_ANSWERS: ")) {
                    String answers = response.substring(14);
                    System.out.println(answers);// för att kontrollera att det fungerar korrekt
                    String[] answersArray = answers.split(",");
                    for (int i = 0; i < answersArray.length - 1; i++) {
                        System.out.println(answersArray[i]);// för att kontrollera att det fungerar korrekt
                        gameScreen.buttonList.get(i).setText(answersArray[i]);
                        gameScreen.buttonList.get(i).setBackground(new JButton().getBackground()); // NYTT målar knappar i default färg
                    }
                    gameScreen.rightAnswer = answersArray[answersArray.length - 1]; // NYTT (Det korrekta svaret)
                    System.out.println(gameScreen.rightAnswer);
                    gameScreen.revalidate();

                } else if (response.equals("WAIT")) {
                    gameScreen.categoryTextLabel.setText("");
                    gameScreen.questionLabel.setText("Please wait while your opponent is answering.");
                    gameScreen.goOnButton.setVisible(false); // VIKTIGT!! Glöm inte att sätta tillbaka till synligt!
                    gameScreen.infoField.setVisible(false);
                    gameScreen.revalidate();
                } else if (response.equals("ANSWERED_QUESTIONS_BOTH")) {
                    gameScreen.questionLabel.setText("Please continue to see the results.");
                    gameScreen.goOnButton.setVisible(true);
                    gameScreen.infoField.setVisible(true);
                    gameScreen.infoField.setText("Please continue to see the results.");
                    gameScreen.revalidate();
                } else if (response.equals("SHOW_RESULTS")) {
                    gameScreen.setVisible(false);
                    resultsScreen.setVisible(true);
                    if (myTurnToChoose) {
                        resultsScreen.theirTurnLabel.setText("Your Turn");
                        resultsScreen.infoField.setText("Your turn to choose a category. Click \"Continue\" to continue.");
                    } else {
                        resultsScreen.theirTurnLabel.setText("Their Turn");
                        resultsScreen.goOnButton.setVisible(false);
                        resultsScreen.infoField.setText("Please wait while your opponent is choosing a category.");
                    }
                }else if(response.equals("SHOW_FINAL_RESULT")){

                } else System.out.println("We are missing out on something. " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.play();
    }
}