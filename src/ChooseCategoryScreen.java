import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChooseCategoryScreen extends JFrame {
    JPanel basePanel = new JPanel(new BorderLayout());
    JLabel questionLabel = new JLabel("What category do you want to choose?",SwingConstants.CENTER);
    JPanel categoryPanel = new JPanel(new GridLayout(3,1,10,10));
    JPanel emptyPanel = new JPanel();
    List<JButton> buttonList = new LinkedList<>();
    Database database = new Database();
    GameScreen gameScreen;
    static String quizTitle = "Quiz Game"; // Test title
    List<String> shuffledCategoryList;
    static String playerNumber;
    public static final Color LIGHT_BLUE = new Color(51, 153, 255);
    public static final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
    public static final Color VERY_LIGHT_GREEN = new Color(102, 255, 102);
    public static final Color LIGHT_GREEN = new Color(0, 255, 51);
    public static final Color GOLD = new Color(255, 204, 51);

    public ChooseCategoryScreen(String player){
        playerNumber=player;

        setTitle(quizTitle);
        add(basePanel);

        questionLabel.setPreferredSize(new Dimension(410,200));
        emptyPanel.setPreferredSize(new Dimension(410,25));

        basePanel.add(questionLabel, BorderLayout.NORTH);

        basePanel.add(categoryPanel, BorderLayout.CENTER);
        basePanel.add(emptyPanel, BorderLayout.SOUTH);

        shuffledCategoryList = new LinkedList<>(database.getCategories());
        Collections.shuffle(shuffledCategoryList);
        for (int i = 0; i < 3; i++) {
            JButton button = new JButton();
            button.setText(String.valueOf(shuffledCategoryList.get(i)));
            button.addActionListener(listener);
            buttonList.add(button);
            categoryPanel.add(button);
        }

        basePanel.setBackground(LIGHT_BLUE);
        categoryPanel.setBackground(LIGHT_BLUE);
        emptyPanel.setBackground(LIGHT_BLUE);

        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Sans Serif", Font.PLAIN,20));

        setSize(410, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    ActionListener listener = new ActionListener() { // anonym klass
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton){
                GameScreen.currentCategory = ((JButton) e.getSource()).getText();
                setVisible(false);
                gameScreen = new GameScreen(playerNumber);
            }
        }
    };

    public static void main(String[] args) {
        ChooseCategoryScreen categoryScreen = new ChooseCategoryScreen(playerNumber);
    }
}