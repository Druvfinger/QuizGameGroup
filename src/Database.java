import java.util.List;

public class Database {

     // klass för att hantera frågor och svar

    List<String> categories = List.of("Technology","Replacement1","Replacement2","Replacement3");

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

    List<String> technologyCorrectAnswers = List.of(
            "2007",
            "Ctrl + c",
            "kilobyte",
            "no",
            "Charles Babbage",
            "Hyper Text Transfer Protocol",
            "Pierre Omidyar",
            "Hotmail",
            "Web browsers",
            "twttr");

    List<String> technologyAnswers = List.of(
            "2007","2008","2006","2005",
            "Ctrl + c","Ctrl + v","Ctrl + a","Ctrl + s",
            "kilobyte","gigabyte","megabyte","terabyte",
            "no","yes","What are you stupid?","im genuis",
            "Charles Babbage","Steve Jobs","Dr.Phil","Bill Gates",
            "Hyper Text Transfer Protocol","Hyper Text Transfer Plan","Hyper Textable Transfer Plan","Hyper Tin Transfer Protocol",
            "Pierre Omidyar","Pierre White","Ramsey Bolton","Jonas Gudrunson",
            "Hotmail","Gmail","Proton","yahoo",
            "Web browsers","Internet things","Companies","Websites",
            "twttr","twittr","twitt","tweet"
    );

    public List<String> getCategories() {
     return categories;
    }

    public List<String> getTechnologyQuestions() {
        return technologyQuestions;
    }

    public String getQuestion(){
        return null;
    }

    public List<String> getTechnologyAnswers() {
        return technologyAnswers;
    }

    public List<String> getTechnologyCorrectAnswers() {
        return technologyCorrectAnswers;
    }
}
