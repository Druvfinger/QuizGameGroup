import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WelcomeScreen extends JFrame {
    static String userName;
    static String quizTitle;
    static String playerNumber;
    JButton newGameButton;
    JButton userNameSubmitButton;
    JTextField userNameTextField;
    JTextField userInfoTextField;
    JPanel userNamePanel;
    JLabel userNameLabel;


    public void setUpWelcomeScreenGUI() {

        JPanel newGameButtonAndInfoFieldPanel = new JPanel(new BorderLayout());
        JLabel welcomeText = new JLabel("Welcome to our Quiz Game!", SwingConstants.CENTER);
        JLabel emojiLabel = new JLabel(new ImageIcon("Pictures/YellowSmart.png"));
        userNamePanel = new JPanel(new GridLayout(1, 3)); //
        userNameLabel = new JLabel("Write your username:", SwingConstants.CENTER);
        userNameTextField = new JTextField(20);
        userInfoTextField = new JTextField("");
        userNameSubmitButton = new JButton("Submit");
        newGameButton = new JButton("New Game");

        JPanel backPanel = new JPanel(new BorderLayout());
        JPanel userPanel = new JPanel(new BorderLayout());
        JPanel emptyPanel = new JPanel();


        setTitle("QuizGame");
        add(backPanel);
        backPanel.add(userPanel, BorderLayout.NORTH);
        backPanel.add(emptyPanel, BorderLayout.CENTER);
        backPanel.add(newGameButtonAndInfoFieldPanel, BorderLayout.SOUTH);

        userPanel.add(welcomeText, BorderLayout.NORTH);
        userPanel.add(emojiLabel, BorderLayout.CENTER);
        userPanel.add(userNamePanel, BorderLayout.SOUTH);

        welcomeText.setForeground(Constants.GOLD);
        welcomeText.setFont(new Font("Sans Serif", Font.BOLD, 20));

        userNameLabel.setBorder(new LineBorder(Color.GRAY, 1));

        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameTextField);
        userNamePanel.add(userNameSubmitButton);

        userPanel.setBackground(Constants.LIGHT_BLUE);
        emptyPanel.setBackground(Constants.LIGHT_BLUE);
        newGameButtonAndInfoFieldPanel.setBackground(Constants.LIGHT_BLUE);
        userNameLabel.setOpaque(true);
        userNameLabel.setBackground(Color.WHITE);

        newGameButton.setPreferredSize(new Dimension(250, 50));
        newGameButton.setBorder(new LineBorder(Color.WHITE, 5));
        newGameButton.setBackground(Constants.VERY_LIGHT_BLUE);

        newGameButtonAndInfoFieldPanel.add(newGameButton, BorderLayout.NORTH);
        newGameButtonAndInfoFieldPanel.add(userInfoTextField, BorderLayout.SOUTH);

        userNameSubmitButton.setEnabled(false);
        userNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                userNameSubmitButton.setEnabled(!userNameTextField.getText().equals(""));
            }
        });

        userNameSubmitButton.addActionListener(listener);
        newGameButton.addActionListener(listener);

        newGameButton.setVisible(false);

        setSize(410, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(900, 250);
        //setLocationRelativeTo(null);
        setVisible(true);
    }

    public WelcomeScreen(String player) { // Kan man ta in en client ?
        playerNumber = player;
        setUpWelcomeScreenGUI();
    }

    ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == newGameButton) {
                Client.outWriter.println("READY_TO_PLAY " + playerNumber);
            }
            if (e.getSource() == userNameSubmitButton) {
                userName = userNameTextField.getText();
                GameScreen.userName = userName; // testa om man tar bort det
                ResultsScreen.userName = userName; // testa om man tar bort det

                rePaintWelcomeScreenIfNameSubmitted();

                Client.outWriter.println("MY_NAME " + userName);

                Client.outWriter.println("ENTERED_NAME " + playerNumber);
            }
        }
    };

    public void rePaintWelcomeScreenIfNameSubmitted() {
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