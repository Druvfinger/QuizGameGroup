import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.List;

public class GameScreen extends JFrame {
    GameSettings gameSettings = new GameSettings();
    int numberOfQuestions;
    static String playerNumber;
    boolean isAnswered = false;
    int currentPoint = 0;
    static String userName;
    static String quizTitle;
    String currentCategory;
    static int finalScore;
    String totalPointsThisRound;
    String currentPlayerName;
    String opponentName;
    String answerA = "Answer A";
    String answerB = "Answer B";
    String answerC = "Answer C";
    String answerD = "Answer D";
    static int currentQuestion = 1;
    List<JButton> buttonList;
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
        goOnButton = new JButton("Continue");
        goOnButton.setEnabled(false);

        buttonList = List.of(answerButtonA, answerButtonB, answerButtonC, answerButtonD);

        JLabel playerEmojiLabelA;
        JLabel playerEmojiLabelB;
        if (playerNumber.equals("Player1")) {
            playerEmojiLabelA = new JLabel(new ImageIcon("Pictures/CuteHipster.png"));
            playerEmojiLabelB = new JLabel(new ImageIcon("Pictures/CuteHeadphones.png"));
        }
        else {
            playerEmojiLabelB = new JLabel(new ImageIcon("Pictures/CuteHipster.png"));
            playerEmojiLabelA = new JLabel(new ImageIcon("Pictures/CuteHeadphones.png"));
        }
        JLabel categoryImageLabel = new JLabel(new ImageIcon());
        JPanel userNamePointsPanelA = new JPanel(new GridLayout(2, 1));
        JPanel userNamePointsPanelB = new JPanel(new GridLayout(2, 1));

        String userNameA = "PLayer A";
        JLabel userNameLabelA = new JLabel(userNameA, SwingConstants.CENTER);
        String userNameB = "Player B";
        JLabel userNameLabelB = new JLabel(userNameB, SwingConstants.CENTER);

        pointsLabelA = new JLabel("Points:", SwingConstants.CENTER);
        pointsLabelB = new JLabel("Points:", SwingConstants.CENTER);

        infoField = new JTextField("Question " + currentQuestion, 43);

        categoryTextLabel = new JLabel(currentCategory, SwingConstants.CENTER);
        categoryTextLabel.setPreferredSize(new Dimension(135, 50));
        categoryTextLabel.setForeground(Color.WHITE);
        categoryTextLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        setTitle(quizTitle);
        add(basePanel);

        basePanel.add(upperPanel);
        basePanel.add(lowerPanel);

        lowerPanel.setBackground(Constants.LIGHT_BLUE);
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        lowerPanel.add(Box.createRigidArea(new Dimension(1, 5)));
        lowerPanel.add(lowerNorthPanel);
        lowerPanel.add(Box.createRigidArea(new Dimension(1, 5)));
        lowerPanel.add(lowerCenterPanel);
        lowerPanel.add(Box.createRigidArea(new Dimension(1, 5)));
        lowerPanel.add(lowerSouthPanel);

        lowerNorthPanel.setBackground(Constants.LIGHT_BLUE);
        lowerCenterPanel.setBackground(Constants.LIGHT_BLUE);
        lowerSouthPanel.setBackground(Constants.LIGHT_BLUE);

        emptyPanel.setBackground(Constants.LIGHT_BLUE);
        emptyPanel.setPreferredSize(new Dimension(400, 25));

        goOnButton.setPreferredSize(new Dimension(250, 40));
        goOnButton.setBorder(new LineBorder(Color.WHITE, 3));
        goOnButton.setBackground(Constants.LIGHT_GREEN);

        lowerNorthPanel.add(answerButtonA);
        lowerNorthPanel.add(answerButtonB);
        lowerCenterPanel.add(answerButtonC);
        lowerCenterPanel.add(answerButtonD);

        lowerSouthPanel.add(emptyPanel);
        lowerSouthPanel.add(goOnButton);
        lowerSouthPanel.add(infoField);

        categoryInfoPanel.setBackground(Constants.LIGHT_BLUE);
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

        questionLabel.setBorder(new CompoundBorder(new LineBorder(Constants.LIGHT_BLUE, 10), new EtchedBorder(EtchedBorder.RAISED)));

        for (JButton button : buttonList) {
            button.addActionListener(listener);
        }

        goOnButton.addActionListener(listener);

        playerEmojiLabelA.setOpaque(true);
        playerEmojiLabelA.setBackground(Constants.LIGHT_BLUE);
        playerEmojiLabelB.setOpaque(true);
        playerEmojiLabelB.setBackground(Constants.LIGHT_BLUE);

        userNamePointsPanelA.setOpaque(true);
        userNamePointsPanelA.setBackground(Constants.LIGHT_BLUE);
        userNamePointsPanelB.setOpaque(true);
        userNamePointsPanelB.setBackground(Constants.LIGHT_BLUE);

        userNameLabelA.setFont(new Font("Sans Serif", Font.BOLD, 20));
        userNameLabelA.setForeground(Constants.GOLD);
        userNameLabelB.setFont(new Font("Sans Serif", Font.BOLD, 20));
        userNameLabelB.setForeground(Constants.GOLD);

        userNamePointsPanelA.add(userNameLabelA);
        userNamePointsPanelA.add(pointsLabelA);
        userNamePointsPanelB.add(userNameLabelB);
        userNamePointsPanelB.add(pointsLabelB);

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

        numberOfQuestions = settings.getNumberOfQuestions();
        setUpGameScreenGUI();
    }

    ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == answerButtonA || e.getSource() == answerButtonB || e.getSource() == answerButtonC ||
                    e.getSource() == answerButtonD) {
                changeScore(isRightAnswer((JButton) e.getSource()), playerNumber);
                paintRightOrFalseAnswer((JButton) e.getSource());
                for (JButton element : buttonList) {
                    element.setEnabled(false);
                }
                currentQuestion++;
                isAnswered = true;
                Client.outWriter.println("I_ANSWERED" + playerNumber);
            }

            if (e.getSource() == goOnButton && isAnswered) {
                isAnswered = false;
                for (JButton element : buttonList) {
                    element.setEnabled(true);
                }
                if (currentQuestion <= numberOfQuestions) {
                    Client.outWriter.println("NEXT_QUESTION? " + playerNumber);
                } else if (!wantToGoForward) {
                    changeInfoField();
                    questionLabel.setText("");
                    for (JButton button : buttonList) {
                        button.setVisible(false);
                    }
                    revalidate();
                    totalPointsThisRound = pointsLabelA.getText();
                    Client.outWriter.println("BACK_TO_RESULTS " + playerNumber + " " + totalPointsThisRound);
                    wantToGoForward = true;
                    isAnswered = true;
                } else {
                        Client.outWriter.println("SHOW_ME_RESULTS " + playerNumber);
                        isAnswered = false;
                        ServerSideGame.currentRound++;
                }
            }
        }
    };

    public boolean isRightAnswer(JButton clickedButton) {
        String givenAnswer = clickedButton.getText();
        return givenAnswer.equalsIgnoreCase(rightAnswer);
    }

    public void paintRightOrFalseAnswer(JButton clickedButton) {
        if (isRightAnswer(clickedButton)) {
            clickedButton.setBackground(Constants.VERY_LIGHT_GREEN);
        } else {
            clickedButton.setBackground(Constants.VERY_LIGHT_RED);
            findButtonWithRightAnswer().setBackground(Constants.VERY_LIGHT_GREEN);
        }
    }

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

    public void changeScore(boolean isCorrectAnswer, String playerNumber) {
        if (isCorrectAnswer) {
            pointsLabelA.setText("Points: " + ++currentPoint + "/" + gameSettings.getNumberOfQuestions());
            pointsLabelA.revalidate();
            finalScore = currentPoint;
            System.out.println(playerNumber + " Total points: " + finalScore);
        } else {
            pointsLabelA.setText("Points: " + currentPoint + "/" + gameSettings.getNumberOfQuestions());
            pointsLabelA.revalidate();
            finalScore = currentPoint;
            System.out.println(playerNumber + " Total points: " + finalScore);
        }
    }

    public void changeInfoField() {
        if (currentQuestion <= numberOfQuestions) {
            infoField.setText("Question " + currentQuestion);
            infoField.revalidate();
        } else {
            infoField.setText("You have answer all " + gameSettings.getNumberOfQuestions() +
                    "questions. Click on Continue to proceed.");
            infoField.revalidate();
        }
    }
}