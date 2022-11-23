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

    ServerSideGame game = new ServerSideGame();

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
    JPanel lowerSouthPanel = new JPanel();

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
    JTextField infoField = new JTextField("Här kommer det skrivas ut info till användare",40);

    public static final Color LIGHT_BLUE = new Color(51,153,255);
    public static final Color VERY_LIGHT_BLUE = new Color(51,204,255);
    public static final Color LIGHT_GREEN = new Color(0, 255, 51);

    public GameScreen(){
        setTitle("QuizGame");
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
            game.currentQuestion++;
            game.isAnswered = true;
        }
    };

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
        GameScreen game = new GameScreen();
    }
}