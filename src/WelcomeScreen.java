import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame implements ActionListener {

    //klass för en välkomstskärmen
    JPanel backPanel = new JPanel(new BorderLayout());
    JPanel userPanel = new JPanel(new BorderLayout());
    JPanel emptyPanel = new JPanel();
    JLabel welcomeText = new JLabel("Welcome to our Quiz Game!");
    JLabel emojiLabel = new JLabel(new ImageIcon("C:\\Users\\46762\\Desktop\\Pictures\\BlueFaceSmile.jpg"));
    JPanel userNamePanel = new JPanel(new GridLayout(1,3));
    JLabel userNameLabel = new JLabel("Write your username:");
    JTextField userNameTextField = new JTextField(20);
    JButton userNameSubmitButton = new JButton("Submit");
    JButton newGameButton = new JButton("New Game");

    JTextField userInfoField = new JTextField("Här visas det info till användaren");
    String userName;
    ResultsScreen results;

    public WelcomeScreen() {
        setTitle("QuizGame");
        add(backPanel);
        backPanel.add(userPanel,BorderLayout.NORTH);
        backPanel.add(emptyPanel, BorderLayout.CENTER);
        backPanel.add(newGameButton, BorderLayout.SOUTH);

        userPanel.add(welcomeText, BorderLayout.NORTH);
        userPanel.add(emojiLabel, BorderLayout.CENTER);
        userPanel.add(userNamePanel, BorderLayout.SOUTH);

        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameTextField);
        userNamePanel.add(userNameSubmitButton);

        userPanel.setBackground(Color.WHITE);
        emptyPanel.setBackground(Color.WHITE);
        userNameLabel.setOpaque(true);
        userNameLabel.setBackground(Color.WHITE);

        newGameButton.setPreferredSize(new Dimension(250,50));

        userNameSubmitButton.addActionListener(this);
        newGameButton.addActionListener(this);

        setSize(400,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameButton){
            setVisible(false);
            results = new ResultsScreen();
        }
        if (e.getSource() == userNameSubmitButton){
            userName = userNameTextField.getText();
            userNamePanel.removeAll();
            userNameLabel.setText(userName);
            userNameLabel.setVerticalAlignment(SwingConstants.CENTER);
            userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            userNamePanel.add(userNameLabel);
            userNamePanel.revalidate();
            System.out.println(userName + " is connected.");
        }
    }
}