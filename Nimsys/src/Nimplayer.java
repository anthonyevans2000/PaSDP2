/**
 * Nimplayer, a class for keeping track of the properties of Nim players
 * @author Anthony Evans 356294
 */
public class Nimplayer {
  
  private String _userName;
  private String _givenName;
  private String _familyName;
  private int _nPlayed;
  private int _nWon;
  
  
  public Nimplayer(String userName, String givenName, String familyName) {
    _userName = userName;
    _givenName = givenName;
    _familyName = familyName;
    _nPlayed = 0;
    _nWon = 0;
  }
  
  /**
   * Edits the Nimplayer to have a new Given and Family name.
   * @param newFamilyName
   * @param newGivenName 
   */
  public void edit(String newFamilyName, String newGivenName) {
    _givenName = newGivenName;
    _familyName = newFamilyName;
  }
  
  /**
   * Returns a string representing the statistics of the player for display.
   * @return 
   */
  public String display() {
    return  _userName + "," 
            + _givenName + ","
            + _familyName + ","
            + Integer.toString(_nPlayed) + " games,"
            + Integer.toString(_nWon) + " wins";
  }
 
  public String firstName() {
    return _givenName;
  }
 
  /**
   * Returns the concatenation of the Nimplayers first and last names.
   * @return 
   */
  public String fullName() {
    return _givenName + " " + _familyName;
  }
  
  public String userName() {
    return _userName;
  }
  
  /**
   * Returns a specific arrangement of the Nimplayers score for printing.
   * @return 
   */
  public String prettyPrintScore() {
    return String.format( "%-4s | %02d games | " ,String.valueOf(this.score())+"%", 
                            this._nPlayed) 
                          + fullName();
  }
  
  /**
   * Increments the Nimplayers score on a win.
   */
  public void wonGame() {
    _nWon++;
  }
  
  /**
   * Indiciates that a Nimplayer has played a game.
   */
  public void playedGame() {
    _nPlayed++;
  }
  
  /**
   * Resets the Nimplayers statistic tracking to zero.
   */
  public void resetStats() {
    _nPlayed = 0;
    _nWon = 0;
  }
  
  /**
   * Returns the Nimplayers win percentage, rounded to the nearest int.
   * @return 
   */
  public int score() {
    if(_nPlayed == 0) {
      return 0;
    }
    return (int) Math.round(_nWon*100.0/_nPlayed);
  }
  
  
}
