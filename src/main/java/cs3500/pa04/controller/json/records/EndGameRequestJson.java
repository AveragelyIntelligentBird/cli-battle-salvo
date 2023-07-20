package cs3500.pa04.controller.json.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.GameResult;

/**
 * Represents the expected format of EndGame request
 *
 * @param result GameResult for client's AI player
 * @param reason String reason for the result
 */
public record EndGameRequestJson(
    @JsonProperty("result") GameResult result,
    @JsonProperty("reason") String reason) {  }
