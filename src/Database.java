import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {

    // klass för att hantera frågor och svar
    private static int randIndex;
    private int indexAnswer;

    private List<Integer> duplicates = List.of();

    final private String currentCategory = "Technology";

    List<String> categories = List.of("Technology", "Pop culture", "Geography", "Science");

    List<String> technologyQuestions = List.of(
            "What year was the very first model of the iPhone released?",
            "What’s the shortcut for the “copy” function on most computers?",
            "What is often seen as the smallest unit of memory?",
            "Is Java a type of OS?",
            "Who is often called the father of the computer?",
            "What does “HTTP” stand for?",
            "What is the name of the man who launched eBay back in 1995?",
            "Which email service is owned by Microsoft?",
            "Google Chrome, Safari, Firefox, and Explorer are different types of what?",
            "What was Twitter’s original name?");

    List<String> technologyAnswers = List.of(
            "2007", "2008", "2006", "2005",
            "Ctrl + c", "Ctrl + v", "Ctrl + a", "Ctrl + s",
            "kilobyte", "gigabyte", "megabyte", "terabyte",
            "no", "yes", "What are you stupid?", "im genuis",
            "Charles Babbage", "Steve Jobs", "Dr.Phil", "Bill Gates",
            "Hyper Text Transfer Protocol", "Hyper Text Transfer Plan", "Hyper Textable Transfer Plan", "Hyper Tin Transfer Protocol",
            "Pierre Omidyar", "Pierre White", "Ramsey Bolton", "Jonas Gudrunson",
            "Hotmail", "Gmail", "Proton", "yahoo",
            "Web browsers", "Internet things", "Companies", "Websites",
            "twttr", "twittr", "twitt", "tweet"
    );

    List<String> popcoultureQuestions = List.of(
            "What is Hawkeye's real name?",
            "Which Avenger is the only one who could calm the Hulk down?",
            "Night Crawler, member of the X-Men, has what kind of powers?",
            "Which infinity stone was located on Vormir?",
            "Which original Avenger was not in the first few movies?",
            "What was Superman’s birth name?",
            "What is the name of Batman’s butler?",
            "Aquaman is from which city under the sea?",
            "Who is Green Lantern’s nemesis?",
            "What does DC stand for?"
    );

    List<String> popcultureAnswers = List.of(
            "Clint Barton", "Charles Hawk", "Oliver Queen", "Robin Hood",
            "Black Widow", "Spiderman", "Ironman", "Captain America",
            "Can teleport", "Can fly", "Is invisible", "Can turn day into night",
            "Soul stone", "Space stone", "Mind stone", "Time stone",
            "The Wasp", "Ironman", "The Hulk", "Spiderman",
            "Kal-El", "Jor-El", "Clark Kent", "Barry Allen",
            "Alfred", "Albert", "Albin", "Brian",
            "Atlantis", "Borås", "Port Royal", "Dwarka",
            "Sinestro", "Red Lantern", "Thanos", "Zoom",
            "Detective Comics", "Dark Comics", "Dice Comics", "Daunting Comics"
    );

    List<String> geographyQuestions = List.of(
            "What is the name of the thin and long country that spans more than half of the western coast of South America?",
            "Which American state is the largest (by area)?",
            "Which two countries share the longest international border?",
            "What is the smallest country in the world?",
            "Which continent is the largest?",
            "Which of the Seven Wonders is located in Egypt?",
            "What is the capital of New Zealand?",
            "Which desert is the largest in the world?",
            "What is the name of the world’s longest river?",
            "Which city in India would you find the Taj Mahal in?"
    );

    List<String> geographyAnswers = List.of(
            "Chile", "Brazil", "Argentina", "Ecuador",
            "Alaska", "Texas", "Nevada", "Montana",
            "Canada and the USA", "Russia and China", "Argentina and Chile", "Russia and Kazakhstan",
            "Vatican City", "Monaco", "Nauru", "Tuvalu",
            "Asia", "North America", "South America", "Europe",
            "The Pyramids of Giza", "Hanging Gardens of Babylon", "Temple of Artemis at Ephesus", "Statue of Zeus at Olympia",
            "Wellington", "Christchurch", "Ashburton", "Napier",
            "Sahara Desert", "Gobi Desert", "Arabian Desert", "Patagonian Desert",
            "The Nile", "The Amazon", "The Yangtze", "The Mississippi",
            "Agra", "Bangalore", "Bombay", "Chennai"
    );

    List<String> scienceQuestions = List.of(
            "Who discovered penicillin?",
            "Who was the first woman to win a Nobel Prize (in 1903)?",
            "What part of the atom has no electric charge?",
            "What is the symbol for potassium?",
            "What is meteorology the study of?",
            "Which planet is the hottest in the solar system?",
            "Which natural disaster is measured with a Richter scale?",
            "What animals are pearls found in?",
            "Which planet has the most gravity?",
            "How many molecules of oxygen does ozone have?"
    );

    List<String> scienceAnswers = List.of(
            "Alexander Fleming", "Albert Einstein", "Stephen Hawking", "Wilhelm Röntgen",
            "Marie Curie", "Ada E. Yonath", "Dorothy Crowfoot Hodgkin", "Rosalyn Yalow",
            "Neutron", "Proton", "Electron", "Anti-neutrino",
            "K", "P", "Pa", "Po",
            "The weather", "Meteors", "The ocean", "The moon",
            "Venus", "Jupiter", "Mercury", "Saturn",
            "Earthquakes", "Tornadoes", "Tsunamis", "Flooding",
            "Oysters", "Crabs", "Lobsters", "Squids",
            "Jupiter", "Earth", "Saturn", "Uranus",
            "Three", "Two", "Four", "Six"
    );

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getTechnologyQuestions() {
        return technologyQuestions;
    }

    public String getQuestion(String category){
        while (true) {
            randIndex = (int) (Math.random() * technologyQuestions.size());
            if (!duplicates.contains(randIndex)) {
                break;
            }
        }
        duplicates.add(randIndex);
        if (category.equals("Technology")) {
            return technologyQuestions.get(randIndex);
        } else if (category.equals("Pop culture")) {
            return popcoultureQuestions.get(randIndex);
        } else if (category.equals("Geography")) {
            return geographyQuestions.get(randIndex);
        } else if (category.equals("Science")) {
            return scienceQuestions.get(randIndex);
        }
        return null;
    }

    public List<String> getAnswers(String category) {
        List<String> answers = new LinkedList<>();
        for (int i = randIndex * 4; i < (randIndex * 4 + 4); i++) {
            if (category.equals("Technology")) {
                answers.add(technologyAnswers.get(i));
            } else if (category.equals("Pop culture")) {
                answers.add(popcultureAnswers.get(i));
            } else if (category.equals("Geography")) {
                answers.add(geographyAnswers.get(i));
            } else if (category.equals("Science")) {
                answers.add(scienceAnswers.get(i));
            }
        }
        Collections.shuffle(answers);
        return answers;
    }

    public void setQuestionCategory(String a) {
        duplicates.clear();
        currentCategory = a;
    }

    public String getCorrectAnswer(String category) {
        if (category.equals("Technology")) { // testa att det fungerar korrekt med currentCategory being static
            return technologyAnswers.get(4 * randIndex);
        } else if (category.equals("Pop culture")) {
            return popcultureAnswers.get(4 * randIndex);
        } else if (category.equals("Geography")) {
            return geographyAnswers.get(4 * randIndex);
        } else if (category.equals("Science")) {
            return scienceAnswers.get(4 * randIndex);
        }
        return null;
    }
}
