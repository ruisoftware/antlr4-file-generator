package generator;

import java.util.LinkedHashMap;

/**
 * Created by ruisoftware on 05/08/17.
 */
public class DataMap extends LinkedHashMap implements DataSet {
    private String mapName;

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
