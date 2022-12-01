import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChooseCategoryScreen extends JFrame {
    JPanel basePanel = new JPanel(new BorderLayout());
    JLabel questionLabel = new JLabel("What category do you want to choose?", SwingConstants.CENTER);
    JPanel categoryPanel = new JPanel(new GridLayout(3, 1, 10, 10));
    JPanel emptyPanel = new JPanel();
    List<JButton> buttonList = new LinkedList<>();
    Database database = new Database();
    static String quizTitle = "Quiz Game";
    List<String> shuffledCategoryList;
    static String playerNumber;
    String currentPlayerName;

    public void setUpCategoryScreenGUI() {

        setTitle(quizTitle);
        add(basePanel);

        questionLabel.setPreferredSize(new Dimension(410, 200));
        emptyPanel.setPreferredSize(new Dimension(410, 25));

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

        basePanel.setBackground(Constants.LIGHT_BLUE);
        categoryPanel.setBackground(Constants.LIGHT_BLUE);
        emptyPanel.setBackground(Constants.LIGHT_BLUE);

        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));

        setSize(410, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public ChooseCategoryScreen(String player, String currentPlayerName) {
        playerNumber = player;
        this.currentPlayerName = currentPlayerName;
        setUpCategoryScreenGUI();
    }

    ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {
                String chosenCategory = ((JButton) e.getSource()).getText();
                Client.outWriter.println("I_CHOSE " + chosenCategory);
            }
        }
    };
}