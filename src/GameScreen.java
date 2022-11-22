import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameScreen extends JFrame{
    String answerA = "Svar A";
    String answerB = "Svar B";
    String answerC = "Svar C";
    String answerD = "Svar D";

    Boolean isAnswerCorrect = false;
    Boolean isAnswered = false;

    ServerSideGame game = new ServerSideGame();
    int currentQuestion = 0;// öka i ae för varje clickad knapp

    String question = "Här kommer det visas fråga";
    String userNameA = "Player A";
    String userNameB = "Player B";
    String categoryName = "Kategorinamn";
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
    JPanel lowerSouthPanel = new JPanel(new GridLayout(2,1));

    JButton answerButtonA = new JButton(answerA);
    JButton answerButtonB = new JButton(answerB);
    JButton answerButtonC = new JButton(answerC);
    JButton answerButtonD = new JButton(answerD);
    JButton goOnButton = new JButton("Fortsätt");

    List<JButton> buttonList = List.of(answerButtonA,answerButtonB,answerButtonC,answerButtonD);

    JLabel playerEmojiLabelA = new JLabel(new ImageIcon());
    JLabel playerEmojiLabelB = new JLabel(new ImageIcon());
    JLabel categoryImageLabel = new JLabel(new ImageIcon());
    JLabel categoryTextLabel = new JLabel();
    JPanel userNamePointsPanelA = new JPanel(new BorderLayout());
    JPanel userNamePointsPanelB = new JPanel(new BorderLayout());

    public GameScreen(){
        setTitle("QuizGame");
        add(basePanel);
        basePanel.add(upperPanel);
        upperPanel.setBackground(Color.pink); // hjälpverktyg
        basePanel.add(lowerPanel);
        lowerPanel.setBackground(Color.CYAN); // hjälpverktyg

        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        lowerPanel.add(Box.createRigidArea(new Dimension(1,5)));
        lowerPanel.add(lowerNorthPanel);
        lowerPanel.add(Box.createRigidArea(new Dimension(1,5)));
        lowerPanel.add(lowerCenterPanel);
        lowerPanel.add(Box.createRigidArea(new Dimension(1,5)));
        lowerPanel.add(lowerSouthPanel);

        lowerNorthPanel.setBackground(Color.CYAN);// hjälpverktyg
        lowerCenterPanel.setBackground(Color.CYAN);// hjälpverktyg
        lowerSouthPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg

        lowerNorthPanel.add(answerButtonA);
        lowerNorthPanel.add(answerButtonB);
        lowerCenterPanel.add(answerButtonC);
        lowerCenterPanel.add(answerButtonD);
        lowerSouthPanel.add(emptyPanel);
        lowerSouthPanel.add(goOnButton);

        leftUserInfoPanel.setBackground(Color.LIGHT_GRAY);//hjälpverktyg
        categoryInfoPanel.setBackground(Color.PINK);//hjälpverktyg
        rightUserInfoPanel.setBackground(Color.LIGHT_GRAY);//hjälpverktyg
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

        goOnButton.setVisible(false); // sätta till synlig när man har spelat klart tre rundor

        answerButtonA.addActionListener(listener);
        answerButtonB.addActionListener(listener);
        answerButtonC.addActionListener(listener);
        answerButtonD.addActionListener(listener);
        goOnButton.addActionListener(listener);

        setSize(400,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    ActionListener listener = new ActionListener() { // anonym klass
        @Override
        public void actionPerformed(ActionEvent e) {
            currentQuestion++;
            isAnswered = true;
        }
    };

    public static void main(String[] args) {
        GameScreen game = new GameScreen();
    }
}