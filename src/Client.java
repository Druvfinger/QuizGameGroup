import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame implements ActionListener {
    protected final String host = "127.0.0.1";
    protected final int port = 54321;

    ServerSideGame game;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    GameScreen gameScreen;
    ResultsScreen resultsScreen;

    String userName;

    JFrame frame = new JFrame("Quiz Game");
    JPanel backpanel = new JPanel(new BorderLayout());
    JPanel headerPanel = new JPanel(new BorderLayout());
    JPanel centerHeaderPanel = new JPanel(new BorderLayout());
    JLabel infoLabel = new JLabel();
    JButton continueButton = new JButton("New Game!");
    JLabel headerLabel = new JLabel("1");
    JPanel userNamePanel = new JPanel();
    JLabel lowerHeaderLabel = new JLabel("2");
    JButton submitUsernameButton = new JButton("Submit!");
    JTextField userNameTextField = new JTextField(15);
    JLabel usernamePromptLabel = new JLabel("Enter username:");


    public Client() throws IOException {


        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        infoLabel.setBackground(Color.white);
        backpanel.setBackground(Color.lightGray);
        userNamePanel.add(usernamePromptLabel);
        userNamePanel.add(userNameTextField);
        submitUsernameButton.setEnabled(false);
        submitUsernameButton.addActionListener(this);
        userNamePanel.add(submitUsernameButton);
        backpanel.add(userNamePanel,BorderLayout.WEST);
        headerPanel.add(headerLabel,BorderLayout.NORTH);
        headerPanel.add(lowerHeaderLabel,BorderLayout.SOUTH);
        backpanel.add(headerPanel,BorderLayout.NORTH);
        backpanel.add(continueButton,BorderLayout.SOUTH);
        frame.getContentPane().add(infoLabel, "South");
        frame.getContentPane().add(backpanel, "Center");
        continueButton.addActionListener(this);
        userNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (userNameTextField.getText().equals("")) {
                    submitUsernameButton.setEnabled(false);
                } else {
                    submitUsernameButton.setEnabled(true);
                    frame.repaint();
                }
            }
        });

    }

    public void play() throws IOException {
        String response;
        String player = "1";
        String opponentPlayer = "2";
        try {
            while (true) {
                response = in.readLine();
                if (response.startsWith("WELCOME")) {
                    player = response.substring(8,response.length()-1);
                    frame.setTitle("Quiz Game" + player);
                }
                else if (response.equals("Waiting for opponent to connect")){
                }
                else if (response.equals("All players connected")){
                    out.println("start");
                    gameScreen = new GameScreen();
                    game.chooseCategory();
                } else if (response.startsWith("SCORE")) {
                    int score = Integer.parseInt(response.substring(5));
                    int temp = gameScreen.currentRound;
                    if (temp ==  1){
                        //resultsScreen.player1ResultLabel.setText(score);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.frame.setLocationRelativeTo(null);
        client.frame.setSize(500, 200);
        client.play();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.newRound();
        if (e.getSource() == submitUsernameButton) {
            userName = userNameTextField.getText();
            userNamePanel.removeAll();
            usernamePromptLabel.setText(userName);
            usernamePromptLabel.setVerticalAlignment(SwingConstants.CENTER);
            usernamePromptLabel.setHorizontalAlignment(SwingConstants.CENTER);
            userNamePanel.add(usernamePromptLabel);
            userNamePanel.revalidate();
            out.println("player " + userName);
            out.println("MOVE");
            System.out.println(userName + " is connected.");
        }
        }
    public void makeLabelForScoreEachRound(){
        String name = "Round " +  gameScreen.getCurrentRound() + ": " + game.getCurrentPlayer().getCurrentScore();
        //Try make label for scorekeeping
    }
}