package cs3500.pa04.controller.json.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.controller.json.GameType;

/**
 * Represents a set of join parameters in JSON format
 *
 * @param name String name of user, will be GitHub repo for the tournament
 * @param gameType Desired type of game to be played
 */
public record JoinResponseJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") GameType gameType) {
}
