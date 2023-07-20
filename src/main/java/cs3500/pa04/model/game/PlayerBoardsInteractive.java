package cs3500.pa04.model.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Read-write interface accessed by the model
 * Includes methods for updating the boards and survShipList as called by the Player
 */
public interface PlayerBoardsInteractive extends PlayerBoardsState {
  /**
   * Given the specifications for a board, returns a list of ships
   *
   * @param height the height of the board
   * @param width the width of the board
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  ArrayList<Ship> setupBoards(int height, int width, Map<ShipType, Integer> specifications);

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *         ship on this board
   */
  ArrayList<Coord> reportDamage(ArrayList<Coord> opponentShotsOnBoard);

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   * @param lastFiredShots list of shots previously shot by the player
   */
  void successfulHits(ArrayList<Coord> shotsThatHitOpponentShips,
                      ArrayList<Coord> lastFiredShots);
}
