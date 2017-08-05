package generator;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by ruisoftware on 28.07.2017.
 */
public class Data {

    private String filename;
    private List<DataMap> maps = new LinkedList<>();

    public void appendNewMap() {
        maps.add(new DataMap());
    }

    private Map<String, Object> getCurrentMap() {
        return maps.get(maps.size() - 1).getMap();
    }

    public void setMapName(String mapName) {
        maps.get(maps.size() - 1).setMapName(mapName);
    }

    public void putToMap(String key, Object value) {
        getCurrentMap().put(key, value);
    }


//    private Map<String, ValueAndType> map = new LinkedHashMap<>();
//    private boolean[] lastParsedValueIsNumber = new boolean[1];
//
//    public String getFilename() {
//        return filename;
//    }
//
//    public void setFilename(String filename) {
//        this.filename = filename;
//    }
//
//    public String getMapName() {
//        return mapName;
//    }
//
//    public void setMapName(String mapName) {
//        this.mapName = mapName;
//    }
//
//    public Map<String, Map<String, ValueAndType>> getAllMaps() {
//        return allMaps;
//    }
//
//    public void setAllMaps(Map<String, Map<String, ValueAndType>> allMaps) {
//        this.allMaps = allMaps;
//    }
//
//    public Map<String, ValueAndType> getMap() {
//        return map;
//    }
//
//    public void setMap(Map<String, ValueAndType> map) {
//        this.map = map;
//    }
//
//    public boolean[] getLastParsedValueIsNumber() {
//        return lastParsedValueIsNumber;
//    }
//
//    public void setLastParsedValueIsNumber(boolean[] lastParsedValueIsNumber) {
//        this.lastParsedValueIsNumber = lastParsedValueIsNumber;
//    }
//
//
//    public static class MapValueType {
//        private Object value;
//        private boolean isNumber;
//
//        public ValueAndType(String value, boolean isNumber) {
//            this.value = value;
//            this.isNumber = isNumber;
//        }
//
//        public String getValue() { return value; }
//        public boolean isNumber() { return isNumber; }
//    }
}
