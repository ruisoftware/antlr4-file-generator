package generator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruisoftware on 05/08/17.
 */
public class DataMap {
    private String mapName;
    private Map<String, Object> map;

    public DataMap() {
        map = new HashMap<>();
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

}
