package cs3500.pa04;

import cs3500.pa04.controller.GameController;
import cs3500.pa04.controller.OneHumanOneAiController;
import cs3500.pa04.controller.ProxyController;
import cs3500.pa04.controller.TwoAisController;
import cs3500.pa04.controller.json.GameType;
import cs3500.pa04.controller.lockbox.UserShotsLockbox;
import cs3500.pa04.controller.viewctrl.CliViewController;
import cs3500.pa04.controller.viewctrl.ViewController;
import cs3500.pa04.model.game.PlayerBoards;
import cs3500.pa04.model.player.AiPlayer;
import cs3500.pa04.model.player.HumanPlayer;
import cs3500.pa04.model.player.HuntDestroyAiPlayer;
import cs3500.pa04.model.player.RandomAiPlayer;
import cs3500.pa04.view.AppendableOutCh;
import cs3500.pa04.view.GameView;
import cs3500.pa04.view.ReadableInCh;
import cs3500.pa04.view.UiReader;
import cs3500.pa04.view.UiWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args CLI arguments for game configuration. Supports multiple game modes:
   *             1. humanVsAi [random|huntDestroy]
   *             2. aiVsRandom
   *             3. aiVsServer [random|huntDestroy] [local|<Host IP address> <Host port>]
   */
  public static void main(String[] args) {
    try {
      if (args.length > 0) {
        UiReader input = new ReadableInCh(new InputStreamReader(System.in));
        UiWriter output = new AppendableOutCh(System.out);
        GameView view = new GameView(input, output);
        ViewController viewController = new CliViewController(view);

        if (args[0].equals("humanVsAi") && args.length == 2
                && (args[1].equals("random") || args[1].equals("huntDestroy"))) {
          PlayerBoards humanBoards = new PlayerBoards(new Random().nextInt());
          UserShotsLockbox lockbox = new UserShotsLockbox();
          HumanPlayer humanPlayer = new HumanPlayer("YOU", humanBoards, lockbox);

          PlayerBoards aiBoards = new PlayerBoards(new Random().nextInt());
          AiPlayer aiPlayer;
          if (args[1].equals("random")) {
            aiPlayer = new RandomAiPlayer("Random AI", aiBoards, new Random().nextInt());
          } else {
            aiPlayer = new HuntDestroyAiPlayer("Hunt Destroy AI", aiBoards, new Random().nextInt());
          }

          GameController runner =
                  new OneHumanOneAiController(viewController, humanPlayer, humanBoards, lockbox, aiPlayer, aiBoards);
          runner.run();
        } else if (args[0].equals("aiVsRandom") && args.length == 1) {
          PlayerBoards aiBoardsA = new PlayerBoards(new Random().nextInt());
          AiPlayer playerA = new RandomAiPlayer("Random AI", aiBoardsA, new Random().nextInt());

          PlayerBoards aiBoardsB = new PlayerBoards(new Random().nextInt());
          AiPlayer playerB = new HuntDestroyAiPlayer("Hunt Destroy AI", aiBoardsB, new Random().nextInt());

          GameController runner = new TwoAisController(viewController, playerA, aiBoardsA, playerB, aiBoardsB);
          runner.run();
        } else if (args[0].equals("aiVsServer")
                && ((args.length == 3 && args[2].equals("local")) || args.length == 4)
                && (args[1].equals("random") || args[1].equals("huntDestroy"))) {
          PlayerBoards aiBoards = new PlayerBoards(new Random().nextInt());
          AiPlayer localAi;
          if (args[1].equals("random")) {
            localAi = new RandomAiPlayer("Random AI", aiBoards, new Random().nextInt());
          } else {
            localAi = new HuntDestroyAiPlayer("Hunt Destroy AI", aiBoards, new Random().nextInt());
          }

          String hostIp;
          int hostPort;
          if (args[2].equals("local")) {
            hostIp = "0.0.0.0";
            hostPort = 35001;
          } else {
            hostIp = args[2];
            try {
              hostPort = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
              System.err.println("Failed to parse host port as an integer.");
              throw new IllegalArgumentException();
            }
          }

          try {
            Socket server = new Socket(hostIp, hostPort);
            GameController runner =
                    new ProxyController(viewController, localAi, GameType.SINGLE, server);

            runner.run();
          } catch (IOException e) {
            System.out.println("Failed to establish the connection."
                    + " Shutting down the application...");
          } catch (Exception e) {
            System.out.println("Unexpected error encountered when running the program."
                    + " Shutting down the application...");
          }
        }
      } else {
        throw new IllegalArgumentException();
      }
    } catch (Exception e) {
      System.err.println("Invalid cli argument format. Possible configurations include:\n" +
              "- humanVsAi [random|huntDestroy]\n" +
              "- aiVsRandom\n" +
              "- aiVsServer [random|huntDestroy] [local|<Host IP address> <Host port>]");
    }

  }
}