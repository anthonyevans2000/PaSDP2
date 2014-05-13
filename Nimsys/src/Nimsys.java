/**
 * Nim, a simple counting game.
 * @author Anthony Evans 356294
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


/**
 *
 * @author anthony
 */
public class Nimsys {
  
  /* Keyboard input scanner */
  public static final Scanner _keyboard = new Scanner(System.in);
  
  /* List of players */
  private static final ArrayList<Nimplayer> _players = new ArrayList<Nimplayer>();
  
  /* Definitions */
  private static final int NUMBER_OF_PRINTED_RANKINGS = 10;
  
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    ArrayList<String> cmd;
    
    welcome();
    /* Main loop. The 'exit' command terminates. */
    while(true) {
      System.out.print(">");
      cmd = getCommand();
      /* getCommand returns a list of strings. The first will map to the desired
       * function call
       */
      String command = cmd.get(0);
      /*Can switch on strings in Java 7 :'( */
      if(command.equals("addplayer")){
          addplayer(cmd);
      }else if(command.equals("removeplayer")) {
          removeplayer(cmd);
      }else if(command.equals("editplayer")) {
          editplayer(cmd);
      }else if(command.equals("resetstats")) {
          resetstats(cmd);
      }else if(command.equals("displayplayer")) {
          displayplayer(cmd);
      }else if(command.equals("rankings")) {
          rankings();
      }else if(command.equals("startgame")) {
          startgame(cmd);
          System.out.println();
      }else if(command.equals("exit")) {
          /* Print blank line and exit */
          System.out.println();
          System.exit(0);
      }
      System.out.println();
    }
  }
  
  /**
   * Displays welcoming message
   */
  private static void welcome() {
    System.out.println("Welcome to Nim\n");
  }
  
  
  /**
   * Processes input strings into a command and argument list.
   * Could easily have added checking for invalid command structure (spacing, 
   * commas, etc)- but this was out of scope. Thus the input commands are assumed
   * to be of the form "{Command} {Optional Args}" where {} denotes a string
   * separated by a space and {Optional Args} is a series of strings separated
   * only by commas.
   */
  private static ArrayList<String> getCommand() {
    ArrayList<String> ans = new ArrayList<String>();
    String[] inputStrings = _keyboard.nextLine().split(" ");
    
    ans.add(inputStrings[0]);
    
    if(inputStrings.length == 2) {
      String[] inputArgs = inputStrings[1].split(",");
      ans.addAll(Arrays.asList(inputArgs));
    }
    return ans;
  }
  
  /* Adds a player to _players. Fills player class using its constructor. Assumes 
  correct syntactical input.*/
  private static void addplayer(ArrayList<String> cmd){ 
    /*The 2nd argument, (indexed by cmd.get(1)) is the username*/
    if(checkExisting(cmd.get(1))) {
      alreadyExists();
    }else{
     /* The 3rd and 4th arguments (indexed by cmd 2 and 3) are given as the new
      family name and given name respectively */
      _players.add(new Nimplayer(cmd.get(1), cmd.get(3), cmd.get(2)));
    }
  }

  /* Deletes a player by username. If no user corresponds to the user arument, 
   it will terminate with a warning. If no user is specified, then it will prompt
   then delete all users on a positive response
  */
  private static void removeplayer(ArrayList<String> cmd){  
    if(cmd.size() < 2) {
      /* If only the removeplayer command is sent- prompt then delete all players
      upon confirmation */
      System.out.println("Are you sure you want to remove all players? (y/n)");
      if(confirm()){
        _players.clear();
      }
    }else{
      /* If an argument is given, delete the player with username corresponding 
      to that argument */
      String usr = cmd.get(1);
      if(confirmExisting(usr)) {
        _players.remove(playerIndex(usr));
      }
    }
  }
  
  /* Updates the first and last name parameters of a user, based on the argument
   *of the users username. Assumes correct syntax for command.
   */
  private static void editplayer(ArrayList<String> cmd) {
    String usr = cmd.get(1);
    if(confirmExisting(usr)) {
      /* The 3rd and 4th arguments (indexed by cmd 2 and 3) are given as the new
      family name and given name respectively */
      _players.get(playerIndex(usr)).edit(cmd.get(2),cmd.get(3));
    }
  }
  
  /* Resets a players stats by username. If no user corresponds to the user 
    arument, it will terminate with a warning. If no user is specified, then it 
    will prompt then reset all users stats on a positive response
  */
  private static void resetstats(ArrayList<String> cmd) {
    if(cmd.size() < 2) {
      /* If only the resetstats command is sent- prompt then reset all players
      stats upon confirmation */
      System.out.println("Are you sure you want to reset all player statistics? (y/n)");
      if(confirm()){
        for(Nimplayer player : _players) {
          player.resetStats();
        }
      }
    }else{
     /* If an argument is given, reset the player with username corresponding 
      to that argument */
      String usr = cmd.get(1);
      if(confirmExisting(usr)) {
        _players.get(playerIndex(usr)).resetStats();
      }
    }
  }
  
  /* Prints information about a player, based on a username argument. Given a
   * non-existing username, it will give a warning. If not given an argument, will
   * print information on all players.
   */
  private static void displayplayer(ArrayList<String> cmd) {
    if(cmd.size() < 2) {
      /* If only the displayplayer command is sent- display all players, ordered by username */
      ArrayList<Integer> indexs = playerIndexByUsername();
      for(int idx : indexs) {
        System.out.println(_players.get(idx).display());
      }
    }else{
      /* If an argument is given, display the player with username corresponding 
      to that argument */
      String usr = cmd.get(1);
      if(confirmExisting(usr)) {
        System.out.println(_players.get(playerIndex(usr)).display());
      }    
    }
  }
  
  
  /*Displays upto the top N players, ranked by score then alphabetically*/
  private static void rankings() {
    /* Sort users by ranking, resolve ties with username */
    ArrayList<Nimplayer> sorted = new ArrayList<Nimplayer>(_players);
    Collections.sort(sorted, new Comparator<Nimplayer>() {
      @Override
      public int compare(Nimplayer p1, Nimplayer p2) {
        if (p1.score() > p2.score()) return -1;
        if (p1.score() < p2.score()) return 1;
        /* Scores are equal. Sort by alphabetical username order */
        return p1.userName().compareTo(p2.userName());
      }
    });
    /* Print top N results, or all, whichever is less */
    int i = 0;
    for(Nimplayer usr : sorted) {
      System.out.println(usr.prettyPrintScore());
      i++;
      if(i >= NUMBER_OF_PRINTED_RANKINGS) break;
    }
    
  }
  
  private static void startgame(ArrayList<String> cmd) {
    String player1 = cmd.get(3);
    String player2 = cmd.get(4);
    int nStones = Integer.parseInt(cmd.get(1));
    int removalUpper = Integer.parseInt(cmd.get(2));
    
    if(checkExisting(player1) && checkExisting(player2)) {
            Nimgame playing = new Nimgame(nStones,
                                          removalUpper,
                                          _players.get(playerIndex(player1)),
                                          _players.get(playerIndex(player2))
                                          );
     /* The Nimgame class looks after the score updating */
    }else{
      System.out.print("One of the players does not exist.");
    }
  }
  
  /* Returns true if a username exists amoungst current players */
  private static boolean checkExisting(String userName) {
    for(Nimplayer player : _players) {
      /* For each player, see if their username matches the given string*/
      if(player.userName().equals(userName)) {
        /* Player found */
        return true;
      }
    }
    /* No players found with that username */
    return false;
  }
  
  /* Returns true if a username exists amoungst current players. Returns false
  and prompts a warning if not */
  private static boolean confirmExisting(String username) {
    if(checkExisting(username)){
      return true;
    }else{
        /* If the player referenced by the given username DNE, then give an error
         message */
        doesNotExist();
        return false;
      }
  }
  
  /* Returns the index in _players of a given username */
  private static int playerIndex(String username) {
    for(Nimplayer player : _players) {
      /* For each player, check if that player has the username. If one does,
      return its index */
      if(username.equals(player.userName())){
        return _players.indexOf(player);
      }
    }
    /* This should never occur, since the player should always be confirmed to exist
     before running this function. Return -1 as an error code. Probably should 
    throw an exception instead.
    */
    return -1;
  }
  
  private static void alreadyExists() {
    System.out.println("The player already exists.");
  }
  
  private static void doesNotExist() {
    System.out.println("The player does not exist.");
  }
  
  /* Prints a prompt asking the user to confirm- returns true if they input a 'y'
   character.
  */
  private static boolean confirm() {
    /* Read input, if a string with 'y' only is recieved, return true. Else, 
    return false */
    String input = _keyboard.nextLine();
    return input.equals("y");
  }
  
  /*
  * Sort the players by username. Return the sorted list of indexes of those players.
  */
  private static ArrayList<Integer> playerIndexByUsername() {
    ArrayList<Nimplayer> sorted = new ArrayList<Nimplayer>(_players);
    Collections.sort(sorted, new Comparator<Nimplayer>() {
      @Override
      public int compare(Nimplayer p1, Nimplayer p2) {
        return p1.userName().compareTo(p2.userName());
      }
    });
    ArrayList<Integer> ans = new ArrayList<Integer>();
    for(Nimplayer usr : sorted) {
      ans.add(_players.indexOf(usr));
    }
    return ans;
  }
}
  
