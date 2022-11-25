import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {

    //klass för en välkomstskärm
    JPanel backPanel = new JPanel(new BorderLayout());
    JPanel userPanel = new JPanel(new BorderLayout());
    JPanel emptyPanel = new JPanel();
    JLabel welcomeText = new JLabel("Welcome to our Quiz Game!", SwingConstants.CENTER);
    JLabel emojiLabel = new JLabel(new ImageIcon("Pictures/YellowSmart.png"));
    JPanel userNamePanel = new JPanel(new GridLayout(1, 3));
    JLabel userNameLabel = new JLabel("Write your username:", SwingConstants.CENTER);
    JTextField userNameTextField = new JTextField(20);
    JButton userNameSubmitButton = new JButton("Submit");
    JButton newGameButton = new JButton("New Game");
    static String userName;
    static String quizTitle;
    static String player;
    ChooseCategoryScreen categoryScreen;
    public static final Color LIGHT_BLUE = new Color(51, 153, 255);
    public static final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
    public static final Color VERY_LIGHT_GREEN = new Color(102, 255, 102);
    public static final Color LIGHT_GREEN = new Color(0, 255, 51);
    public static final Color GOLD = new Color(255, 204, 51);

    ServerSideGame game;

    public WelcomeScreen() {
        setTitle("QuizGame");
        add(backPanel);
        backPanel.add(userPanel, BorderLayout.NORTH);
        backPanel.add(emptyPanel, BorderLayout.CENTER);
        backPanel.add(newGameButton, BorderLayout.SOUTH);

        userPanel.add(welcomeText, BorderLayout.NORTH);
        userPanel.add(emojiLabel, BorderLayout.CENTER);
        userPanel.add(userNamePanel, BorderLayout.SOUTH);

        welcomeText.setForeground(GOLD);
        welcomeText.setFont(new Font("Sans Serif", Font.BOLD, 20));

        userNameLabel.setBorder(new LineBorder(Color.GRAY, 1));

        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameTextField);
        userNamePanel.add(userNameSubmitButton);

        userPanel.setBackground(LIGHT_BLUE);
        emptyPanel.setBackground(LIGHT_BLUE);
        userNameLabel.setOpaque(true);
        userNameLabel.setBackground(Color.WHITE);

        newGameButton.setPreferredSize(new Dimension(250, 50));
        newGameButton.setBorder(new LineBorder(Color.WHITE, 5));
        newGameButton.setBackground(VERY_LIGHT_BLUE);

        userNameSubmitButton.addActionListener(listener);
        newGameButton.addActionListener(listener);

        setSize(410, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    ActionListener listener = new ActionListener() { // anonym klass
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == newGameButton) {
                if (userName != null) {
                    setVisible(false);
                    categoryScreen = new ChooseCategoryScreen();
                } else JOptionPane.showMessageDialog(null, "Please enter your username before you proceed.");
            }
            if (e.getSource() == userNameSubmitButton) {
                userName = userNameTextField.getText();
                GameScreen.userName = userName;
                ResultsScreen.userName = userName;
                quizTitle = "Quiz Game " + userName;
                ChooseCategoryScreen.quizTitle = quizTitle;
                GameScreen.quizTitle = quizTitle;
                ResultsScreen.quizTitle = quizTitle;
                userNamePanel.removeAll();
                userNameLabel.setText("Welcome " + userName + " and Good Luck!");
                userNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));
                userNameLabel.setBorder(new EmptyBorder(2, 20, 5, 20));
                userNamePanel.add(userNameLabel);
                userNamePanel.revalidate();
                System.out.println(userName + " is connected.");
            }
        }
    };


    public static void main(String[] args) {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
    }
}