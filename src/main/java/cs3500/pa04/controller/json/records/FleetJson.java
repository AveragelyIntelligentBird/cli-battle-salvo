package cs3500.pa04.controller.json.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.controller.json.ShipJsonAdapter;
import java.util.List;

/**
 * Represents a fleet in Json format
 *
 * @param shipJsons list of ShipAdapters which can be serialized directly
 */
public record FleetJson(
    @JsonProperty("fleet") List<ShipJsonAdapter> shipJsons) { }
