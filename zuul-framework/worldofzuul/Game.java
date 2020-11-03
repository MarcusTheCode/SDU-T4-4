package worldofzuul;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game
{
    private Parser parser; //parser attribut
    private Room currentRoom; //room attribut

    public Game() //game constructor
    {
        createRooms(); //create rooms funktion
        parser = new Parser(); //create parser object
    }

    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;

        Item fridge = new Item("Fridge");
        Item freezer = new Item("Freezer");
        Item[] itemsArray = {fridge,freezer};
        Items roomItems = new Items(itemsArray);

        outside = new Room("outside the main entrance of the university", roomItems);
        theatre = new Room("in a lecture theatre", roomItems);
        pub = new Room("in the campus pub", roomItems);
        lab = new Room("in a computing lab", roomItems);
        office = new Room("in the computing admin office", roomItems);
        
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom = outside;
    }


    public void play()
    {
        printWelcome();
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            StatusScore end = new StatusScore();
            if (processCommand(command)|| end.gameOver())
            {
                finished = processCommand(command)|| end.gameOver();
                System.out.println("Thank you for playing. You lost.");
            }
            else if(end.won())
            {
                finished = true;
                System.out.println("Thank you for playing. You won!");
            }

        }
    }

    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        if(commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        if (commandWord == CommandWord.HELP) {
            printHelp();
        }
        else if (commandWord == CommandWord.GO) {
            goRoom(command);
        }
        else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        }
        else if (commandWord == CommandWord.INTERACT) {
            System.out.println("interact works");
            interactItem(command);
        }
        return wantToQuit;
    }

    private void printHelp() 
    {
        System.out.println("You are alone in your apartment during the Covid-19 pandemic.");
        System.out.println("Live your day.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;
        }
    }

    private void interactItem(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("Interact with what?");
            return;
        }
        String interatebleItem = command.getSecondWord();
        Item[] item = currentRoom.getItem().itemsArray;
        for(Item i: item)
        {
            if (interatebleItem.equals(i.itemName))
            {
               System.out.println("You are interacting with: "+i.itemName);
            }

        }

    }

    public String[] getInteraction(Room room)
    {
        if (room.equals("Kitchen"))
        {

        }
    }


}
