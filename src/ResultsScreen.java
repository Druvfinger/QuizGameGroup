import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ResultsScreen extends JFrame {
    JPanel basePanel = new JPanel(new BorderLayout());
    JLabel theirTurnLabel = new JLabel("Spelarens tur", SwingConstants.CENTER); //
    JPanel middlePanel = new JPanel(new BorderLayout());
    JPanel goOnPanel = new JPanel(new BorderLayout());
    JButton goOnButton = new JButton("Fortsätt");
    JPanel leftPlayerPanel = new JPanel(new BorderLayout());
    JLabel totalsLabel = new JLabel("0 - 0", SwingConstants.CENTER);
    JPanel categoryPanel = new JPanel();
    JPanel rightPlayerPanel = new JPanel(new BorderLayout());
    JPanel leftUserInfoPanel = new JPanel(new BorderLayout());
    JPanel rightUserInfoPanel = new JPanel(new BorderLayout());
    JPanel leftPlayerAnswersPanel = new JPanel();
    JPanel rightPlayerAnswersPanel = new JPanel();
    JLabel playerEmojiLabelA = new JLabel(new ImageIcon("Pictures/CuteHipster.png"));
    JLabel playerEmojiLabelB = new JLabel(new ImageIcon("Pictures/CuteHeadphones.png"));
    String userNameA = "Player A"; //
    JLabel userNameLabelA = new JLabel(userNameA,SwingConstants.CENTER);
    String userNameB = "Player B"; //
    JLabel userNameLabelB = new JLabel(userNameB, SwingConstants.CENTER);

    JTextField infoField = new JTextField("Här kommer det skrivas ut info till användare", 43);
    GameScreen gameScreen;
    ServerSideGame game;
    static String quizTitle;
    static String playerNumber;
    static String userName; // livsviktigt för att det ska fungera
    public static final Color LIGHT_BLUE = new Color(51, 153, 255);
    public static final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
    public static final Color VERY_LIGHT_GREEN = new Color(102, 255, 102);
    public static final Color LIGHT_GREEN = new Color(0, 255, 51);
    public static final Color GOLD = new Color(255, 204, 51);

    static int finalScore;
    GameSettings gameSettings = new GameSettings();
    int numberOfCategories = gameSettings.getNumberOfRounds();
    List<JLabel> listOfLabelsPlayerA = new LinkedList<>();
    List<JLabel> listOfLabelsPlayerB = new LinkedList<>();
    List<JLabel> listOfCategoryLabels = new LinkedList<>();
    String currentPlayerName;
    String opponentName;
    boolean myTurnToChoose = false;
    int currentRoundNumber = 1;


    public ResultsScreen(String player, String currentPlayerName, String opponentName) {
        playerNumber = player;
        this.currentPlayerName = currentPlayerName;
        this.opponentName = opponentName;

        setTitle(quizTitle);
        add(basePanel);

        theirTurnLabel.setPreferredSize(new Dimension(300, 75));
        theirTurnLabel.setForeground(GOLD);
        theirTurnLabel.setFont(new Font("Sans Serif", Font.BOLD, 25));

        leftPlayerPanel.setPreferredSize(new Dimension(110, 400));
        rightPlayerPanel.setPreferredSize(new Dimension(110, 400));
        goOnPanel.setPreferredSize(new Dimension(300, 75));

        leftUserInfoPanel.setPreferredSize(new Dimension(110, 125));
        rightUserInfoPanel.setPreferredSize(new Dimension(110, 125));

        theirTurnLabel.setOpaque(true); //hjälpverktyg
        theirTurnLabel.setBackground(LIGHT_BLUE); //hjälpverktyg
        leftPlayerPanel.setBackground(Color.WHITE); //hjälpverktyg
        middlePanel.setBackground(LIGHT_BLUE);
        categoryPanel.setBackground(VERY_LIGHT_BLUE); //hjälpverktyg
        rightPlayerPanel.setBackground(Color.WHITE); //hjälpverktyg
        goOnPanel.setBackground(LIGHT_BLUE); //hjälpverktyg

        basePanel.add(theirTurnLabel, BorderLayout.NORTH);
        basePanel.add(leftPlayerPanel, BorderLayout.WEST);
        basePanel.add(middlePanel, BorderLayout.CENTER);
        basePanel.add(rightPlayerPanel, BorderLayout.EAST);
        basePanel.add(goOnPanel, BorderLayout.SOUTH);

        totalsLabel.setPreferredSize(new Dimension(190, 100));
        totalsLabel.setForeground(Color.WHITE);
        totalsLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));

        middlePanel.add(totalsLabel, BorderLayout.NORTH);
        middlePanel.add(categoryPanel, BorderLayout.CENTER);

        goOnButton.setPreferredSize(new Dimension(250, 40));
        goOnButton.setBorder(new LineBorder(Color.WHITE, 3));
        goOnButton.setBackground(LIGHT_GREEN);

        goOnPanel.add(goOnButton, BorderLayout.CENTER);
        goOnPanel.add(infoField, BorderLayout.SOUTH);

        leftPlayerAnswersPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg
        rightPlayerAnswersPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg

        leftPlayerAnswersPanel = createDesiredNumberOfLabels(leftPlayerAnswersPanel, numberOfCategories, listOfLabelsPlayerA);
        rightPlayerAnswersPanel = createDesiredNumberOfLabels(rightPlayerAnswersPanel, numberOfCategories, listOfLabelsPlayerB);
        categoryPanel = createDesiredNumberOfLabels(categoryPanel, numberOfCategories, listOfCategoryLabels);

        leftPlayerPanel.add(leftUserInfoPanel, BorderLayout.NORTH);
        leftPlayerPanel.add(leftPlayerAnswersPanel, BorderLayout.CENTER);
        rightPlayerPanel.add(rightUserInfoPanel, BorderLayout.NORTH);
        rightPlayerPanel.add(rightPlayerAnswersPanel, BorderLayout.CENTER);

        userNameLabelA.setForeground(Color.WHITE);
        userNameLabelA.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        playerEmojiLabelA.setOpaque(true);
        userNameLabelA.setOpaque(true);
        userNameLabelA.setBackground(LIGHT_BLUE);
        playerEmojiLabelA.setBackground(LIGHT_BLUE);

        leftUserInfoPanel.add(playerEmojiLabelA, BorderLayout.CENTER);
        leftUserInfoPanel.add(userNameLabelA, BorderLayout.SOUTH);

        userNameLabelB.setForeground(Color.WHITE);
        userNameLabelB.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        playerEmojiLabelB.setOpaque(true);
        userNameLabelB.setOpaque(true);
        userNameLabelB.setBackground(LIGHT_BLUE);
        playerEmojiLabelB.setBackground(LIGHT_BLUE);

        rightUserInfoPanel.add(playerEmojiLabelB, BorderLayout.CENTER);
        rightUserInfoPanel.add(userNameLabelB, BorderLayout.SOUTH);

        goOnButton.addActionListener(listener);

        //Placerar namn/spelare på panelen
        userNameLabelA.setText(currentPlayerName);
        userNameLabelB.setText(opponentName);

        setSize(410, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    ActionListener listener = new ActionListener() { // anonym klass
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == goOnButton && theirTurnLabel.getText().equals("Din tur")) {
                Client.outWriter.println("CHOOSING_CATEGORY " + playerNumber);
            } else if (e.getSource() == goOnButton && theirTurnLabel.getText().equals("Time to play!")) {
                Client.outWriter.println("READY_TO_ANSWER " + playerNumber);
            }
        }
    };

    public ImageIcon getRandomImageIcon() {
        int randomImage = (int) (Math.random() * 20 + 1);
        String filePath = "C:\\Users\\46762\\Desktop\\Pictures\\Bild" + randomImage + ".png";
        return new ImageIcon(filePath);
    }

    public JPanel createDesiredNumberOfLabels(JPanel panelToFill, int numberOfLabels, List<JLabel> lista) {
        panelToFill.setLayout(new GridLayout(numberOfLabels, 1));
        for (int i = 0; i < numberOfLabels; i++) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(LIGHT_BLUE);
            label.setBorder(new LineBorder(VERY_LIGHT_BLUE, 1));
            panelToFill.add(label);
            lista.add(label);
        }
        return panelToFill;
    }

    public JPanel createDesiredNumberOfPanels(JPanel panelToFill, int numberOfPanels) {
        panelToFill.setLayout(new GridLayout(numberOfPanels, 1));
        for (int i = 0; i < numberOfPanels; i++) {
            JPanel panel = new JPanel();
            panel.setOpaque(true);
            panel.setBackground(VERY_LIGHT_BLUE);
            panel.setBorder(new LineBorder(LIGHT_BLUE, 1));
            panelToFill.add(panel);
        }
        return panelToFill;
    }

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

    public static void main(String[] args) {
        ResultsScreen results = new ResultsScreen("Player2", "David", "Anakin"); // OBS! Testparametrar
    }
}