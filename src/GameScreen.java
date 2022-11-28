import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameScreen extends JFrame {

    int numberOfQuestions;
    int onlyOneAnswerPressed = 1;
    static String playerNumber;
    Boolean isAnswerCorrect = false; // testa
    Boolean isAnswered = false; // testa

    int currentPoint = 0;
    int currentRound = 0; // testa
    static String userName; // livsviktigt för att det ska fungera
    final Color LIGHT_BLUE = new Color(51, 153, 255);
    final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
    final Color VERY_LIGHT_GREEN = new Color(102, 255, 102);
    final Color LIGHT_GREEN = new Color(0, 255, 51);
    final Color GOLD = new Color(255, 204, 51);
    final Color VERY_LIGHT_RED = new Color(255, 102, 102);

    static String quizTitle; // when do you use static ??
    String currentCategory;
    static int finalScore;
    String currentPlayerName;
    String opponentName;
    String answerA = "Svar A";
    String answerB = "Svar B";
    String answerC = "Svar C";
    String answerD = "Svar D";
    int currentQuestion = 1;
    List<JButton> buttonList;
    ServerSideGame game = new ServerSideGame();
    Database database = new Database();
    ResultsScreen resultsScreen;
    GameSettings settings = new GameSettings();
    JButton answerButtonA;
    JButton answerButtonB;
    JButton answerButtonC;
    JButton answerButtonD;
    JButton goOnButton;
    JLabel questionLabel;
    JLabel pointsLabelA;
    JLabel pointsLabelB;
    JTextField infoField;
    JLabel categoryTextLabel;
    String rightAnswer;
    boolean wantToGoForward = false;

    public void setUpGameScreenGUI() {


        JPanel basePanel = new JPanel(new GridLayout(2, 1));
        JPanel upperPanel = new JPanel(new GridLayout(2, 1));
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        JPanel emptyPanel = new JPanel();


        JPanel leftUserInfoPanel = new JPanel(new GridLayout(2, 1));
        JPanel categoryInfoPanel = new JPanel(new BorderLayout());
        JPanel rightUserInfoPanel = new JPanel(new GridLayout(2, 1));
        questionLabel = new JLabel("", SwingConstants.CENTER);

        JPanel lowerPanel = new JPanel();
        JPanel lowerNorthPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel lowerCenterPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel lowerSouthPanel = new JPanel();

        answerButtonA = new JButton(answerA);
        answerButtonB = new JButton(answerB);
        answerButtonC = new JButton(answerC);
        answerButtonD = new JButton(answerD);
        goOnButton = new JButton("Fortsätt");

        buttonList = List.of(answerButtonA, answerButtonB, answerButtonC, answerButtonD);

        JLabel playerEmojiLabelA = new JLabel(new ImageIcon("Pictures/CuteHipster.png"));
        JLabel playerEmojiLabelB = new JLabel(new ImageIcon("Pictures/CuteHeadphones.png"));
        JLabel categoryImageLabel = new JLabel(new ImageIcon());
        JPanel userNamePointsPanelA = new JPanel(new GridLayout(2, 1));
        JPanel userNamePointsPanelB = new JPanel(new GridLayout(2, 1));

        String userNameA = "PLayer A";
        JLabel userNameLabelA = new JLabel(userNameA, SwingConstants.CENTER);
        String userNameB = "Player B";
        JLabel userNameLabelB = new JLabel(userNameB, SwingConstants.CENTER);

        pointsLabelA = new JLabel("Points:", SwingConstants.CENTER);
        pointsLabelB = new JLabel("Points:", SwingConstants.CENTER);

        infoField = new JTextField("Fråga " + currentQuestion, 43);


        categoryTextLabel = new JLabel(currentCategory, SwingConstants.CENTER);
        categoryTextLabel.setPreferredSize(new Dimension(135, 50));
        categoryTextLabel.setForeground(Color.WHITE);
        categoryTextLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        setTitle(quizTitle);
        add(basePanel);

        basePanel.add(upperPanel);
        basePanel.add(lowerPanel);

        lowerPanel.setBackground(LIGHT_BLUE);
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        lowerPanel.add(Box.createRigidArea(new Dimension(1, 5)));
        lowerPanel.add(lowerNorthPanel);
        lowerPanel.add(Box.createRigidArea(new Dimension(1, 5)));
        lowerPanel.add(lowerCenterPanel);
        lowerPanel.add(Box.createRigidArea(new Dimension(1, 5)));
        lowerPanel.add(lowerSouthPanel);

        lowerNorthPanel.setBackground(LIGHT_BLUE);
        lowerCenterPanel.setBackground(LIGHT_BLUE);
        lowerSouthPanel.setBackground(LIGHT_BLUE);

        emptyPanel.setBackground(LIGHT_BLUE);
        emptyPanel.setPreferredSize(new Dimension(400, 25));

        goOnButton.setPreferredSize(new Dimension(250, 40));
        goOnButton.setBorder(new LineBorder(Color.WHITE, 3));
        goOnButton.setBackground(LIGHT_GREEN);

        lowerNorthPanel.add(answerButtonA);
        lowerNorthPanel.add(answerButtonB);
        lowerCenterPanel.add(answerButtonC);
        lowerCenterPanel.add(answerButtonD);

        lowerSouthPanel.add(emptyPanel);
        lowerSouthPanel.add(goOnButton);
        lowerSouthPanel.add(infoField);

        categoryInfoPanel.setBackground(LIGHT_BLUE);
        rightUserInfoPanel.setBackground(Color.WHITE);
        questionLabel.setOpaque(true);
        questionLabel.setBackground(Color.WHITE);

        upperPanel.add(infoPanel);
        upperPanel.add(questionLabel);

        infoPanel.add(leftUserInfoPanel);
        infoPanel.add(categoryInfoPanel);
        infoPanel.add(rightUserInfoPanel);

        leftUserInfoPanel.add(playerEmojiLabelA);
        leftUserInfoPanel.add(userNamePointsPanelA);
        categoryInfoPanel.add(categoryImageLabel, BorderLayout.CENTER);
        categoryInfoPanel.add(categoryTextLabel, BorderLayout.SOUTH);
        rightUserInfoPanel.add(playerEmojiLabelB);
        rightUserInfoPanel.add(userNamePointsPanelB);

        questionLabel.setBorder(new CompoundBorder(new LineBorder(LIGHT_BLUE, 10), new EtchedBorder(EtchedBorder.RAISED)));

        for (JButton button : buttonList) {
            button.addActionListener(listener);
        }

        goOnButton.addActionListener(listener);

        playerEmojiLabelA.setOpaque(true);
        playerEmojiLabelA.setBackground(LIGHT_BLUE);
        playerEmojiLabelB.setOpaque(true);
        playerEmojiLabelB.setBackground(LIGHT_BLUE);

        userNamePointsPanelA.setOpaque(true);
        userNamePointsPanelA.setBackground(LIGHT_BLUE);
        userNamePointsPanelB.setOpaque(true);
        userNamePointsPanelB.setBackground(LIGHT_BLUE);

        userNameLabelA.setFont(new Font("Sans Serif", Font.BOLD, 20));
        userNameLabelA.setForeground(GOLD);
        userNameLabelB.setFont(new Font("Sans Serif", Font.BOLD, 20));
        userNameLabelB.setForeground(GOLD);

        userNamePointsPanelA.add(userNameLabelA);
        userNamePointsPanelA.add(pointsLabelA);
        userNamePointsPanelB.add(userNameLabelB);
        userNamePointsPanelB.add(pointsLabelB);

        //Placerar namn/spelare på panelen
        userNameLabelA.setText(currentPlayerName);
        userNameLabelB.setText(opponentName);

        setSize(500, 670);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public GameScreen(String player, String currentPlayerName, String opponentName, String currentCategory) {
        playerNumber = player;
        this.currentPlayerName = currentPlayerName;
        this.opponentName = opponentName;
        this.currentCategory = currentCategory;

        numberOfQuestions = settings.getNumberOfQuestions(); // nuvarande 3
        setUpGameScreenGUI();
    }

    ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == answerButtonA || e.getSource() == answerButtonB || e.getSource() == answerButtonC ||
                    e.getSource() == answerButtonD) {

                changeScore(isRightAnswer((JButton) e.getSource()), playerNumber); // ändrar antal poäng på spelarens poäng panel
                paintRightOrFalseAnswer((JButton) e.getSource()); // målar knappar i olika färger beroende på om svaret är korrekt
                currentQuestion++;
            }

            if (e.getSource() == goOnButton) {

                if (currentQuestion <= numberOfQuestions) {
                    Client.outWriter.println("NEXT_QUESTION? " + playerNumber);
                } else if (!wantToGoForward) { // (currentQuestion > numberOfQuestions)
                    changeInfoField();
                    questionLabel.setText("");
                    for (JButton button : buttonList) {
                        button.setVisible(false); // VIKTIGT!! Glöm inte att sätta tillbaka till synligt!
                    }
                    revalidate();
                    Client.outWriter.println("BACK_TO_RESULTS " + playerNumber);
                    wantToGoForward = true;
                } else {
                    Client.outWriter.println("SHOW_ME_RESULTS " + playerNumber);
                }
            }
        }
    };


    // kontrollerar om svaret på den valda knappen är korrekt
    //-- Kolla att denna stämmer har en känsla att den spökar - Den spöKar inte! ;-P
    public boolean isRightAnswer(JButton clickedButton) {
        String givenAnswer = clickedButton.getText();
        return givenAnswer.equalsIgnoreCase(rightAnswer);
    }

    // målar knappar i grönt eller rött/grönt beroende på om svaret är korrekt
    public void paintRightOrFalseAnswer(JButton clickedButton) {
        if (isRightAnswer(clickedButton)) {
            clickedButton.setBackground(VERY_LIGHT_GREEN);
        } else {
            clickedButton.setBackground(VERY_LIGHT_RED);
            findButtonWithRightAnswer().setBackground(VERY_LIGHT_GREEN);
        }
    }

    // ger knapp med det korrekta svaret
    public JButton findButtonWithRightAnswer() {
        JButton correctButton = new JButton();
        for (JButton button : buttonList) {
            if (button.getText().equals(rightAnswer)) {
                correctButton = button;
                return correctButton;
            }
        }
        return correctButton;
    }

    // ändrar poäng på spelarens poäng-label
    public void changeScore(boolean isCorrectAnswer, String playerNumber) {
        if (isCorrectAnswer && playerNumber.equals("Player1")) {
            pointsLabelA.setText("Points: " + ++currentPoint + "/3");
            pointsLabelA.revalidate();
            finalScore = currentPoint;
        } else if (!isCorrectAnswer && playerNumber.equals("Player1")) {
            pointsLabelA.setText("Points: " + currentPoint + "/3");
            pointsLabelA.revalidate();
            finalScore = currentPoint;
        } else if (isCorrectAnswer && playerNumber.equals("Player2")) {
            pointsLabelB.setText("Points: " + ++currentPoint + "/3");
            pointsLabelB.revalidate();
            finalScore = currentPoint;
        } else {   // if (!isCorrectAnswer && playerNumber.equals("Player2"))
            pointsLabelB.setText("Points: " + currentPoint + "/3");
            pointsLabelB.revalidate();
            finalScore = currentPoint;
        }
    }

    // ändrar information på info panelen längst ner
    public void changeInfoField() {
        if (currentQuestion <= numberOfQuestions) {
            infoField.setText("Fråga " + currentQuestion);
            infoField.revalidate();
        } else {
            infoField.setText("Du har nu svarat på alla 3 frågorna. Click på Fortsätt för att gå vidare."); // Ändra???
            infoField.revalidate();
        }
    }
}