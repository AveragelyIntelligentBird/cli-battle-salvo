package cs3500.pa04.model.game;

/**
 * Read-only interface processed by the Controller
 */
public interface PlayerBoardsState {
  /**
   * Getter for the current player board
   *
   * @return CellState 2d array representing the current state of player board
   */
  CellState[][] getPlayerBoardState();

  /**
   * Getter for the current opponent board
   *
   * @return CellState 2d array representing the current state of opponent board
   */
  CellState[][] getOpponentBoardState();

  /**
   * Checks whether the player can continue the game
   *
   * @return true if the player still has surviving ships
   */
  boolean playerHasSurvivingShips();

  /**
   * How many shots a player can take
   *
   * @return int number of allowed shots in the next salvo
   */
  int numberOfAllowedShots();
}
