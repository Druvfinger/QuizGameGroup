import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class GameScreen extends JFrame {
    String answerA = "Svar A";
    String answerB = "Svar B";
    String answerC = "Svar C";
    String answerD = "Svar D";


    JPanel basePanel = new JPanel(new GridLayout(2, 1));
    JPanel upperPanel = new JPanel(new GridLayout(2, 1));
    JPanel infoPanel = new JPanel(new GridLayout(1, 3));
    JPanel emptyPanel = new JPanel();


    JPanel leftUserInfoPanel = new JPanel(new GridLayout(2, 1));
    JPanel categoryInfoPanel = new JPanel(new BorderLayout());
    JPanel rightUserInfoPanel = new JPanel(new GridLayout(2, 1));
    JLabel questionLabel = new JLabel("", SwingConstants.CENTER);

    JPanel lowerPanel = new JPanel();
    JPanel lowerNorthPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    JPanel lowerCenterPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    JPanel lowerSouthPanel = new JPanel();

    JButton answerButtonA = new JButton(answerA);
    JButton answerButtonB = new JButton(answerB);
    JButton answerButtonC = new JButton(answerC);
    JButton answerButtonD = new JButton(answerD);
    JButton goOnButton = new JButton("Fortsätt");

    List<JButton> buttonList = List.of(answerButtonA, answerButtonB, answerButtonC, answerButtonD);

    JLabel playerEmojiLabelA = new JLabel(new ImageIcon("Pictures/CuteHipster.png"));
    JLabel playerEmojiLabelB = new JLabel(new ImageIcon("Pictures/CuteHeadphones.png"));
    JLabel categoryImageLabel = new JLabel(new ImageIcon());
    JLabel categoryTextLabel;
    JPanel userNamePointsPanelA = new JPanel(new GridLayout(2, 1));
    JPanel userNamePointsPanelB = new JPanel(new GridLayout(2, 1));

    String userNameA = "PLayer A";
    JLabel userNameLabelA = new JLabel(userNameA, SwingConstants.CENTER);
    String userNameB = "Player B"; // test name
    JLabel userNameLabelB = new JLabel(userNameB, SwingConstants.CENTER);

    JLabel pointsLabelA = new JLabel("Points:", SwingConstants.CENTER);
    JLabel pointsLabelB = new JLabel("Points:", SwingConstants.CENTER);
    int currentQuestion = 1;
    JTextField infoField = new JTextField("Fråga " + currentQuestion, 43);

    ServerSideGame game = new ServerSideGame();
    Database database = new Database();
    GameSettings settings = new GameSettings();
    ResultsScreen resultsScreen;

    int numberOfQuestions;

    static String playerNumber;
    Boolean isAnswerCorrect = false; // testa
    Boolean isAnswered = false; // testa
    int currentPoint = 0;
    int currentRound = 0; // testa
    static String userName; // livsviktigt för att det ska fungera

    static String quizTitle;
    static String currentCategory = "Geography"; // Testkategori
    static int finalScore;
    int goOnButtonNumberClicked = 0;

    public int getCurrentRound() {
        return currentRound;
    }

    public static final Color LIGHT_BLUE = new Color(51, 153, 255);
    public static final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
    public static final Color VERY_LIGHT_GREEN = new Color(102, 255, 102);
    public static final Color LIGHT_GREEN = new Color(0, 255, 51);
    public static final Color GOLD = new Color(255, 204, 51);
    public static final Color VERY_LIGHT_RED = new Color(255, 102, 102);

    public GameScreen(String player) {
        playerNumber = player;
        System.out.println(playerNumber);

        numberOfQuestions = settings.getNumberOfQuestions(); // nuvarande 3

        categoryTextLabel = new JLabel(currentCategory, SwingConstants.CENTER);
        categoryTextLabel.setPreferredSize(new Dimension(135, 50));

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

        game.drawUpQuestion(questionLabel, buttonList); // metod som ritar upp fråga och svar

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

        //goOnButton.setVisible(false); // sätta till synlig när man har spelat klart tre rundor

        questionLabel.setBorder(new CompoundBorder(new LineBorder(LIGHT_BLUE, 10), new EtchedBorder(EtchedBorder.RAISED)));

        for (JButton button : buttonList) { // kopplar lyssnaren till svar-knappar
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

        // Här tilldelas det rätt av sida av skärmen till spelare 1 och spelare 2 beroende på vilken tråd de är
        if (playerNumber.equals("Player1")) {
            userNameLabelA.setText(userName);
        }
        if (playerNumber.equals("Player2")) {
            userNameLabelB.setText(userName);
        }

        setSize(410, 670);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    ActionListener listener = new ActionListener() { // anonym klass
        public void actionPerformed(ActionEvent e) {


            if (e.getSource() == answerButtonA || e.getSource() == answerButtonB || e.getSource() == answerButtonC ||
                    e.getSource() == answerButtonD) {
                if (currentQuestion <= numberOfQuestions) {
                    changeScore(isRightAnswer((JButton) e.getSource()), playerNumber); // ändrar antal poäng på spelarens poäng panel
                    paintRightOrFalseAnswer((JButton) e.getSource()); // målar knappar i olika färger beroende på om svaret är korrekt
                    currentQuestion++;
                }

                //isAnswered = true;
            }

            if (e.getSource() == goOnButton) {
                goOnButtonNumberClicked++;
                if (goOnButtonNumberClicked <= 3) {
                    if (currentQuestion <= numberOfQuestions) {
                        changeInfoField(); // ändrar information på info panelen längst ner
                        game.drawUpQuestion(questionLabel, buttonList);
                        for (JButton button : buttonList) {
                            button.setBackground(new JButton().getBackground());
                        }
                        revalidate();
                    } else { // (currentQuestion > numberOfQuestions)
                        changeInfoField();
                        questionLabel.setText("");
                        for (JButton button : buttonList) {
                            button.setBackground(new JButton().getBackground());
                            button.setText("");
                        }
                        JOptionPane.showMessageDialog(null, "Du har nu svarat på alla 3 frågorna. " +
                                "Click på Fortsätt för att gå vidare.");
                        revalidate();

                    }
                }
                else {
                    ResultsScreen.finalScore = finalScore;
                    setVisible(false);
                    resultsScreen = new ResultsScreen(playerNumber);
                }
            }
        }
    };

    // Test metod, måste testas noggrant
    public ImageIcon setSizeToFitLabel(JLabel label, String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image resizedImage = image.getScaledInstance(label.getWidth(), label.getHeight(),
                Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    // kontrollerar om svaret på den valda knappen är korrekt
    public boolean isRightAnswer(JButton clickedButton) {
        String givenAnswer = clickedButton.getText();
        return givenAnswer.equalsIgnoreCase(database.getCorrectAnswer());
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
        String rightAnswer = database.getCorrectAnswer();
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
            infoField.setText("Du har nu svarat på alla 3 frågorna. Click på Fortsätt för att gå vidare.");
            infoField.revalidate();
        }
    }

    public static void main(String[] args) {
        GameScreen game = new GameScreen("Player2");
    }
}