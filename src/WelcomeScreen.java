import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WelcomeScreen extends JFrame implements ActionListener {

    //klass för en välkomstskärmen (detta måste vara klient)
    JPanel backPanel = new JPanel(new BorderLayout());
    JPanel userPanel = new JPanel(new BorderLayout());
    JPanel emptyPanel = new JPanel();
    JLabel welcomeText = new JLabel("Welcome to our Quiz Game!");
    //JLabel emojiLabel = new JLabel(new ImageIcon("C:\\Users\\46762\\Desktop\\Pictures\\BlueFaceSmileSuddig.png"));
    JLabel emojiLabel = new JLabel(new ImageIcon("C:\\Users\\46762\\Desktop\\Pictures\\YellowSmart.png"));
    JPanel userNamePanel = new JPanel(new GridLayout(1, 3));
    JLabel userNameLabel = new JLabel("Write your username:");
    JTextField userNameTextField = new JTextField(20);
    JButton userNameSubmitButton = new JButton("Submit");
    JButton newGameButton = new JButton("New Game");
    private String userName;

    public static final Color LIGHT_BLUE = new Color(51, 153, 255);
    public static final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
    public static final Color VERY_LIGHT_GREEN = new Color(102, 255, 102);
    public static final Color LIGHT_GREEN = new Color(0, 255, 51);
    public static final Color GOLD = new Color(255, 204, 51);

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    protected final String host = "127.0.0.1";
    protected final int port = 54321;

    ServerSideGame game = new ServerSideGame();

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    GameScreen gameScreen;
    ResultsScreen resultsScreen;

    //String userName;

    public WelcomeScreen() throws IOException {

        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        setTitle("QuizGame");
        add(backPanel);
        backPanel.add(userPanel, BorderLayout.NORTH);
        backPanel.add(emptyPanel, BorderLayout.CENTER);
        backPanel.add(newGameButton, BorderLayout.SOUTH);

        userPanel.add(welcomeText, BorderLayout.NORTH);
        userPanel.add(emojiLabel, BorderLayout.CENTER);
        userPanel.add(userNamePanel, BorderLayout.SOUTH);

        welcomeText.setVerticalAlignment(SwingConstants.CENTER);
        welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
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

        userNameSubmitButton.addActionListener(this);
        newGameButton.addActionListener(this);

        setSize(410, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void play() throws IOException {
        String response;
        String player = "1";
        String opponentPlayer = "2";
        try {
            while (true) {
                response = in.readLine();
                System.out.println(response + " C");
                if (response.startsWith("WELCOME")) {
                    player = response.substring(8);

                } else if (response.equals("All players connected")) {
                    resultsScreen.infoField.setText("All players connected");
                    if (player.equals("Player1")) {
                        resultsScreen.goOnButton.setEnabled(true);
                    } else {
                        resultsScreen.infoField.setText("Waiting for opponent to play the round");
                    }

                } else if (response.equals("start")) {
                    game.newRound();
                    System.out.println("Hit kom vi");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.play();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameButton) {
            if (userName != null) {
                resultsScreen = new ResultsScreen();
                setVisible(false);
                out.println("READY");

            } else JOptionPane.showMessageDialog(null, "Please enter your username before you proceed.");
        }
        if (e.getSource() == userNameSubmitButton) {
            userName = userNameTextField.getText();
            userNamePanel.removeAll();
            userNameLabel.setText("Welcome " + userName + " and Good Luck!");
            userNameLabel.setVerticalAlignment(SwingConstants.CENTER);
            userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            userNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            userNameLabel.setBorder(new EmptyBorder(2, 20, 5, 20));
            userNamePanel.add(userNameLabel);
            userNamePanel.revalidate();
            System.out.println(userName + " is connected.");
        }
    }
}