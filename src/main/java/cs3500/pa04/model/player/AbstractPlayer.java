package cs3500.pa04.model.player;

import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.PlayerBoardsInteractive;
import cs3500.pa04.model.game.Ship;
import cs3500.pa04.model.game.ShipType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Represents a local player for which the functionality of many methods is shared
 * Implements all methods from Player except takeShots
 */
public abstract class AbstractPlayer implements Player {
  private final String name;
  protected int boardHeight;
  protected int boardWidth;
  protected int fleetSize;
  protected final PlayerBoardsInteractive playerPovBoards;
  protected ArrayList<Coord> lastFiredShots;
  protected ArrayList<Coord> lastSuccShots;
  protected int numOfOpponentShots;

  /**
   * Constructor for AbstractLocalPlayer
   *
   * @param name String name of the player
   */
  public AbstractPlayer(String name, PlayerBoardsInteractive playerPovBoards) {
    this.name = name;
    this.playerPovBoards = playerPovBoards;

    lastFiredShots = new ArrayList<>();
    lastSuccShots = new ArrayList<>();
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  public String name() {
    return this.name;
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.boardHeight = height;
    this.boardWidth = width;

    List<Ship> setUpShips = playerPovBoards.setupBoards(height, width, specifications);
    this.fleetSize = setUpShips.size();
    return setUpShips;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *         ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    numOfOpponentShots = opponentShotsOnBoard.size();
    return playerPovBoards.reportDamage((ArrayList<Coord>) opponentShotsOnBoard);
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    lastSuccShots = (ArrayList<Coord>) shotsThatHitOpponentShips;
    playerPovBoards.successfulHits((ArrayList<Coord>) shotsThatHitOpponentShips, lastFiredShots);
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    // Unimplemented as suggested on Slack
    // Intended for the following parts of the assignment
  }
}
