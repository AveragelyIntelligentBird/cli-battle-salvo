package cs3500.pa04.controller;

/**
 * Interface for decoupling GameController from Driver
 */
public interface GameController {
  /**
   * Main game runner, repeats Salvo stage until one of the players no longer can play
   *
   * @throws Exception when critical io error occurs, handled by Driver
   */
  void run() throws Exception;
}
