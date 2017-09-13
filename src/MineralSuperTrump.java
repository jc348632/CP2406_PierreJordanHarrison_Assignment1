import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by Pierre Jordan Harrison on 9/9/2017.
 */
public class MineralSuperTrump {
    private Card lastPlayedCard;
    private ArrayList<Card> deck;
    private ArrayList<Player> players;
    private ArrayList<Player> winningPlayers;
    private String gameMode;
    private Scanner sc;
    private int passCounter;

    MineralSuperTrump() {
        passCounter = 0;
        lastPlayedCard = new SupertrumpCard("None","You are playing first");
        deck = new ArrayList<>();
        players = new ArrayList<>();
        winningPlayers = new ArrayList<>();
        gameMode = "none";
        sc = new Scanner(System.in);
    }


    public void start(){ // condition to start the game
        String[] array;
        String string;
        Path file = Paths.get("C:\\Users\\dellp\\Desktop\\CP2406_PierreJordanHarrison_Assignment1\\CP2406_PierreJordanHarrison_Assignment1\\src\\card.txt"); // find the files which have a content about the game
        try {
            InputStream input = new BufferedInputStream(Files.newInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            reader.readLine();
            while ((string = reader.readLine()) != null){
                array = string.split(",");
                deck.add(new NormalCard(array[0],Float.valueOf(array[1]),Float.valueOf(array[2]),array[3],array[4],array[5])); //add the super trump to each player, base on category with different function
            }
            deck.add(new SupertrumpCard("The Mineralogist","Change trump category to cleavage"));
            deck.add(new SupertrumpCard("The Geologist","Change to trump category of your choice"));
            deck.add(new SupertrumpCard("The Geophysicist","Change trump category to specific gravity or throw magnetite"));
            deck.add(new SupertrumpCard("The Petrologist","Change trump category to crustal abundance"));
            deck.add(new SupertrumpCard("The Miner","Change trump category to economic value"));
            deck.add(new SupertrumpCard("The Gemmologist","Change trump category to hardness"));
            Collections.shuffle(deck);
        }
        catch(Exception e)
        {
            System.out.println("Message: " + e.getMessage());           //To show error message
        }


        System.out.println("How many players (3-5)?"); //first show how many player to play
        int numberOfPlayers = sc.nextInt();
        while (numberOfPlayers <3 || numberOfPlayers>5){ //if user input <3 players it will be denied and need to input from range 3 to 5
            System.out.println("Wrong number of players");
            System.out.println("How many players (3-5)?");
            numberOfPlayers = sc.nextInt();
        }
        for (int x = 0; x<numberOfPlayers; x++){
            System.out.println("Enter player "+ (x+1) +" name"); // this to input he player name
            String nameInput = sc.next();
            players.add(new Player(nameInput)); // add a player
        }
        for (int x = 0; x<8;x++){
            for (int y = 0; y<numberOfPlayers; y++){ //every player draw card
                drawCard(players.get(y));
            }
        }
        boolean playing = true;
        while(playing){
            for(Player player : players){
                if(winningPlayers.size() < players.size()-1) { //player only can put their card when they have bigger amount
                    if (gameMode.equals("none")) {
                        showCard(player);
                        modeChoice();
                        playTurn(player);
                    } else if (passCounter == players.size() - 1) {
                        lastPlayedCard = new SupertrumpCard("None", "You can put anything"); // if they use super trump card they can choose another category and put the card again
                        showCard(player);
                        modeChoice();
                        playTurn(player);
                    } else {
                        playTurn(player);
                    }
                } else {
                    playing = false;
                }
            }
        }
        int e = 1;
        System.out.println("Winner list"); // in the end of the game the winner will be shown 1. , 2., and the loser
        for(Player player: winningPlayers){
            System.out.println(e+". "+player.getName());
            players.remove(player);
            e++;
        }
        System.out.println(players.get(0).getName() + " has lost"); // this is to show player who lose example (c has lose)
    }

    public void drawCard(Player player){ // every player for drawing card
        Card drawedCard =  deck.get(deck.size()-1);
        deck.remove(drawedCard);
        player.addCard(drawedCard);
    }

    public void showCard(Player player){
        System.out.println(player.getName()+"\'s card:");
        for(int x = 0; x<player.getHand().size();x++){
            System.out.println("No: " + x +" "+ player.getHand().get(x).toString());
        }
    }

    public void modeChoice(){ //choose the mode card for to play
        String newMode = "Empty";
        System.out.println("Please pick a game mode");
        System.out.println("H = Hardness, S = Specific Gravity, C = Cleavage, A = Crustal Abundance, " +
                "E= Economic Value");
        String input = sc.next().toUpperCase();
        while (!(input.equals("H")||input.equals("S")||input.equals("C")||input.equals("A")|| // if user put besides H, S, C, A it will be invalid
                input.equals("E"))){
            System.out.println("Wrong mode entered");
            System.out.println("Please pick a game mode");
            System.out.println("H = Hardness, S = Specific Gravity, C = Cleavage, A = Crustal Abundance, " +
                    "E= Economic Value");
            input = sc.next().toUpperCase();
        }
        switch (input){
            case "H": //if player choose H the mode will be Hardness
                newMode = "Hardness";
                break;
            case "S": //if player choose S the mode will be Specific Gravity
                newMode = "Specific Gravity";
                break;
            case "C": //if player choose C the mode will be Cleavage
                newMode = "Cleavage";
                break;
            case "A": //if player choose A the mode will be Crustal Abundance
                newMode = "Crustal Abundance";
                break;
            case "E": //if player choose E the mode will be Economic Value
                newMode = "Economic Value";
                break;
        }
        gameMode = newMode;
    }

    public void playTurn(Player player){
        boolean turnValidator = false;
        while (!turnValidator) { //while true
            if(winningPlayers.contains(player)){
                passCounter++;
                turnValidator = true;
            } else {
                System.out.println("It is a game of "+ gameMode); // choosing game mode base on the user choose
                System.out.println("Last played card : " + lastPlayedCard.toString());
                showCard(player);
                System.out.println("What card are you going to play?"); // choosing the mode of the card
                System.out.println("Enter the number to play that card"); // choosing the card to fight the other player with bigger amount
                System.out.println("Else enter PASS to pass"); // pass turn when player do not have card to fight the other players
                String input = sc.next();
                if (input.toUpperCase().equals("PASS")) {
                    drawCard(player);
                    passCounter++;
                    turnValidator = true;
                } else {
                    try {
                        int cardNum = Integer.parseInt(input);
                        Card cardplayed = player.playCard(cardNum);
                        Float current;
                        Float previous;
                        int comparison = 0;
                        if (cardplayed instanceof NormalCard) {
                            if (lastPlayedCard instanceof NormalCard) {
                                switch (gameMode) {
                                    case "Hardness":
                                        current = new Float(((NormalCard) cardplayed).getHardness()); // the user have the card in their hand with mode Hardness
                                        previous = new Float(((NormalCard) lastPlayedCard).getHardness()); // card that other player choose with mode Hardness
                                        comparison = current.compareTo(previous); // comparing the card from the main player to the other player with mode Hardness
                                        break;
                                    case "Specific Gravity":
                                        current = new Float(((NormalCard) cardplayed).getSpecificGravity()); // the user have the card in their hand base on mode Specific Gravity
                                        previous = new Float(((NormalCard) lastPlayedCard).getSpecificGravity()); // card that other player choose base on mode Specific Gravity
                                        comparison = current.compareTo(previous); // comparing the card from the main player to the other player base on mode Specific Gravity
                                        break;
                                    case "Cleavage":
                                        current = new Float(((NormalCard) cardplayed).getCleavagePoint()); // the user have the card in their hand with mode Cleavage
                                        previous = new Float(((NormalCard) lastPlayedCard).getCleavagePoint()); // card that other player choose with mode Cleavage
                                        comparison = current.compareTo(previous); // comparing the card from the main player to the other player with mode Cleavage
                                        break;
                                    case "Crustal Abundance":
                                        current = new Float(((NormalCard) cardplayed).getCrustalAbundancePoint()); // the user have the card in their hand base on mode Crustal Abundance
                                        previous = new Float(((NormalCard) lastPlayedCard).getCrustalAbundancePoint()); // card that other player choose base on mode Crustal Abundance
                                        comparison = current.compareTo(previous); // comparing the card from the main player to the other player base on mode Crustal Abundance
                                        break;
                                    case "Economic Value":
                                        current = new Float(((NormalCard) cardplayed).getEcoValuePoint()); // the user have the card in their hand with mode Economic Value
                                        previous = new Float(((NormalCard) lastPlayedCard).getEcoValuePoint()); // card that other player choose base on mode Economic Value
                                        comparison = current.compareTo(previous); // comparing the card from the main player to the other player base on mode Economic Value
                                        break;
                                }
                                if (comparison > 0) { // comparing the card from the las player to the current player base on the mode they have choosen
                                    lastPlayedCard = cardplayed; // last player choosing card
                                    player.useCard(cardNum);
                                    passCounter = 0;
                                    if(checkHasWin(player)){ // they player that have win for the first time in the game should left first
                                        System.out.println("Player "+ player.getName() + " has left the table");
                                        player.setWin();
                                        winningPlayers.add(player);
                                        passCounter = -1;
                                    };
                                    turnValidator = true;
                                } else {
                                    System.out.println("Invalid card"); // cannot choosing card that have lower level or value
                                    System.out.println("Please play card with higher " + gameMode + " value"); // should choosing card with higher value
                                }
                            } else {
                                lastPlayedCard = cardplayed;
                                player.useCard(cardNum);
                                passCounter = 0;
                                if(checkHasWin(player)){ // if they player left first means that is the first winner, and in the table just the rest of players left
                                    System.out.println("Player "+ player.getName() + " has left the table");
                                    player.setWin();
                                    winningPlayers.add(player);
                                    passCounter = -1;
                                };
                                turnValidator = true;
                            }
                        } else {
                            switch (cardplayed.getName()) {
                                case "The Mineralogist": // this is the name of the super trump card to move to Cleavage mode
                                    gameMode = "Cleavage";
                                    break;
                                case "The Geologist":
                                    modeChoice();
                                    break;
                                case "The Geophysicist": // this name of the card base on mode Specific Gravity
                                    gameMode = "Specific Gravity";
                                    if(player.hasMagnetite()){ // if the player left the table for the first time, that is the first winner
                                        System.out.println("Player "+ player.getName() + " has left the table");
                                        player.setWin();
                                        winningPlayers.add(player);
                                        passCounter = -1;
                                        turnValidator = true;
                                    }
                                    break;
                                case "The Petrologist": // name of the super trump card to move to Crustal abundance mode
                                    gameMode = "Crustal abundance";
                                    break;
                                case "The Miner": // name of the super trump card to move to Economic Value mode
                                    gameMode = "Economic Value";
                                    break;
                                case "The Gemmologist": // this is name of the super trump card to move to Hardness mode
                                    gameMode = "Hardness";
                                    break;
                            }
                            lastPlayedCard = cardplayed;
                            player.useCard(cardNum);
                            if(checkHasWin(player)){
                                System.out.println("Player "+ player.getName() + " has left the table");
                                player.setWin();
                                winningPlayers.add(player);
                                passCounter = -1;
                                turnValidator = true;
                            };
                        }
                    } catch (Throwable e) {
                            System.out.println(e.getMessage());
                    }
                }
            }
        }

    }

    public boolean checkHasWin(Player player){
        boolean condition = false;
        if(player.getHand().size() == 0){
            condition = true;
        }
        return condition;
    }
}
