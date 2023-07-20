package cs3500.pa04.controller.viewctrl;

import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.game.CellState;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.ShipType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface used to decouple specifics of how key UI actions are rendered
 */
public interface ViewController {
  /**
   * Displays a welcome message to start off the session
   *
   * @throws Exception if failed to write to view
   */
  void displayWelcomeMessage() throws Exception;

  /**
   * Displays the player boards as they are known to them at a given instance
   *
   * @param playerBoard CellState[][] current state of player's board
   * @param opponentBoard CellState[][] current player's knowledge of opponent's board
   * @throws Exception if failed to write to view
   */
  void displayPlayerPovBoards(CellState[][] playerBoard, CellState[][] opponentBoard)
      throws Exception;

  /**
   * Displays a given message
   *
   * @param msg String to be displayed
   * @throws Exception if failed to write to view
   */
  void displayMsg(String msg) throws Exception;

  /**
   * Displays a game over message based on the result of the session from the user's pov
   *
   * @param result GameResult of the current session
   * @throws Exception if failed to write to view
   */
  void displayEndOfGameMsg(GameResult result) throws Exception;

  /**
   * Prompts user and gets a validly parsed board dimension
   *
   * @return int[] of size 2, where the first element is the height and the second is the width
   * @throws Exception if failed to write to or read from view
   */
  int[] getBoardDimensions() throws Exception;

  /**
   * Prompts user and gets a validly parsed ship specifications
   *
   * @return Map, mapping each of the ship types to an int desired number of ships of that type
   * @throws Exception if failed to write to or read from view
   */
  HashMap<ShipType, Integer> getShipSpecifications() throws Exception;

  /**
   * Prompts user and gets n lines of input, where each is a valid shot; returns a validly parsed
   * list of shot coordinates
   *
   * @param numShots int number of ships to be taken from the user
   * @return List of coordinates, where each is a valid shot
   * @throws Exception if failed to write to or read from view
   */
  ArrayList<Coord> getNumShots(int numShots) throws Exception;

}
