public class Protocol {

    private final int WAITING = 0;
    private final int ALLCONNECTED = 1;
    private final int PLAYER1TURN = 2;
    private final int OPPONENTSTURN = 3;

    private int NUMQUESTIONS = 2;
    private int CURRENTQUESTION = 0;

    private int NUMTURNS = 2;


    private int state = WAITING;

    public String processInput(String clientInput) {
        String theOutput = "";

        if (state == WAITING) {
            theOutput = "Waiting for opponent to connect";
        } else if (state == ALLCONNECTED) {
            theOutput = "All players connected";
            state = PLAYER1TURN;
        } else if (state == PLAYER1TURN) {

        }

        return theOutput;
    }
}
