import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerSidePlayer extends Thread {
    ServerSidePlayer opponent;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    String player;
    String currentPlayerName;
    ServerSideGame game;
    private MultiWriter multiWriter;
    Database database;
    boolean playerEnteredName = false;
    boolean playerAnsweredQuestions = false;
    boolean playerReadyToPlay = false;
    static String category;
    static String question;
    static StringBuilder builderWithAnswers;
    static boolean drawnNextQuestion = false;
    boolean questionAnswered = false;
    String myPoints;

    public ServerSidePlayer(Socket socket, String player, ServerSideGame game, MultiWriter multiWriter, Database database) throws IOException {
        this.socket = socket;
        this.player = player;
        this.game = game;
        this.multiWriter = multiWriter;
        this.database = database;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            multiWriter.addWriter(output);
            output.println("WELCOME " + player);
            output.println("WAITING " + player);
            System.out.println("Waiting for opponent to connect.");
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }

    public void setOpponent(ServerSidePlayer opponent) {
        this.opponent = opponent;
    }

    public ServerSidePlayer getOpponent() {
        return opponent;
    }

    public synchronized void run() {
        try {
            output.println("PLAYERS_CONNECTED");
            System.out.println("All players connected");

            String fromClient, toClient;
            while ((fromClient = input.readLine()) != null) {
                if (fromClient.startsWith("ENTERED_NAME ")) {

                    player = fromClient.substring(13);
                    System.out.println(player + " has entered their name to " + currentPlayerName);
                    checkThatBothPlayerChoseName();


                } else if (fromClient.startsWith("MY_NAME ")) {
                    currentPlayerName = fromClient.substring(8);
                    sendPlayerNames(currentPlayerName);

                } else if (fromClient.startsWith("READY_TO_PLAY ")) {
                    player = fromClient.substring(14);
                    sendPlayerReadyToPlay(player);


                } else if (fromClient.startsWith("CHOOSING_CATEGORY ")) {
                    for (PrintWriter writer : multiWriter.getWriters()) {
                        writer.println(fromClient);
                    }

                } else if (fromClient.startsWith("I_CHOSE ")) {
                    category = fromClient.substring(8);
                    for (PrintWriter writer : multiWriter.getWriters()) {
                        writer.println(fromClient);
                        prepQuestionAndAnswers(category);
                    }


                } else if (fromClient.startsWith("READY_TO_ANSWER ")) {
                    output.println(fromClient);

                } else if (fromClient.startsWith("QUESTION? ")) {
                    output.println("QUESTION: " + question);
                    output.println("ANSWERS: " + builderWithAnswers);

                } else if (fromClient.startsWith("I_ANSWERED")) {
                    System.out.println(player + " Has answered the question");
                    checkThatBothAnsweredQuestion();
                    
                } else if (fromClient.startsWith("NEXT_QUESTION? ")) {
                    sendNextQuestionAndAnswer();

                } else if (fromClient.startsWith("BACK_TO_RESULTS ")) {
                    String playerInfoNumber = fromClient.substring(16, 23);
                    myPoints = fromClient.substring(24);
                    sendPointsAndCheckThatBothAnswered(myPoints,playerInfoNumber);


                } else if (fromClient.startsWith("SHOW_ME_RESULTS ")) {
                    output.println("SHOW_RESULTS");
                }
            }
        } catch (Exception ex) {
            System.out.println(player + " Disconnected!");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("There was a problem closing socket");
            }
        }
    }
    public void sendNextQuestionAndAnswer(){
        if (!drawnNextQuestion) {
            question = game.getQuestionText(category);
            System.out.println(question);
            List<String> answers = game.getAnswersText(category);
            String rightAnswer = database.getCorrectAnswer(category);
            builderWithAnswers.delete(0, builderWithAnswers.length());
            for (String answer : answers) {
                builderWithAnswers.append(answer).append(",");
            }
            builderWithAnswers.append(rightAnswer);
            System.out.println(builderWithAnswers);
        }
        output.println("NEXT_QUESTION: " + question);
        output.println("NEXT_ANSWERS: " + builderWithAnswers);
        if (!drawnNextQuestion) {
            drawnNextQuestion = true;
        } else
            drawnNextQuestion = false;
    }
    public void sendPointsAndCheckThatBothAnswered(String myPoints, String playerInfoNumber){
        String toClient = "";
        System.out.println(playerInfoNumber + " " + myPoints);
        this.playerAnsweredQuestions = true;
        output.println("WAIT");

        for (PrintWriter writer : multiWriter.getWriters()) {
            writer.println("POINTS: " + playerInfoNumber + " " + myPoints);
        }
        if (playerAnsweredQuestions && this.getOpponent().playerAnsweredQuestions) {
            System.out.println("Both players have answered questions.");
            toClient = "ANSWERED_QUESTIONS_BOTH";
            for (PrintWriter writer : multiWriter.getWriters()) {
                writer.println(toClient);
            }
        }
    }
    public void checkThatBothAnsweredQuestion(){
        String toClient = "";
        this.questionAnswered = true;
        output.println("HOLD");
        if (questionAnswered && getOpponent().questionAnswered) {
            System.out.println("Both answered Question");
            toClient = "BOTH_ANSWERED_QUESTION";
            for (PrintWriter writer : multiWriter.getWriters()) {
                writer.println(toClient);
            }
            this.questionAnswered = false;
            getOpponent().questionAnswered = false;
        }
    }
    public void prepQuestionAndAnswers(String category){
        question = game.getQuestionText(category);
        System.out.println(category);
        System.out.println(question);// för att kontrollera att det fungerar korrekt
        List<String> answers = game.getAnswersText(category);
        String rightAnswer = database.getCorrectAnswer(category);
        builderWithAnswers = new StringBuilder();
        for (String answer : answers) {
            builderWithAnswers.append(answer).append(",");
        }
        builderWithAnswers.append(rightAnswer);
        System.out.println(builderWithAnswers);// för att kontrollera att det fungerar korrekt
    }
    public void checkThatBothPlayerChoseName(){
        String toClient = "";
        this.playerEnteredName = true;
        if (playerEnteredName && this.getOpponent().playerEnteredName) {
            System.out.println("Both player has entered their names.");
            toClient = "ENTERED_NAME_BOTH";
            for (PrintWriter writer : multiWriter.getWriters()) {
                writer.println(toClient);
            }
        }
    }
    public void sendPlayerNames(String currentPlayerName){
        for (PrintWriter writer : multiWriter.getWriters()) {
            writer.println("MY_NAME " + player + " " + currentPlayerName);
        }
    }
    public void sendPlayerReadyToPlay(String player){
        System.out.println(player + " is ready to play.");// för att kontrollera att det fungerar korrekt
        this.playerReadyToPlay = true;
        output.println("READY_TO_PLAY");
    }
}