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
    int currentRoundNumber = 0;
    int currentPlayerPoints = 0;
    int opponentPlayerPoints = 0;
    GameSettings gameSettings = new GameSettings();
    int numberOfCategories = gameSettings.getNumberOfRounds();
    int numberOfQuestions = gameSettings.getNumberOfQuestions();

    public Client() throws IOException {
        socket = new Socket(host, port);
        outWriter = new PrintWriter(socket.getOutputStream(), true);
        inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        game = new ServerSideGame(); //
    }

    public void play() throws IOException {
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
                    resultsScreen.listOfCategoryLabels.get(currentRoundNumber).setText(chosenCategory);
                    resultsScreen.listOfCategoryLabels.get(currentRoundNumber).setForeground(Color.WHITE);
                    resultsScreen.listOfCategoryLabels.get(currentRoundNumber).setFont(new Font("Sans Serif", Font.BOLD, 25));
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

                } else if (response.startsWith("HOLD")) {
                    gameScreen.categoryTextLabel.setText("");
                    gameScreen.questionLabel.setText("Please wait while your opponent is answering.");
                    gameScreen.goOnButton.setEnabled(false); // VIKTIGT!! Glöm inte att sätta tillbaka till synligt!
                    gameScreen.infoField.setVisible(false);
                    gameScreen.revalidate();

                } else if (response.startsWith("BOTH_ANSWERED_QUESTION")) { // NYTT -->
                    gameScreen.goOnButton.setEnabled(true);
                    gameScreen.infoField.setVisible(true);
                    gameScreen.infoField.setText("You can go to next Question");
                    gameScreen.revalidate();

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
                    System.out.println(player + " is waiting.");

                } else if (response.startsWith("POINTS: ")) {
                    String playerInfoNumber = response.substring(8, 15);
                    System.out.println(playerInfoNumber); // kontroll
                    String points = response.substring(16);
                    System.out.println(points); // kontroll
                    String pointsFirstPart = points.substring(9, 10);
                    ImageIcon pointsToStars;

                    //Placerar poäng på GameScreen under respektive spelare innan man går vidare till ResultsScreen
                    if (player.equals("Player1")) {
                        if (playerInfoNumber.equals("Player1")) {
                            gameScreen.pointsLabelA.setText(points); // uppdaterar poäng på GameScreen poäng label

                            pointsToStars = getScaledImage(resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber), points);
                            resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber).setIcon(pointsToStars);
                            // om man vill visa text istället:
                            //resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber).setText(points); // uppdaterar poäng på ResultsScreen poäng label

                            currentPlayerPoints = sumPointsForRounds(currentPlayerPoints, points); // metod som adderar befintliga poäng till poäng från Server
                            resultsScreen.pointsALabel.setText(Integer.toString(currentPlayerPoints));
                            System.out.println(playerInfoNumber + " " + currentPlayerPoints); // kontroll

                        } else { // (playerInfoNumber.equals("Player2"))
                            gameScreen.pointsLabelB.setText(points);

                            pointsToStars = getScaledImage(resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber), points);
                            resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber).setIcon(pointsToStars);
                            // om man vill visa text istället:
                            //resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber).setText(points);


                            opponentPlayerPoints = sumPointsForRounds(opponentPlayerPoints, points);
                            resultsScreen.pointsBLabel.setText(Integer.toString(opponentPlayerPoints));
                            System.out.println(playerInfoNumber + " " + opponentPlayerPoints); // kontroll
                        }
                    } else if (player.equals("Player2")) {
                        if (playerInfoNumber.equals("Player1")) {
                            gameScreen.pointsLabelB.setText(points);

                            pointsToStars = getScaledImage(resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber), points);
                            resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber).setIcon(pointsToStars);
                            // om man vill visa text istället:
                            //resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber).setText(points);

                            opponentPlayerPoints = sumPointsForRounds(opponentPlayerPoints, points);
                            resultsScreen.pointsBLabel.setText(Integer.toString(opponentPlayerPoints));
                            System.out.println(playerInfoNumber + " " + opponentPlayerPoints); // kontroll
                        } else { // (playerInfoNumber.equals("Player2"))
                            gameScreen.pointsLabelA.setText(points);

                            pointsToStars = getScaledImage(resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber), points);
                            resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber).setIcon(pointsToStars);
                            // om man vill visa text istället:
                            // resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber).setText(points); // om man vill visa text istället

                            currentPlayerPoints = sumPointsForRounds(currentPlayerPoints, points);
                            resultsScreen.pointsALabel.setText(Integer.toString(currentPlayerPoints));
                            System.out.println(playerInfoNumber + " " + currentPlayerPoints); // kontroll
                        }
                    }
                    gameScreen.revalidate();

                } else if (response.equals("ANSWERED_QUESTIONS_BOTH")) {
                    gameScreen.questionLabel.setText("Please continue to see the results.");
                    gameScreen.goOnButton.setVisible(true);
                    gameScreen.infoField.setVisible(true);
                    gameScreen.infoField.setText("Please continue to see the results.");
                    gameScreen.revalidate();

                } else if (response.equals("SHOW_RESULTS")) {
                    currentRoundNumber++;
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
                    resultsScreen.revalidate();


                } else if (response.equals("SHOW_FINAL_RESULT")) {

                } else System.out.println("We are missing out on something. " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPointsIntFromPointsString(String points) {
        String part = points.substring(8, 9);
        return Integer.parseInt(part);
    }

    public int sumPointsForRounds(int currenPoints, String pointsToAdd) {
        int pointsToSum = getPointsIntFromPointsString(pointsToAdd);
        return currenPoints + pointsToSum;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.play();
    }

    public ImageIcon turnPointsInPicture(String points) {
        ImageIcon icon = null;
        String pointsPart = points.substring(8, 11);
        if (numberOfQuestions == 3) {
            if (pointsPart.equals("0/3")) {
                icon = new ImageIcon("Pictures/NoStarNoBG3.png");
            } else if (pointsPart.equals("1/3")) {
                icon = new ImageIcon("Pictures/OneStarNoBG3.png");
            } else if (pointsPart.equals("2/3")) {
                icon = new ImageIcon("Pictures/TwoStarNoBG3.png");
            } else if (pointsPart.equals("3/3")) {
                icon = new ImageIcon("Pictures/ThreeStarNoBG3.png");
            }
        } else if (numberOfQuestions == 2) {
            if (pointsPart.equals("0/2")) {
                icon = new ImageIcon("Pictures/NoStarNoBG2.png");
            } else if (pointsPart.equals("1/2")) {
                icon = new ImageIcon("Pictures/OneStarNoBG2.png");
            } else if (pointsPart.equals("2/2")) {
                icon = new ImageIcon("Pictures/TwoStarNoBG2.png");
            }
        } else if (numberOfQuestions == 4) {
            if (pointsPart.equals("0/4")) {
                icon = new ImageIcon("Pictures/NoStarNoBG4.png");
            } else if (pointsPart.equals("1/4")) {
                icon = new ImageIcon("Pictures/OneStarNoBG4.png");
            } else if (pointsPart.equals("2/4")) {
                icon = new ImageIcon("Pictures/TwoStarNoBG4.png");
            } else if (pointsPart.equals("3/4")) {
                icon = new ImageIcon("Pictures/ThreeStarNoBG4.png");
            } else if (pointsPart.equals("4/4")) {
                icon = new ImageIcon("Pictures/FourStarNoBG4.png");
            }
        } else if (numberOfQuestions == 5) {
            if (pointsPart.equals("0/4")) {
                icon = new ImageIcon("Pictures/NoStarNoBG5.png");
            } else if (pointsPart.equals("1/5")) {
                icon = new ImageIcon("Pictures/OneStarNoBG5.png");
            } else if (pointsPart.equals("2/5")) {
                icon = new ImageIcon("Pictures/TwoStarNoBG5.png");
            } else if (pointsPart.equals("3/5")) {
                icon = new ImageIcon("Pictures/ThreeStarNoBG5.png");
            } else if (pointsPart.equals("4/5")) {
                icon = new ImageIcon("Pictures/FourStarNoBG5.png");
            } else if (pointsPart.equals("5/5")) {
                icon = new ImageIcon("Pictures/FiveStarNoBG5.png");
            }
        }
        return icon;
    }

    public ImageIcon getScaledImage(JLabel label, String points) {
        ImageIcon icon = turnPointsInPicture(points);
        Image image = icon.getImage();
        Image imageScaled = null;
        if (numberOfQuestions == 5) {
            imageScaled = image.getScaledInstance(label.getWidth(), label.getHeight() / 3, Image.SCALE_SMOOTH);
        } else if (numberOfQuestions == 4) {
            imageScaled = image.getScaledInstance(label.getWidth() - 15, label.getHeight() - 60, Image.SCALE_SMOOTH);
        } else if (numberOfQuestions == 3) {
            imageScaled = image.getScaledInstance(label.getWidth() - 20, label.getHeight() / 2, Image.SCALE_SMOOTH);
        } else if (numberOfQuestions == 2) {
            imageScaled = image.getScaledInstance(label.getWidth(), label.getHeight() - 15, Image.SCALE_SMOOTH);
        }
        ImageIcon scaledIcon = new ImageIcon(imageScaled);
        return scaledIcon;
    }
}