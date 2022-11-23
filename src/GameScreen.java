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

public class GameScreen extends JFrame{
    String answerA = "Svar A";
    String answerB = "Svar B";
    String answerC = "Svar C";
    String answerD = "Svar D";

    Boolean isAnswerCorrect = false;
    Boolean isAnswered = false;
    int currentQuestion = 0;
    int currentRound = 0;
    static String userName = "David"; // testnamn
    static String quizTitle;
    static String currentCategory = "Geography"; // Testkategori
    ResultsScreen resultsScreen;

    public int getCurrentRound() {
        return currentRound;
    }

    ServerSideGame game = new ServerSideGame();

    String question = "Här kommer det visas fråga";

    String userNameB = "Player B";
    JPanel basePanel = new JPanel(new GridLayout(2,1));
    JPanel upperPanel = new JPanel(new GridLayout(2,1));
    JPanel infoPanel = new JPanel(new GridLayout(1,3));
    JPanel emptyPanel = new JPanel();

    JPanel leftUserInfoPanel = new JPanel(new GridLayout(2,1));
    JPanel categoryInfoPanel = new JPanel(new BorderLayout());
    JPanel rightUserInfoPanel = new JPanel(new GridLayout(2,1));
    JLabel questionLabel = new JLabel(question, SwingConstants.CENTER);

    JPanel lowerPanel = new JPanel();
    JPanel lowerNorthPanel = new JPanel(new GridLayout(1,2,10,10));
    JPanel lowerCenterPanel = new JPanel(new GridLayout(1,2,10,10));
    JPanel lowerSouthPanel = new JPanel();

    JButton answerButtonA = new JButton(answerA);
    JButton answerButtonB = new JButton(answerB);
    JButton answerButtonC = new JButton(answerC);
    JButton answerButtonD = new JButton(answerD);
    JButton goOnButton = new JButton("Fortsätt");

    List<JButton> buttonList = List.of(answerButtonA,answerButtonB,answerButtonC,answerButtonD);

    JLabel playerEmojiLabelA = new JLabel(new ImageIcon("Pictures/CuteHipster.png"));
    JLabel playerEmojiLabelB = new JLabel(new ImageIcon("Pictures/CuteHeadphones.png"));
    JLabel categoryImageLabel = new JLabel(new ImageIcon());
    JLabel categoryTextLabel;
    JPanel userNamePointsPanelA = new JPanel(new GridLayout(2,1));
    JPanel userNamePointsPanelB = new JPanel(new GridLayout(2,1));
    JLabel userNameLabelA = new JLabel(userName,SwingConstants.CENTER);
    JLabel userNameLabelB = new JLabel(userNameB,SwingConstants.CENTER);
    JLabel pointsLabelA = new JLabel("Points:", SwingConstants.CENTER);
    JLabel pointsLabelB = new JLabel("Points:", SwingConstants.CENTER);
    JTextField infoField = new JTextField("Här kommer det skrivas ut info till användare",40);
    Database database = new Database();

    public static final Color LIGHT_BLUE = new Color(51, 153, 255);
    public static final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
    public static final Color VERY_LIGHT_GREEN = new Color(102, 255, 102);
    public static final Color LIGHT_GREEN = new Color(0, 255, 51);
    public static final Color GOLD = new Color(255, 204, 51);

    public GameScreen(){
        categoryTextLabel = new JLabel(currentCategory,SwingConstants.CENTER);
        categoryTextLabel.setPreferredSize(new Dimension(135,50));

        setTitle(quizTitle);
        add(basePanel);
        basePanel.add(upperPanel);
        upperPanel.setBackground(LIGHT_BLUE); // hjälpverktyg
        basePanel.add(lowerPanel);
        lowerPanel.setBackground(LIGHT_BLUE); // hjälpverktyg

        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        lowerPanel.add(Box.createRigidArea(new Dimension(1,5)));
        lowerPanel.add(lowerNorthPanel);
        lowerPanel.add(Box.createRigidArea(new Dimension(1,5)));
        lowerPanel.add(lowerCenterPanel);
        lowerPanel.add(Box.createRigidArea(new Dimension(1,5)));
        lowerPanel.add(lowerSouthPanel);

        lowerNorthPanel.setBackground(LIGHT_BLUE);// hjälpverktyg
        lowerCenterPanel.setBackground(LIGHT_BLUE);// hjälpverktyg
        lowerSouthPanel.setBackground(LIGHT_BLUE);// hjälpverktyg
        emptyPanel.setBackground(LIGHT_BLUE);
        emptyPanel.setPreferredSize(new Dimension(400,25));

        goOnButton.setPreferredSize(new Dimension(250,40));
        goOnButton.setBorder(new LineBorder(Color.WHITE, 3));
        goOnButton.setBackground(LIGHT_GREEN);

        game.drawUpQuestion(questionLabel,buttonList);

        lowerNorthPanel.add(answerButtonA);
        lowerNorthPanel.add(answerButtonB);
        lowerCenterPanel.add(answerButtonC);
        lowerCenterPanel.add(answerButtonD);

        lowerSouthPanel.add(emptyPanel);
        lowerSouthPanel.add(goOnButton);
        lowerSouthPanel.add(infoField);

        leftUserInfoPanel.setBackground(Color.WHITE);//hjälpverktyg
        categoryInfoPanel.setBackground(LIGHT_BLUE);//hjälpverktyg
        rightUserInfoPanel.setBackground(Color.WHITE);//hjälpverktyg
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
        categoryInfoPanel.add(categoryTextLabel,BorderLayout.SOUTH);
        rightUserInfoPanel.add(playerEmojiLabelB);
        rightUserInfoPanel.add(userNamePointsPanelB);

        //goOnButton.setVisible(false); // sätta till synlig när man har spelat klart tre rundor

        //questionLabel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        //questionLabel.setBorder(new CompoundBorder(new LineBorder(Color.WHITE,10), new EtchedBorder(EtchedBorder.RAISED)));
        questionLabel.setBorder(new CompoundBorder(new LineBorder(LIGHT_BLUE,10), new EtchedBorder(EtchedBorder.RAISED)));

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

        userNameLabelA.setFont(new Font("Sans Serif", Font.BOLD,20));
        userNameLabelA.setForeground(GOLD);
        userNameLabelB.setFont(new Font("Sans Serif", Font.BOLD,20));
        userNameLabelB.setForeground(GOLD);

        userNamePointsPanelA.add(userNameLabelA);
        userNamePointsPanelA.add(pointsLabelA);
        userNamePointsPanelB.add(userNameLabelB);
        userNamePointsPanelB.add(pointsLabelB);

        setSize(410,670);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    ActionListener listener = new ActionListener() { // anonym klass
        public void actionPerformed(ActionEvent e) {
            currentQuestion++;
            isAnswered = true;
            if (e.getSource() == answerButtonA || e.getSource() == answerButtonB || e.getSource() == answerButtonC ||
            e.getSource() == answerButtonD){
                System.out.println(isRightAnswer((JButton) e.getSource()));

            }

            if (e.getSource() == goOnButton){
                setVisible(false);
                ResultsScreen resultsScreen = new ResultsScreen();
            }
        }
    };

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

    public boolean isRightAnswer (JButton clickedButton){
        String givenAnswer = clickedButton.getText();
        return givenAnswer.equalsIgnoreCase(database.getCorrectAnswer());
    }

    public static void main(String[] args) {
        GameScreen game = new GameScreen();
    }
}