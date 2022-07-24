package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;


public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** The current encourangement message index*/
    private int encore;
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder ss = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            ss.append(CHARACTERS[RandomUtils.uniform(rand, CHARACTERS.length)]);
        }

        return ss.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(width/2., height/2., s);
        if (!gameOver) drawHelpfulUI();
        StdDraw.show();
    }

    public void drawHelpfulUI() {
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);

        double margin = height*9./10.;
        StdDraw.line(0., margin, width, margin);

        double offset = 1.5;
        StdDraw.textLeft(1., margin + offset, "Round: " + round);
        StdDraw.text(width/2., margin + offset, playerTurn? "Type!" : "Watch!");
        if (playerTurn || encore < 0) encore = RandomUtils.uniform(rand, ENCOURAGEMENT.length);
        StdDraw.textRight(width - 1., margin + offset, ENCOURAGEMENT[encore]);
    }

    public void waitTime(long time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void flashSequence(String letters) {
        for (int i = 0; i< letters.length(); i++) {
            String c = letters.substring(i, i+1);
            drawFrame(c);
            waitTime(1000);
            drawFrame("");
            waitTime(500);
        }
    }

    public String solicitNCharsInput(int n) {
        StringBuilder ss = new StringBuilder(n);
        while(n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                ss.append(StdDraw.nextKeyTyped());
                n--;
                drawFrame(ss.toString());
            }
        }
        waitTime(500);
        return ss.toString();
    }

    public void startGame() {
        gameOver = false;
        round = 1;
        playerTurn = false;
        encore = -1;

        while (!gameOver) {
            drawFrame("Round: " + round);
            waitTime(1000);

            String ss = generateRandomString(round);
            flashSequence(ss);

            playerTurn = true;
            drawFrame("");
            String res = solicitNCharsInput(round);
            playerTurn = false;

            if (ss.equals(res)) round++;
            else gameOver = true;

            if (gameOver) {
                drawFrame("Game Over! You made it to round: " + round);
                break;
            }
        }
    }

}
