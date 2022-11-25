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
    JLabel theirTurnLabel = new JLabel("Deras tur",SwingConstants.CENTER);
    JPanel goOnPanel = new JPanel();
    JButton goOnButton = new JButton("Fortsätt");
    JPanel leftPlayerPanel = new JPanel(new BorderLayout());
    JPanel middleCategoryPanel = new JPanel();
    JPanel rightPlayerPanel = new JPanel(new BorderLayout());
    JPanel leftUserInfoPanel = new JPanel(new BorderLayout());
    JPanel rightUserInfoPanel = new JPanel(new BorderLayout());
    JPanel leftPlayerAnswersPanel = new JPanel();
    JPanel rightPlayerAnswersPanel = new JPanel();
    JLabel playerEmojiLabelA;
    JLabel playerEmojiLabelB;
    JLabel userNameLabelA;
    JLabel userNameLabelB = new JLabel("Player B",SwingConstants.CENTER );
    JTextField infoField = new JTextField("Här kommer det skrivas ut info till användare",43);
    GameScreen gameScreen;
    ServerSideGame game;
    static String quizTitle;
    public static final Color LIGHT_BLUE = new Color(51,153,255);
    public static final Color VERY_LIGHT_BLUE = new Color(51,204,255);
    public static final Color VERY_LIGHT_GREEN = new Color(102,255,102);
    public static final Color LIGHT_GREEN = new Color(0, 255, 51);
    public static final Color GOLD = new Color(255,204, 51);
    ImageIcon image;
    ImageIcon image2;
    static String userName = "David"; // testName
    //int numberOfCategories = game.getNumberOfRounds();
    static String playerNumber;
    static int finalScore;
    GameSettings gameSettings = new GameSettings();
    int numberOfCategories = gameSettings.numberOfRounds;
    List<JLabel> listOfLabelsPlayerA = new LinkedList<>();
    List<JLabel> listOfLabelsPlayerB = new LinkedList<>();



    public ResultsScreen(String player){
        playerNumber=player;

        setTitle(quizTitle);
        add(basePanel);

        theirTurnLabel.setPreferredSize(new Dimension(300,75));
        leftPlayerPanel.setPreferredSize(new Dimension(110,400));
        rightPlayerPanel.setPreferredSize(new Dimension(110,400));
        goOnPanel.setPreferredSize(new Dimension(300,75));

        leftUserInfoPanel.setPreferredSize(new Dimension(110,125));
        rightUserInfoPanel.setPreferredSize(new Dimension(110,125));

        theirTurnLabel.setForeground(GOLD);
        theirTurnLabel.setFont(new Font("Sans Serif", Font.BOLD,25));

        theirTurnLabel.setOpaque(true); //hjälpverktyg
        theirTurnLabel.setBackground(LIGHT_BLUE); //hjälpverktyg
        leftPlayerPanel.setBackground(Color.WHITE); //hjälpverktyg
        middleCategoryPanel.setBackground(VERY_LIGHT_BLUE); //hjälpverktyg
        rightPlayerPanel.setBackground(Color.WHITE); //hjälpverktyg
        goOnPanel.setBackground(LIGHT_BLUE); //hjälpverktyg

        basePanel.add(theirTurnLabel, BorderLayout.NORTH);
        basePanel.add(leftPlayerPanel, BorderLayout.WEST);
        basePanel.add(middleCategoryPanel, BorderLayout.CENTER);
        basePanel.add(rightPlayerPanel, BorderLayout.EAST);
        basePanel.add(goOnPanel, BorderLayout.SOUTH);

        goOnButton.setPreferredSize(new Dimension(250,40));
        goOnButton.setBorder(new LineBorder(Color.WHITE, 3));
        goOnButton.setBackground(LIGHT_GREEN);

        goOnPanel.add(goOnButton);
        goOnPanel.add(infoField);

        leftPlayerAnswersPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg
        rightPlayerAnswersPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg

        //image = getRandomImageIcon();
        //image2 = getRandomImageIcon();
        image = new ImageIcon("Pictures/CuteHipster.png");
        image2 = new ImageIcon("Pictures/CuteHeadphones.png");

        leftPlayerAnswersPanel = createDesiredNumberOfLabels(leftPlayerAnswersPanel,numberOfCategories);
        rightPlayerAnswersPanel = createDesiredNumberOfLabels(rightPlayerAnswersPanel,numberOfCategories);
        middleCategoryPanel = createDesiredNumberOfLabels(middleCategoryPanel,numberOfCategories);

        leftPlayerPanel.add(leftUserInfoPanel, BorderLayout.NORTH);
        leftPlayerPanel.add(leftPlayerAnswersPanel, BorderLayout.CENTER);
        rightPlayerPanel.add(rightUserInfoPanel, BorderLayout.NORTH);
        rightPlayerPanel.add(rightPlayerAnswersPanel, BorderLayout.CENTER);

        playerEmojiLabelA = new JLabel(image);
        userNameLabelA = new JLabel(userName,SwingConstants.CENTER);
        userNameLabelA.setForeground(Color.WHITE);
        userNameLabelA.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        playerEmojiLabelA.setOpaque(true);
        userNameLabelA.setOpaque(true);
        userNameLabelA.setBackground(LIGHT_BLUE);
        playerEmojiLabelA.setBackground(LIGHT_BLUE);

        leftUserInfoPanel.add(playerEmojiLabelA, BorderLayout.CENTER);
        leftUserInfoPanel.add(userNameLabelA, BorderLayout.SOUTH);

        playerEmojiLabelB = new JLabel(image2);
        userNameLabelB.setForeground(Color.WHITE);
        userNameLabelB.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        playerEmojiLabelB.setOpaque(true);
        userNameLabelB.setOpaque(true);
        userNameLabelB.setBackground(LIGHT_BLUE);
        playerEmojiLabelB.setBackground(LIGHT_BLUE);

        rightUserInfoPanel.add(playerEmojiLabelB, BorderLayout.CENTER);
        rightUserInfoPanel.add(userNameLabelB, BorderLayout.SOUTH);

        goOnButton.addActionListener(listener);

        setSize(410,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    ActionListener listener = new ActionListener() { // anonym klass
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == goOnButton){
                setVisible(false);
                gameScreen = new GameScreen(playerNumber);
            }
        }
    };

    public ImageIcon getRandomImageIcon(){
        int randomImage = (int) (Math.random() * 20 + 1);
        String filePath = "C:\\Users\\46762\\Desktop\\Pictures\\Bild" + randomImage + ".png";
        return new ImageIcon(filePath);
    }

    public JPanel createDesiredNumberOfLabels(JPanel panelToFill, int numberOfLabels){
        panelToFill.setLayout(new GridLayout(numberOfLabels,1));
        for (int i = 0; i < numberOfLabels; i++){
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setBackground(LIGHT_BLUE);
            label.setBorder(new LineBorder(VERY_LIGHT_BLUE,1));
            panelToFill.add(label);
            listOfLabelsPlayerA.add(label);
        }
        return panelToFill;
    }

    public JPanel createDesiredNumberOfPanels(JPanel panelToFill, int numberOfPanels){
        panelToFill.setLayout(new GridLayout(numberOfPanels,1));
        for (int i = 0; i < numberOfPanels; i++){
            JPanel panel = new JPanel();
            panel.setOpaque(true);
            panel.setBackground(VERY_LIGHT_BLUE);
            panel.setBorder(new LineBorder(LIGHT_BLUE,1));
            panelToFill.add(panel);
        }
        return panelToFill;
    }

    public ImageIcon setSizeToFitLabel(JLabel label, String imagePath){
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
        ResultsScreen results = new ResultsScreen(playerNumber);
    }
}