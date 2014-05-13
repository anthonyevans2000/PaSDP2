/**
 * Nimgame, a class that monitors a simple counting game.
 * @author Anthony Evans 356294
 */
public class Nimgame {
  
  
  /* Stone counter */
  private static int _nStones;
  
  /* Removal upper bound */
  private static int _removalUpper;

  /* Players */
  private static Nimplayer _player1;
  private static Nimplayer _player2;
 
  /* Active player */
  private static boolean _player1Active;

  public Nimgame(int nStones, int removalUpper, Nimplayer player1, Nimplayer player2) {
    _nStones = nStones;
    _removalUpper = removalUpper;
    _player1 = player1;
    _player2 = player2;
    /* Set player 1 as the first active player */
    _player1Active = true;
    /*Play the game */
    playGame();
  }
  
  private void playGame() {
     /*Display initial welcome message*/
    welcome();
    
    /*
     * Main loop.
     * While there are remaining stones, display the number of stones remaining
     * and ask how many to remove
     */
    while(_nStones > 0) {
      displayStones();
      removeStones();
    }
    
    /*
     * After loop terminates, display quit message
     */    
    System.out.println("Game Over");
    
    /* Allocate winner */
    Nimplayer winningPlayer;
    if(_player1Active) {
      winningPlayer = _player1;
    }else{
      winningPlayer = _player2;
    }
    
    /* Print victory message */
    System.out.print(winningPlayer.fullName() + " wins!");
    
    /* Update stats */
    winningPlayer.wonGame();
    _player1.playedGame();
    _player2.playedGame();
    
      
  }
  
  /**
   * Displays initial game message
   */
  private static void welcome() {
    System.out.println();
    System.out.println("Initial stone count: " + _nStones);
    System.out.println("Maximum stone removal: " + _removalUpper);
    System.out.println("Player 1: " + _player1.fullName());
    System.out.println("Player 2: " + _player2.fullName() + "\n");
  }
  
   /**
   * Displays a string explaining the number of stones left, followed by a 
   * visual display of the remaining stones, represented by asterisks
   */
  private static void displayStones() {
    System.out.print(_nStones + " stones left:");
    
    //Prints _nStones asterisks
    for(int i = 0; i < _nStones; i++) {
      System.out.print(" *");
    }
    //Terminating line break
    System.out.println("");
  }
  
  /**
   * Displays a string prompting the user for a number of stones to remove, then
   * takes away that amount from _nStones.
   * Method checks for a valid move
   * Active player swaps on each iteration. At the completion of this method,
   * the player that is next to move will be the active player.
   * Hence, if there are no stones left, that player is the winner.
   */
  private static void removeStones() {
    /* Determine the active player and swap for the next round*/
    Nimplayer activePlayer = activePlayer();
    
    
    //Reads in an integer input and updates _nStones
    int stonesToRemove = 0;
    
    while(stonesToRemove == 0) {
      System.out.println(activePlayer.firstName() + "'s turn - remove how many?\n");
      stonesToRemove = getInput();
    }
    _nStones = _nStones - stonesToRemove;

  }
  
  /**
   * Waits for user input and tests against the valid available moves.
   * @return 
   */
  private static int getInput() {
    int underTest = Integer.parseInt(Nimsys._keyboard.nextLine());
    if(underTest < 1 || underTest > _removalUpper) {
      invalidMove();
      displayStones();
      return 0;
    } else {
      return underTest;
    }
  }
  
  /**
   * Prints a warning indicating the allowed moveset.
   */
  private static void invalidMove() {
    int min = (_nStones > _removalUpper) ? _removalUpper : _nStones;
    System.out.println("Invalid move. You must remove between 1 and " 
                        + String.valueOf(min) 
                        + " stones.\n");
  }
  
  /**
   * Returns the active player, swapping the active player at the same time.
   * @return 
   */
  private static Nimplayer activePlayer() {
    Nimplayer activePlayer;
    if(_player1Active) {
      activePlayer = _player1;
      _player1Active = false;
    } else {
      activePlayer = _player2;
      _player1Active = true;
    }
    return activePlayer;
  }
  
}