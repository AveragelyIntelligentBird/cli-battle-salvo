package cs3500.pa04.controller.json.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents an expected structure of every JSON message to be exchanged in the remote session
 *
 * @param messageName the name of the server method request
 * @param arguments   the arguments passed along with the message formatted as a Json object
 */
public record MessageJson(
    @JsonProperty("method-name") String messageName,
    @JsonProperty("arguments") JsonNode arguments) {
}