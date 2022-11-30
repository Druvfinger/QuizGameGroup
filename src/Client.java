
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
    ServerSideGame game;
    String currentPlayerName;
    String opponentName;
    String chosenCategory;
    boolean myTurnToChoose = false;
    static boolean gameOver = false;
    int currentRoundNumber = 0;
    int currentPlayerPoints = 0;
    int opponentPlayerPoints = 0;
    GameSettings gameSettings = new GameSettings();

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

                if (response.startsWith("WELCOME")) {
                    player = response.substring(8);
                    setScreenWaitingToStartGame(player);

                } else if (response.startsWith("WAITING")) {
                    System.out.println("Waiting for opponent to connect.");

                } else if (response.equals("PLAYERS_CONNECTED")) {
                    System.out.println("All players connected. We are set to go.");

                } else if (response.startsWith("MY_NAME ")) {
                    String playerNumber = response.substring(8, 15);
                    String playerName = response.substring(16);
                    if (playerNumber.equals(player)) {
                        currentPlayerName = playerName;
                    } else opponentName = playerName;

                } else if (response.equals("ENTERED_NAME_BOTH")) {
                    setScreenToStartGame();

                } else if (response.equals("READY_TO_PLAY")) {
                    determineWhoChoosesCategory();

                } else if (response.startsWith("CHOOSING_CATEGORY ")) {
                    setScreenChooseCategory();

                } else if (response.startsWith("I_CHOSE ")) {
                    chosenCategory = response.substring(8);
                    System.out.println(chosenCategory);
                    setUpForNewRound(chosenCategory);

                } else if (response.startsWith("READY_TO_ANSWER ")) {
                    resultsScreen.setVisible(false);
                    outWriter.println("QUESTION? " + chosenCategory);

                } else if (response.startsWith("QUESTION: ")) {
                    String question = response.substring(10);
                    System.out.println(question);
                    getAndPaintUpQuestion(question);

                } else if (response.startsWith("ANSWERS: ")) {
                    String answers = response.substring(9);
                    System.out.println(answers);
                    getAndPaintUpAnswers(answers);

                } else if (response.startsWith("HOLD")) {
                    setScreenToWaitForOpponentToFinishQuestion();

                } else if (response.startsWith("BOTH_ANSWERED_QUESTION")) {
                    setScreenReadyToGoToNextQuestion();

                } else if (response.startsWith("NEXT_QUESTION: ")) {
                    String question = response.substring(15);
                    System.out.println(question);
                    getAndPaintUpNextQuestion(question);

                } else if (response.startsWith("NEXT_ANSWERS: ")) {
                    String answers = response.substring(14);
                    System.out.println(answers);
                    getAndPaintUpNextAnswers(answers);

                } else if (response.equals("WAIT")) {
                    setScreenToWaitForOpponentToFinishRound();

                } else if (response.startsWith("POINTS: ")) {
                    String playerInfoNumber = response.substring(8, 15);
                    String points = response.substring(16);
                    setPoints(playerInfoNumber, points);

                } else if (response.equals("ANSWERED_QUESTIONS_BOTH")) {
                    showGoToResultScreen();

                } else if (response.equals("SHOW_RESULTS")) {
                    showResults();

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

    public void setScreenToStartGame() {
        welcomeScreen.newGameButton.setBackground(Constants.VERY_LIGHT_GREEN);
        welcomeScreen.newGameButton.setVisible(true);
        welcomeScreen.userInfoTextField.setText("We are set to go. Please continue to the game.");
        welcomeScreen.repaint();
        welcomeScreen.revalidate();
    }
    public void setScreenWaitingToStartGame(String player){
        if (player.equals("Player1")) {
            myTurnToChoose = true;
        }
        welcomeScreen = new WelcomeScreen(player);
        if (player.equals("Player1")){
            welcomeScreen.setLocation(400,250);
        }
        else welcomeScreen.setLocation(900,250);
        welcomeScreen.userInfoTextField.setText("Just one moment " + player + "! We are waiting for your opponent.");
    }
    public void setScreenChooseCategory(){
        if (myTurnToChoose) {
            resultsScreen.setVisible(false);
            categoryScreen = new ChooseCategoryScreen(player, currentPlayerName);
            categoryScreen.setLocation(resultsScreen.getLocation());
        }
    }

    public void getAndPaintUpQuestion(String question) {
        gameScreen = new GameScreen(player, currentPlayerName, opponentName, chosenCategory);
        gameScreen.setLocation(resultsScreen.getLocation());
        gameScreen.currentCategory = chosenCategory;
        gameScreen.questionLabel.setText(question);
        gameScreen.revalidate();
    }

    public void getAndPaintUpNextQuestion(String question) {
        gameScreen.questionLabel.setText(question);
        gameScreen.changeInfoField();
        gameScreen.revalidate();
    }

    public void determineWhoChoosesCategory() {
        System.out.println("My turn to choose: " + myTurnToChoose);
        if (myTurnToChoose) {
            welcomeScreen.setVisible(false);
            resultsScreen = new ResultsScreen(player, currentPlayerName, opponentName);
            resultsScreen.setLocation(welcomeScreen.getLocation());
            resultsScreen.theirTurnLabel.setText("Your Turn");
            resultsScreen.infoField.setText("Your turn to choose a category. Click \"Continue\" to continue.");
        } else {
            welcomeScreen.setVisible(false);
            resultsScreen = new ResultsScreen(player, currentPlayerName, opponentName);
            resultsScreen.setLocation(welcomeScreen.getLocation());
            resultsScreen.theirTurnLabel.setText("Their Turn");
            resultsScreen.goOnButton.setVisible(false);
            resultsScreen.infoField.setText("Please wait while your opponent is choosing a category.");
        }
    }

    public void getAndPaintUpAnswers(String answers) {
        String[] answersArray = answers.split(",");
        for (int i = 0; i < answersArray.length - 1; i++) {
            gameScreen.buttonList.get(i).setText(answersArray[i]);
        }
        gameScreen.rightAnswer = answersArray[answersArray.length - 1];
        gameScreen.revalidate();
    }

    public void getAndPaintUpNextAnswers(String answers) {
        String[] answersArray = answers.split(",");
        for (int i = 0; i < answersArray.length - 1; i++) {
            gameScreen.buttonList.get(i).setText(answersArray[i]);
            gameScreen.buttonList.get(i).setBackground(new JButton().getBackground());
        }
        gameScreen.rightAnswer = answersArray[answersArray.length - 1];
        gameScreen.revalidate();
    }

    public void setUpForNewRound(String chosenCategory) {
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
            myTurnToChoose = false;
        } else myTurnToChoose = true;

        welcomeScreen.setVisible(false);
        resultsScreen.setVisible(true);
    }

    public void setScreenToWaitForOpponentToFinishRound() {
        gameScreen.categoryTextLabel.setText("");
        gameScreen.questionLabel.setText("Please wait while your opponent is answering.");
        gameScreen.goOnButton.setVisible(false);
        gameScreen.infoField.setVisible(false);
        gameScreen.revalidate();
        System.out.println(player + " is waiting.");
    }

    public void setScreenToWaitForOpponentToFinishQuestion() {
        gameScreen.categoryTextLabel.setText("");
        gameScreen.questionLabel.setText("Please wait while your opponent is answering.");
        gameScreen.goOnButton.setEnabled(false);
        gameScreen.infoField.setVisible(false);
        gameScreen.revalidate();
    }

    public void setScreenReadyToGoToNextQuestion() {
        gameScreen.goOnButton.setEnabled(true);
        gameScreen.infoField.setVisible(true);
        gameScreen.infoField.setText("You can go to next Question");
        gameScreen.revalidate();
    }

    public void showGoToResultScreen() {
        gameScreen.questionLabel.setText("Please continue to see the results.");
        gameScreen.goOnButton.setVisible(true);
        gameScreen.infoField.setVisible(true);
        gameScreen.infoField.setText("Please continue to see the results.");
        gameScreen.revalidate();
    }

    public void setPoints(String playerInfoNumber, String points) {
        ImageIcon pointsToStars;
        if (player.equals("Player1")) {
            if (playerInfoNumber.equals("Player1")) {
                gameScreen.pointsLabelA.setText(points);
                pointsToStars = getScaledImage(resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber), points);
                resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber).setIcon(pointsToStars);
                currentPlayerPoints = sumPointsForRounds(currentPlayerPoints, points);
                resultsScreen.pointsALabel.setText(Integer.toString(currentPlayerPoints));
                System.out.println(playerInfoNumber + " " + currentPlayerPoints + "p / " + gameSettings.getNumberOfQuestions());
            } else {
                gameScreen.pointsLabelB.setText(points);
                pointsToStars = getScaledImage(resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber), points);
                resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber).setIcon(pointsToStars);
                opponentPlayerPoints = sumPointsForRounds(opponentPlayerPoints, points);
                resultsScreen.pointsBLabel.setText(Integer.toString(opponentPlayerPoints));
                System.out.println(playerInfoNumber + " " + opponentPlayerPoints + "p / " + gameSettings.getNumberOfQuestions());
            }

        } else if (player.equals("Player2")) {
            if (playerInfoNumber.equals("Player1")) {
                gameScreen.pointsLabelB.setText(points);
                pointsToStars = getScaledImage(resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber), points);
                resultsScreen.listOfLabelsPlayerB.get(currentRoundNumber).setIcon(pointsToStars);
                opponentPlayerPoints = sumPointsForRounds(opponentPlayerPoints, points);
                resultsScreen.pointsBLabel.setText(Integer.toString(opponentPlayerPoints));
                System.out.println(playerInfoNumber + " " + opponentPlayerPoints + "p / " + gameSettings.getNumberOfQuestions());
            } else {
                gameScreen.pointsLabelA.setText(points);
                pointsToStars = getScaledImage(resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber), points);
                resultsScreen.listOfLabelsPlayerA.get(currentRoundNumber).setIcon(pointsToStars);
                currentPlayerPoints = sumPointsForRounds(currentPlayerPoints, points);
                resultsScreen.pointsALabel.setText(Integer.toString(currentPlayerPoints));
                System.out.println(playerInfoNumber + " " + currentPlayerPoints + "p / " + gameSettings.getNumberOfQuestions());
            }
        }
        gameScreen.revalidate();
    }

    public void showResults() {
        currentRoundNumber++;
        gameScreen.setVisible(false);
        resultsScreen.setVisible(true);
        resultsScreen.setLocation(gameScreen.getLocation());
        System.out.println("CurrentRound: " + ServerSideGame.currentRound + " - Max Rounds: " +
                gameSettings.getNumberOfRounds() + " -- CurrentQuestion: " +
                (GameScreen.currentQuestion - 1) + " - Max Questions: " + gameSettings.getNumberOfQuestions());
        if ((ServerSideGame.currentRound == gameSettings.getNumberOfRounds()) && ((GameScreen.currentQuestion - 1) ==
                gameSettings.getNumberOfQuestions())) {
            gameOver = true;
            getWinner();

        } else if (myTurnToChoose) {
            resultsScreen.theirTurnLabel.setText("Your Turn");
            resultsScreen.infoField.setText("Your turn to choose a category. Click \"Continue\" to continue.");

        } else {
            resultsScreen.theirTurnLabel.setText("Their Turn");
            resultsScreen.goOnButton.setVisible(false);
            resultsScreen.infoField.setText("Please wait while your opponent is choosing a category.");
            resultsScreen.revalidate();
        }
    }

    public int sumPointsForRounds(int currenPoints, String pointsToAdd) {
        int pointsToSum = getPointsIntFromPointsString(pointsToAdd);
        return currenPoints + pointsToSum;
    }

    public void getWinner() {
        gameScreen.setVisible(false);
        resultsScreen.infoField.setText("Game is over press continue to exit game");
        resultsScreen.goOnButton.setText("Exit the game");
        if (this.currentPlayerPoints > opponentPlayerPoints) {
            resultsScreen.theirTurnLabel.setText("YOU WON!");
        } else if (this.currentPlayerPoints == opponentPlayerPoints) {
            resultsScreen.theirTurnLabel.setText("YOU TIED!");
        } else {
            resultsScreen.theirTurnLabel.setText("YOU LOST");
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.play();
    }

    public ImageIcon turnPointsInPicture(String points) {
        ImageIcon icon = null;
        String pointsPart = points.substring(8, 11);
        if (gameSettings.getNumberOfQuestions() == 3) {
            if (pointsPart.equals("0/3")) {
                icon = new ImageIcon("Pictures/NoStarNoBG3.png");
            } else if (pointsPart.equals("1/3")) {
                icon = new ImageIcon("Pictures/OneStarNoBG3.png");
            } else if (pointsPart.equals("2/3")) {
                icon = new ImageIcon("Pictures/TwoStarNoBG3.png");
            } else if (pointsPart.equals("3/3")) {
                icon = new ImageIcon("Pictures/ThreeStarNoBG3.png");
            }

        } else if (gameSettings.getNumberOfQuestions() == 2) {
            if (pointsPart.equals("0/2")) {
                icon = new ImageIcon("Pictures/NoStarNoBG2.png");
            } else if (pointsPart.equals("1/2")) {
                icon = new ImageIcon("Pictures/OneStarNoBG2.png");
            } else if (pointsPart.equals("2/2")) {
                icon = new ImageIcon("Pictures/TwoStarNoBG2.png");
            }

        } else if (gameSettings.getNumberOfQuestions() == 4) {
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

        } else if (gameSettings.getNumberOfQuestions() == 5) {
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
        if (gameSettings.getNumberOfQuestions() == 5) {
            imageScaled = image.getScaledInstance(label.getWidth(), label.getHeight() / 3, Image.SCALE_SMOOTH);
        } else if (gameSettings.getNumberOfQuestions() == 4) {
            imageScaled = image.getScaledInstance(label.getWidth() - 15, label.getHeight() - 60, Image.SCALE_SMOOTH);
        } else if (gameSettings.getNumberOfQuestions() == 3) {
            imageScaled = image.getScaledInstance(label.getWidth() - 20, label.getHeight() / 2, Image.SCALE_SMOOTH);
        } else if (gameSettings.getNumberOfQuestions() == 2) {
            imageScaled = image.getScaledInstance(label.getWidth(), label.getHeight() - 15, Image.SCALE_SMOOTH);
        }
        return new ImageIcon(imageScaled);
    }
}