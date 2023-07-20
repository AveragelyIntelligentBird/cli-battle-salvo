package cs3500.pa04.controller.viewctrl;

import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.game.CellState;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.ShipType;
import cs3500.pa04.view.GameView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Implementation of ViewController to specify exactly in what way the user will be interacting with
 * the application running in the command line
 */
public class CliViewController implements ViewController {
  private static final int SPACE_BETWEEN_BOARDS = 8;
  private static final String PLAYER_BOARD_HEADER = "YOUR BOARD"; //<= 12 chars for best result
  private static final String OPPONENT_BOARD_HEADER = "ENEMY BOARD"; //<= 12 chars for best result
  private static final String DIVIDER =
      "------------------------------------------------------\n";
  private static final Map<CellState, String> CELL_STR_MAPPING = new HashMap<>(Map.of(
      CellState.EMPTY, ".",
      CellState.SHIP, "#",
      CellState.HIT, "X",
      CellState.MISS, "_",
      CellState.UNKNOWN, "~"
  ));
  private static final int[] BOARD_DIMS_RANGE = new int[]{6, 15};
  private int[] boardDim;
  private final GameView view;


  /**
   * Constructor for the CliViewController
   *
   * @param view GameView to be used to interact with the user
   */
  public CliViewController(GameView view) {
    this.view = view;
    this.boardDim = null;
  }

  /**
   * Displays a welcome message to start off the session
   *
   * @throws IOException if failed to write to view
   */
  @Override
  public void displayWelcomeMessage() throws IOException {
    String str = """

        ______       _   _   _      _____       _           \s
        | ___ \\     | | | | | |    /  ___|     | |          \s
        | |_/ / __ _| |_| |_| | ___\\ `--.  __ _| |_   _____ \s
        | ___ \\/ _` | __| __| |/ _ \\`--. \\/ _` | \\ \\ / / _ \\\s
        | |_/ / (_| | |_| |_| |  __/\\__/ / (_| | |\\ V / (_) |
        \\____/ \\__,_|\\__|\\__|_|\\___\\____/ \\__,_|_| \\_/ \\___/\s

        """ + "Hello! Welcome to the OOD BattleSalvo Game!";
    view.display(str);
  }

  /**
   * Displays the player boards as they are known to them at a given instance
   *
   * @param playerBoard   CellState[][] current state of player's board
   * @param opponentBoard CellState[][] current player's knowledge of opponent's board
   * @throws IOException if failed to write to view
   */
  @Override
  public void displayPlayerPovBoards(CellState[][] playerBoard, CellState[][] opponentBoard)
      throws IOException {
    if (boardDim == null) {
      throw new IllegalStateException();
    }
    if (playerBoard == null || opponentBoard == null) {
      throw new IllegalArgumentException();
    }

    StringBuilder str = new StringBuilder(PLAYER_BOARD_HEADER);
    str.append(" ".repeat(
        (boardDim[1] * 2) - PLAYER_BOARD_HEADER.length() + SPACE_BETWEEN_BOARDS));
    str.append(OPPONENT_BOARD_HEADER);
    str.append(" ".repeat(
        (boardDim[1] * 2) - OPPONENT_BOARD_HEADER.length() + SPACE_BETWEEN_BOARDS));
    str.append("BOARD LEGEND:\n");

    CellState[] states =  CellState.values();
    for (int i = 0; i < boardDim[0]; i++) {
      str.append(formatRowOfBoard(playerBoard[i]));
      str.append(" ".repeat(SPACE_BETWEEN_BOARDS));
      str.append(formatRowOfBoard(opponentBoard[i]));

      if (i < states.length) {
        CellState curState = states[i];
        str.append(" ".repeat(SPACE_BETWEEN_BOARDS));
        str.append(CELL_STR_MAPPING.get(curState));
        str.append(" - ");
        str.append(curState.toString());
      }

      str.append("\n");
    }

    view.display(str.toString());
  }

  /**
   * Displays a given message
   *
   * @param msg String to be displayed
   * @throws IOException if failed to write to view
   */
  @Override
  public void displayMsg(String msg) throws IOException {
    view.display(msg);
  }

  /**
   * Displays a game over message based on the result of the session from the user's pov
   *
   * @param result GameResult of the current session
   * @throws IOException if failed to write to view
   */
  @Override
  public void displayEndOfGameMsg(GameResult result) throws IOException {
    switch (result) {
      case WIN -> view.display("""
                    
           _   _ _____ _____ _____ _____________   ___\s
          | | | |_   _/  __ \\_   _|  _  | ___ \\ \\ / / |
          | | | | | | | /  \\/ | | | | | | |_/ /\\ V /| |
          | | | | | | | |     | | | | | |    /  \\ / | |
          \\ \\_/ /_| |_| \\__/\\ | | \\ \\_/ / |\\ \\  | | |_|
           \\___/ \\___/ \\____/ \\_/  \\___/\\_| \\_| \\_/ (_)
                    
          """);
      case LOSE -> view.display("\nUnfortunately, you lost. Better luck next time!");
      case DRAW -> view.display("\nGame resulted in a tie!");
      default -> { }
    }
  }

  /**
   * Prompts user and gets a validly parsed board dimension
   *
   * @return int[] of size 2, where the first element is the height and the second is the width
   * @throws IOException if failed to write to view
   */
  @Override
  public int[] getBoardDimensions() throws IOException {
    int numDimensions = 2;
    int[] boardDim = new int[numDimensions];

    displayMsg("First, select a height and width for the playing board."
        + "\nThe value for each dimension must be an integer in the range [6, 15], inclusive."
        + "\nPlease enter two space separated values below.\n" + DIVIDER);

    boolean inputIsValid = false;
    while (!inputIsValid) {
      String userInLine = view.getLineOfInput();

      try {
        boardDim = getSpaceSeparatedInts(userInLine, numDimensions);
        if (BOARD_DIMS_RANGE[0] <= boardDim[0] && boardDim[0] <= BOARD_DIMS_RANGE[1]
            && BOARD_DIMS_RANGE[0] <= boardDim[1] && boardDim[1] <= BOARD_DIMS_RANGE[1]) {
          inputIsValid = true;
        } else {
          displayMsg("One or more of the values you entered isn't within the valid range."
              + "\nPlease, enter values in the range [6, 15], inclusive.\n" + DIVIDER);
        }
      } catch (NumberFormatException e) {
        displayMsg("One or more of the values you entered isn't an integer."
            + "\nPlease, enter integer values for height and width below.\n" + DIVIDER);
      } catch (InputMismatchException e) {
        displayMsg("You entered an unexpected number of values!"
            + "\nPlease, enter exactly two values for height and width below.\n" + DIVIDER);
      }
    }
    view.display(DIVIDER);

    this.boardDim = boardDim;
    return boardDim;
  }

  /**
   * Prompts user and gets a validly parsed ship specifications
   *
   * @return Map, mapping each of the ship types to an int desired number of ships of that type
   * @throws IOException if failed to write to view
   */
  @Override
  public HashMap<ShipType, Integer> getShipSpecifications() throws IOException {
    if (boardDim == null) {
      throw new IllegalStateException();
    }

    int numShipTypes = ShipType.values().length;
    int maxFleetSize = Math.min(boardDim[0], boardDim[1]);
    int[] inputs = new int[numShipTypes];

    displayMsg("Now, specify the make up of the fleet. "
        + "The available ship types are " + Arrays.toString(ShipType.values()) + "."
        + "\nThe fleet must contain at least one ship of each type and at most "
        + maxFleetSize + " ships in total."
        + "\nThe desired number of ships of each type should be input "
        + "in the same order as the list above."
        + "\nPlease enter values in a single line, space separated.\n" + DIVIDER);

    boolean inputIsValid = false;
    while (!inputIsValid) {
      String userInLine = view.getLineOfInput();

      try {
        inputs = getSpaceSeparatedInts(userInLine, numShipTypes);
        int tempSum = IntStream.of(inputs).sum();
        if (tempSum > maxFleetSize) {
          displayMsg("The fleet size you entered is too large!"
              + "\nPlease, make sure that the fleet doesn't have more than " + maxFleetSize
              + " ships.\n" + DIVIDER);
        } else if (Arrays.stream(inputs).anyMatch(n -> n <= 0)) {
          displayMsg("One or more of the values you entered isn't valid."
              + "\nPlease, make sure that there is at least one ship of each type.\n" + DIVIDER);
        } else {
          inputIsValid = true;
        }
      } catch (NumberFormatException e) {
        displayMsg("One or more of the values you entered isn't an integer."
            + "\nPlease, enter integer values for number of ships below.\n" + DIVIDER);
      } catch (InputMismatchException e) {
        displayMsg("You entered an unexpected number of values!"
            + "\nPlease, enter exactly " + numShipTypes + " values.\n" + DIVIDER);
      }
    }
    view.display(DIVIDER);

    HashMap<ShipType, Integer> specs = new HashMap<>();
    for (int i = 0; i < numShipTypes; i++) {
      specs.put(ShipType.values()[i], inputs[i]);
    }
    return specs;
  }

  /**
   * Prompts user and gets n lines of input, where each is a valid shot; returns a validly parsed
   * list of shot coordinates
   *
   * @param numShots int number of ships to be taken from the user
   * @return List of coordinates, where each is a valid shot
   * @throws IOException if failed to write to view
   */
  @Override
  public ArrayList<Coord> getNumShots(int numShots) throws IOException {
    if (boardDim == null) {
      throw new IllegalStateException();
    }
    ArrayList<Coord> shotCoords = new ArrayList<>();
    int numOfCoords = 2;
    int[] inputs;

    displayMsg("Enter 0-indexed coordinates of " + numShots + " shots to attack the opponent."
        + "\nPlease enter two space separated values for each shot, with each shot on a new line.\n"
        + DIVIDER);

    boolean inputIsValid = false;
    while (!inputIsValid) {
      shotCoords = new ArrayList<>();
      inputIsValid = true;

      for (int i = 0; i < numShots; i++) {
        String userInLine = view.getLineOfInput();

        try {
          inputs = getSpaceSeparatedInts(userInLine, numOfCoords);
          if (0 <= inputs[0] && inputs[0] < boardDim[1]
              && 0 <= inputs[1] && inputs[1] < boardDim[0]) {
            shotCoords.add(new Coord(inputs[0], inputs[1]));
          } else {
            displayMsg("One or more of the values you entered isn't within the valid range."
                + "\nPlease, enter valid 0-indexed coordinates for each of the shots."
                + "\nNow enter the Salvo from the beginning.\n" + DIVIDER);
            inputIsValid = false;
            break;
          }
        } catch (NumberFormatException e) {
          displayMsg("One or more of the values you entered isn't an integer."
              + "\nPlease, enter integer values for shot coordinates."
              + "\nNow enter the Salvo from the beginning.\n" + DIVIDER);
          inputIsValid = false;
          break;
        } catch (InputMismatchException e) {
          displayMsg("You entered an unexpected number of values!"
              + "\nPlease, enter exactly two values. Now enter the Salvo from the beginning.\n"
              + DIVIDER);
          inputIsValid = false;
          break;
        }
      }
    }
    view.display(DIVIDER);

    return shotCoords;
  }

  /**
   * Produces a string representing a single row of a board
   *
   * @param boardRow CellState[] row of a board
   * @return String representing a single row of a board
   */
  private String formatRowOfBoard(CellState[] boardRow) {
    StringBuilder str = new StringBuilder();
    for (CellState curCell : boardRow) {
      str.append(CELL_STR_MAPPING.get(curCell));
      str.append(" ");
    }
    return str.toString();
  }

  /**
   * Gets a single line of input from the user input parsed as an array list of numbers
   *
   * @param line String to parse ints from
   * @param expNumOfIns int number of expected integers in the line
   * @return int[] of size parsed ints from a single line
   * @throws NumberFormatException when the input isn't integer
   * @throws InputMismatchException when number of space separated inputs isn't equal to expNumOfIns
   */
  private int[] getSpaceSeparatedInts(String line, int expNumOfIns)
      throws NumberFormatException, InputMismatchException {
    int[] input = new int[expNumOfIns];

    String[] splitLine = line.split("\\s+");
    if (splitLine.length != expNumOfIns) {
      throw new InputMismatchException();
    }

    for (int i = 0; i < expNumOfIns; i++) {
      input[i] = Integer.parseInt(splitLine[i]);
    }
    return input;
  }
}
