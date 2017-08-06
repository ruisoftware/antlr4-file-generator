package generator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruisoftware on 05/08/17.
 */
public class DataMap extends LinkedHashMap<String, Object> implements DataSet {

    private String mapName;
    private Class mapOfValue;

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public Object put(String key, Object value) {
        if (value != null) {
            Class classOfParam = value.getClass();
            if (mapOfValue == null) {
                mapOfValue = classOfParam;
            } else {
                if (classOfParam != mapOfValue) {
                    mapOfValue = Object.class;
                }
            }
        }
        return super.put(key, value);
    }

    /**
     * Returns the most specific class common to all values in this map
     */
    public Class getClassOfValues() {
        if (mapOfValue == null) {
            return Object.class;
        }
        if (mapOfValue == DataArray.class) {
            return List.class;
        }
        if (mapOfValue == DataMap.class) {
            return Map.class;
        }
        return mapOfValue;
    }
}
