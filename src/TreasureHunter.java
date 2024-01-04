import java.util.Objects;
import java.util.Scanner;
import java.awt.Color;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean testMode;
    private boolean gameOver;
    private boolean samuraiMode;
    private String mode;
    private final OutputWindow window;

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        window = new OutputWindow();
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        window.addTextToWindow("Welcome to TREASURE HUNTER!", Color.ORANGE);
        window.addTextToWindow("Going hunting for the big treasure, eh?", Color.BLACK);
        window.addTextToWindow("What's your name, Hunter? ", Color.BLACK);
        String name = SCANNER.nextLine().toLowerCase();
        window.addTextToWindow("Select mode? (e/n/h): ", Color.BLACK);
        String hard = SCANNER.nextLine().toLowerCase();
        if (hard.equals("h")) {
            hardMode = true;
            hunter = new Hunter(name, 10, "h");
            mode = "h";
        } else if (hard.equals("test")) {
            hunter = new Hunter(name);
        } else if (hard.equals("e")) {
            easyMode = true;
            mode = "e";
            hunter = new Hunter(name, 20, "e");
        } else if (hard.equals("n")) {
            mode = "n";
            hunter = new Hunter(name, 10, "n");
        } else if (hard.equals("s")) {
            samuraiMode = true;
            hunter = new Hunter(name, 10, "s");
            mode = "s";
        }

    }

    public boolean getEasyMode() {
        return easyMode;
    }

    private void checkGameOver() {
        if (hunter.getHunterGold() < 0) {
            gameOver = true;
        } else {
            gameOver = false;
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.25;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.5;

            // and the town is "tougher"
            toughness = 0.75;
        } else if (easyMode) {
            markdown = 0;
            toughness = .2;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, mode, window);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, mode);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x") && !gameOver) {
            window.addTextToWindow("\n", Color.BLACK);
            window.addTextToWindow(currentTown.getLatestNews(), Color.BLACK);
            window.addTextToWindow("***", Color.BLACK);
            if (hunter.getHunterGold() < 0) {
                gameOver = true;
                window.addTextToWindow("You lose!", Color.RED);
            } else if (hunter.getTreasures()[2] != null ) {
                gameOver = true;
                window.addTextToWindow("Congratulations! You found the last of the hidden treasures!", Color.ORANGE);
                window.addTextToWindow("You win!", Color.ORANGE);
            } else {
                window.addTextToWindow(hunter.toString(), Color.BLACK);
                window.addTextToWindow(currentTown.toString(), Color.BLACK);
                window.addTextToWindow("(B)uy something at the shop.", Color.BLACK);
                window.addTextToWindow("(S)ell something at the shop.", Color.BLACK);
                window.addTextToWindow("(M)ove on to a different town.", Color.BLACK);
                window.addTextToWindow("(L)ook for trouble!", Color.BLACK);
                window.addTextToWindow("(H)unt for treasure!", Color.BLACK);
                window.addTextToWindow("(D)ig for gold!", Color.BLACK);
                window.addTextToWindow("Give up the hunt and e(X)it.", Color.BLACK);
                System.out.println();
                window.addTextToWindow("What's your next move? ", Color.BLACK);
                choice = SCANNER.nextLine().toLowerCase();
                window.clear();
                processChoice(choice);
            }
        }



    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                window.addTextToWindow(currentTown.getLatestNews(), Color.BLACK);
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("x")) {
            window.addTextToWindow("Fare thee well, " + hunter.getHunterName() + "!", Color.BLACK);
        } else if (choice.equals("d")) {
            currentTown.digForGold();
        } else if (choice.equals("h")) {
            window.addTextToWindow(currentTown.lookForTreasure(), Color.BLACK);
        } else {
            window.addTextToWindow("Yikes! That's an invalid option! Try again.", Color.RED);
        }
    }

}