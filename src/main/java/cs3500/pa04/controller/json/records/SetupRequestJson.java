package cs3500.pa04.controller.json.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.game.ShipType;
import java.util.Map;

/**
 * Represents the expected format of server setup request
 *
 * @param width int width of the board
 * @param height int height of the board
 * @param specs ship specifications
 */
public record SetupRequestJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> specs) {
}
