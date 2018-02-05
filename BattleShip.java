/**
 * Microsoft: DEV277x - Object Oriented Programming in Java Edx Course
 * Module 1 Project
 ***************
 * BATTLE SHIP
 ***************
 * Author: Emma Byrne
 **/
import java.util.Scanner;

public class BattleShip {
    static char[][] world; // Create global world 2D array
    public static void main(String[] args){
        world = createWorld(); //Create the world
        System.out.println("**** Welcome to the Battleship game ****\n");
        System.out.println("\tRight now the sea is empty");
        printWorld();
        deployPlayerShip(); // Deploy the player's ships
        printWorld();
        System.out.println("\nDeploying computer ships");
        deployComputerShips(); // Deploy the computer's ships
        playGame(); // Play the game
    }

    // Method to create the world map
    public static char[][] createWorld(){
        world = new char[10][10]; // Create a 10 by 10 grid
        return world;
    }

    // Method to print the map
    public static void printWorld(){
        System.out.println("  0123456789  ");
        for (int i = 0; i < world.length; i++){
            System.out.print(i + "|");
            for (int j = 0; j < world[i].length; j++){
                // \u0000 is the default entry in a char array if it is not populated
                if (world[i][j] == '\u0000' || world[i][j] == '2'){
                    System.out.print(" ");
                } else if (world[i][j] == '1') {
                    System.out.print('@');
                } else {
                    System.out.print(world[i][j]);
                }
             }
            System.out.print("|" + i + "\n");
        }
        System.out.println("  0123456789  ");
    }

    // Method to deploy the player's ships
    public static char[][] deployPlayerShip(){
        Scanner input = new Scanner(System.in);
        int count = 0;
        int x, y;
        // The user will deploy 5 ships so keep prompting the user until 5 ships are deployed
        while (count < 5) {
            System.out.print("Enter X coordinate for your ship: ");
            x = input.nextInt();
            System.out.print("Enter Y coordinate for your ship: ");
            y = input.nextInt();
            if (checkPosition(x,y) == 0){
                world[x][y] = '1';
                count++;
            } else if (checkPosition(x,y) == 1){
                System.out.println("That position is outside of the world. Please try again");
            } else if (checkPosition(x,y) == 2){
                System.out.println("There is a ship already in that position. Please try again");
            }
            System.out.println("Number of ships deployed: " + count + "\n");
        }
        return world;
    }

    // Method to check if the coordinates are valid and if valid, determines what is in that position on the map
    public static int checkPosition(int x, int y){
        // Return 1 if the coordinates are outside the map, 2 if a user ship is there, 3 if a computer ship is there
        // Return 0 if the position is not filled
        if (x > 10 || y > 10){
            return 1;
        } else if (world[x][y] == '1'){
            return 2;
        } else if (world[x][y] == '2') {
            return 3;
        } else if (world[x][y] == '!' || world[x][y] == 'x'){
            return 4; // if the ship has already been hit
        }
        return 0;
    }

    // Method to deploy the computer's ships
    public static char[][] deployComputerShips(){
        // Generate random ints between 0 and 9
        int x, y;
        int count = 0;
        while (count < 5){
            x = (int)(Math.random() * 10);
            y = (int)(Math.random() * 10);
            // Check if the numbers are valid
            if (checkPosition(x,y) == 0){
                world[x][y] = '2';
                count++;
            }
        }
        System.out.println("Successfully deployed 5 computer ships");
        System.out.println("--------------------------------------");
        return world;
    }

    // Method to enable a user to take a turn and try sink a ship
    public static int[] playerTurn(int[] ships){
        System.out.println("\n--------------------------\nYOUR TURN");
        // Get user to guess x and y coordinates
        boolean correct = false;
        int x, y, check;
        Scanner input = new Scanner(System.in);
        System.out.println("Time to make a guess");
        while (!correct){
            System.out.print("Enter X coordinate: ");
            x = input.nextInt();
            System.out.print("Enter Y coordinate: ");
            y = input.nextInt();
            check = checkPosition(x,y); // Check if the coordinates are valid
            switch(check) {
                case 1:
                    System.out.println("That position is outside of the map. Please try again");
                    break;
                case 2:
                    System.out.println("Oh no! You sunk your own ship!!!");
                    world[x][y] = 'x'; // update world map
                    ships[0] = ships[0] - 1; // remove 1 ship from user's ships
                    correct = true; // to break out of the while loop
                    break;
                case 3:
                    System.out.println("BOOM! You sunk an enemy ship!");
                    world[x][y] = '!'; // update world map
                    ships[1] = ships[1] - 1; // remove 1 ship from the computer's ships
                    correct = true;
                    break;
                case 4:
                    System.out.println("That ship is already sunk!");
                    break;
                default:
                    // if no ship is found
                    System.out.println("Sorry, that shot has missed");
                    world[x][y] = '-';
                    correct = true;
            }
        }
        System.out.println("------------------------------");
        return ships;
    }

    // Method to enable the computer to try and sink a ship
    public static int[] computerTurn(int[] ships){
        // the ships array stores how many user and computer ships are left
        // ships[0] = user ships, ships[1] = computer ships
        System.out.println("\nCOMPUTERS TURN");
        int x, y, check;
        boolean correct = false;
        while (!correct) {
            x = (int)(Math.random() * 10);
            y = (int)(Math.random() * 10);
            check = checkPosition(x,y);
            switch(check) {
                case 2:
                    System.out.println("The computer sunk one of your ships!!!");
                    world[x][y] = 'x';
                    ships[0] = ships[0] - 1;
                    correct = true;
                    break;
                case 3:
                    System.out.println("The computer sunk one of its own ships!");
                    world[x][y] = '!';
                    ships[1] = ships[1] - 1;
                    correct = true;
                    break;
                default:
                    // if no ship is found
                    System.out.println("The computer missed");
                    correct = true;
            }
        }
        System.out.println("------------------------------");
        return ships;
    }

    // Method to play the game
    public static void playGame(){
        // calls above methods to play the game
        int[] ships = new int[] {5,5}; // populate ships array with 5 user ships and 5 computer ships
        int playerShips = ships[0];
        int computerShips = ships[1];
        while (playerShips > 0 && computerShips > 0){
            ships = playerTurn(ships);
            ships = computerTurn(ships);
            playerShips = ships[0];
            computerShips = ships[1];
            // Print the world and number of ships remaining after each turn
            printWorld();
            System.out.println("Your ships: " + playerShips + " | Computer ships: " + computerShips);
        }

        if (playerShips == 0){
            System.out.println("\n\nOh no you lost! :(");
        } else if (computerShips == 0){
            System.out.println("\n\nHurray you won! Congratulations! :)");
        }
    }
}
