
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class ResultsScreen extends JFrame {
    JButton goOnButton;
    static String quizTitle;
    static String playerNumber;
    static String userName; // livsviktigt för att det ska fungera
//    static int finalScore; // kommer att användas senare på ResultsScreen
    GameSettings gameSettings = new GameSettings();
    String currentPlayerName;
    String opponentName;
    JLabel theirTurnLabel;
    List<JLabel> listOfLabelsPlayerA = new LinkedList<>();
    List<JLabel> listOfLabelsPlayerB = new LinkedList<>();
    List<JLabel> listOfCategoryLabels = new LinkedList<>();
    JTextField infoField;
    JPanel categoryPanel;
    JLabel pointsALabel;
    JLabel pointsBLabel;

    public void setUpResultScreenGUI() {

        JPanel basePanel = new JPanel(new BorderLayout());
        theirTurnLabel = new JLabel("Players Turn", SwingConstants.CENTER); //
        JPanel middlePanel = new JPanel(new BorderLayout());
        JPanel goOnPanel = new JPanel(new BorderLayout());
        goOnButton = new JButton("Continue");
        JPanel leftPlayerPanel = new JPanel(new BorderLayout());
        JPanel totalsPanel = new JPanel(new GridLayout(1, 3)); // NYTT
        pointsALabel = new JLabel("0", SwingConstants.RIGHT);
        JLabel dashLabel = new JLabel("-", SwingConstants.CENTER);
        pointsBLabel = new JLabel("0", SwingConstants.LEFT);
        categoryPanel = new JPanel();
        JPanel rightPlayerPanel = new JPanel(new BorderLayout());
        JPanel leftUserInfoPanel = new JPanel(new BorderLayout());
        JPanel rightUserInfoPanel = new JPanel(new BorderLayout());
        JPanel leftPlayerAnswersPanel = new JPanel();
        JPanel rightPlayerAnswersPanel = new JPanel();
        JLabel playerEmojiLabelA = new JLabel(new ImageIcon("Pictures/CuteHipster.png"));
        JLabel playerEmojiLabelB = new JLabel(new ImageIcon("Pictures/CuteHeadphones.png"));
        String userNameA = "Player A";
        JLabel userNameLabelA = new JLabel(userNameA, SwingConstants.CENTER);
        String userNameB = "Player B";
        JLabel userNameLabelB = new JLabel(userNameB, SwingConstants.CENTER);

        infoField = new JTextField("Info for user shown here!", 43);

        setTitle(quizTitle);
        add(basePanel);

        theirTurnLabel.setPreferredSize(new Dimension(300, 75));
        theirTurnLabel.setForeground(Constants.GOLD);
        theirTurnLabel.setFont(new Font("Sans Serif", Font.BOLD, 25));

        leftPlayerPanel.setPreferredSize(new Dimension(110, 400));
        rightPlayerPanel.setPreferredSize(new Dimension(110, 400));
        goOnPanel.setPreferredSize(new Dimension(300, 75));

        leftUserInfoPanel.setPreferredSize(new Dimension(110, 125));
        rightUserInfoPanel.setPreferredSize(new Dimension(110, 125));

        theirTurnLabel.setOpaque(true); //hjälpverktyg
        theirTurnLabel.setBackground(Constants.LIGHT_BLUE); //hjälpverktyg
        leftPlayerPanel.setBackground(Color.WHITE); //hjälpverktyg
        middlePanel.setBackground(Constants.LIGHT_BLUE);
        categoryPanel.setBackground(Constants.VERY_LIGHT_BLUE); //hjälpverktyg
        rightPlayerPanel.setBackground(Color.WHITE); //hjälpverktyg
        goOnPanel.setBackground(Constants.LIGHT_BLUE); //hjälpverktyg
        totalsPanel.setBackground(Constants.LIGHT_BLUE);

        basePanel.add(theirTurnLabel, BorderLayout.NORTH);
        basePanel.add(leftPlayerPanel, BorderLayout.WEST);
        basePanel.add(middlePanel, BorderLayout.CENTER);
        basePanel.add(rightPlayerPanel, BorderLayout.EAST);
        basePanel.add(goOnPanel, BorderLayout.SOUTH);

        totalsPanel.setPreferredSize(new Dimension(190, 100));
        // skriva en metod för följande rader (koden upprepas):
        pointsALabel.setForeground(Color.WHITE);
        pointsALabel.setFont(new Font("Sans Serif", Font.BOLD, 30));
        dashLabel.setForeground(Color.WHITE);
        dashLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));
        pointsBLabel.setForeground(Color.WHITE);
        pointsBLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));

        middlePanel.add(totalsPanel, BorderLayout.NORTH);
        middlePanel.add(categoryPanel, BorderLayout.CENTER);

        dashLabel.setPreferredSize(new Dimension(30, 100));

        totalsPanel.add(pointsALabel);
        totalsPanel.add(dashLabel);
        totalsPanel.add(pointsBLabel);

        goOnButton.setPreferredSize(new Dimension(250, 40));
        goOnButton.setBorder(new LineBorder(Color.WHITE, 3));
        goOnButton.setBackground(Constants.LIGHT_GREEN);

        goOnPanel.add(goOnButton, BorderLayout.CENTER);
        goOnPanel.add(infoField, BorderLayout.SOUTH);

        leftPlayerAnswersPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg
        rightPlayerAnswersPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg

        leftPlayerAnswersPanel = createDesiredNumberOfLabels(leftPlayerAnswersPanel, gameSettings.getNumberOfRounds(), listOfLabelsPlayerA);
        rightPlayerAnswersPanel = createDesiredNumberOfLabels(rightPlayerAnswersPanel, gameSettings.getNumberOfRounds(), listOfLabelsPlayerB);
        categoryPanel = createDesiredNumberOfLabels(categoryPanel, gameSettings.getNumberOfRounds(), listOfCategoryLabels);

        leftPlayerPanel.add(leftUserInfoPanel, BorderLayout.NORTH);
        leftPlayerPanel.add(leftPlayerAnswersPanel, BorderLayout.CENTER);
        rightPlayerPanel.add(rightUserInfoPanel, BorderLayout.NORTH);
        rightPlayerPanel.add(rightPlayerAnswersPanel, BorderLayout.CENTER);

        reSetLabels(userNameLabelA, playerEmojiLabelA, leftUserInfoPanel);
        reSetLabels(userNameLabelB, playerEmojiLabelB, rightUserInfoPanel);

        goOnButton.addActionListener(listener);

        //Placerar namn/spelare på panelen
        userNameLabelA.setText(currentPlayerName);
        userNameLabelB.setText(opponentName);

        setSize(410, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void reSetLabels(JLabel userNameLabel, JLabel playerEmojiLabel, JPanel userInfoPanel) {
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        playerEmojiLabel.setOpaque(true);
        userNameLabel.setOpaque(true);
        userNameLabel.setBackground(Constants.LIGHT_BLUE);
        playerEmojiLabel.setBackground(Constants.LIGHT_BLUE);

        userInfoPanel.add(playerEmojiLabel, BorderLayout.CENTER);
        userInfoPanel.add(userNameLabel, BorderLayout.SOUTH);
    }


    public ResultsScreen(String player, String currentPlayerName, String opponentName) {
        playerNumber = player;
        this.currentPlayerName = currentPlayerName;
        this.opponentName = opponentName;
        setUpResultScreenGUI();
    }

    ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Client.gameOver) {
                System.exit(0);
            }
            else if (e.getSource() == goOnButton && theirTurnLabel.getText().equals("Your Turn")) {
                Client.outWriter.println("CHOOSING_CATEGORY " + playerNumber);
            } else if (e.getSource() == goOnButton && theirTurnLabel.getText().equals("Time to play!")) {
                Client.outWriter.println("READY_TO_ANSWER " + playerNumber);
                GameScreen.currentQuestion = 1;
            }
        }

    };

    public JPanel createDesiredNumberOfLabels(JPanel panelToFill, int numberOfLabels, List<JLabel> lista) {
        panelToFill.setLayout(new GridLayout(numberOfLabels, 1));
        for (int i = 0; i < numberOfLabels; i++) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(Constants.LIGHT_BLUE);
            label.setBorder(new LineBorder(Constants.VERY_LIGHT_BLUE, 1));
            panelToFill.add(label);
            lista.add(label);
        }
        return panelToFill;
    }
}