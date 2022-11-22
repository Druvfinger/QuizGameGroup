import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultsScreen extends JFrame {
    String theirTurn = "Deras tur";
    JPanel basePanel = new JPanel(new BorderLayout());
    JLabel theirTurnLabel = new JLabel(theirTurn);
    JPanel goOnPanel = new JPanel();
    JButton goOnButton = new JButton("Fortsätt");
    JPanel leftPlayerPanel = new JPanel(new BorderLayout());
    JPanel middleCategoryPanel = new JPanel();
    JPanel rightPlayerPanel = new JPanel(new BorderLayout());
    JPanel leftUserInfoPanel = new JPanel();
    JPanel rightUserInfoPanel = new JPanel();
    JPanel leftPlayerAnswersPanel = new JPanel();
    JPanel rightPlayerAnswersPanel = new JPanel();
    GameScreen game;


    public ResultsScreen(){
        setTitle("QuizGame");
        add(basePanel);

        theirTurnLabel.setPreferredSize(new Dimension(300,75));
        leftPlayerPanel.setPreferredSize(new Dimension(110,400));
        rightPlayerPanel.setPreferredSize(new Dimension(110,400));
        goOnPanel.setPreferredSize(new Dimension(300,75));

        leftUserInfoPanel.setPreferredSize(new Dimension(110,125));
        rightUserInfoPanel.setPreferredSize(new Dimension(110,125));

        theirTurnLabel.setVerticalAlignment(SwingConstants.CENTER);
        theirTurnLabel.setHorizontalAlignment(SwingConstants.CENTER);

        theirTurnLabel.setOpaque(true); //hjälpverktyg
        theirTurnLabel.setBackground(Color.PINK); //hjälpverktyg
        leftPlayerPanel.setBackground(Color.LIGHT_GRAY); //hjälpverktyg
        middleCategoryPanel.setBackground(Color.GRAY); //hjälpverktyg
        rightPlayerPanel.setBackground(Color.LIGHT_GRAY); //hjälpverktyg
        goOnPanel.setBackground(Color.CYAN); //hjälpverktyg

        basePanel.add(theirTurnLabel, BorderLayout.NORTH);
        basePanel.add(leftPlayerPanel, BorderLayout.WEST);
        basePanel.add(middleCategoryPanel, BorderLayout.CENTER);
        basePanel.add(rightPlayerPanel, BorderLayout.EAST);
        basePanel.add(goOnPanel, BorderLayout.SOUTH);

        goOnButton.setPreferredSize(new Dimension(150,50));
        goOnPanel.add(goOnButton);

        leftPlayerAnswersPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg
        rightPlayerAnswersPanel.setBackground(Color.LIGHT_GRAY);// hjälpverktyg

        leftPlayerPanel.add(leftUserInfoPanel, BorderLayout.NORTH);
        leftPlayerPanel.add(leftPlayerAnswersPanel, BorderLayout.CENTER);
        rightPlayerPanel.add(rightUserInfoPanel, BorderLayout.NORTH);
        rightPlayerPanel.add(rightPlayerAnswersPanel, BorderLayout.CENTER);

        goOnButton.addActionListener(listener);

        setSize(400,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    ActionListener listener = new ActionListener() { // anonym klass
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == goOnButton){
                setVisible(false);
                game = new GameScreen();
            }
        }
    };

    public static void main(String[] args) {
        ResultsScreen results = new ResultsScreen();
    }
}