package cs3500.pa04.controller.json.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.game.Coord;
import java.util.List;

/**
 * Volley record to be used for JSON conversion
 *
 * @param volley list of CoordJson to represent the volley
 */
public record VolleyJson(
    @JsonProperty("coordinates") List<Coord> volley) {
}

