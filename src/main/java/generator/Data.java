package generator;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by ruisoftware on 28.07.2017.
 */
public class Data {

    private String filename;
    private List<DataMap> maps = new LinkedList<>();
    private Object valueToBeInsertedLater;
    private Stack<DataSet> stackOfDataSets = new Stack<>();
    private byte qtDataMaps, qtDataArrays;

    public void appendNewMap() {
        if (qtDataMaps == 0) {
            maps.add(new DataMap());
        }
    }

    public void setMapName(String mapName) {
        getLastDataMap().setMapName(mapName);
    }

    public void putToLastMap(String key) {
        getLastDataMap().put(key, valueToBeInsertedLater);
        valueToBeInsertedLater = null;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setValueToBeInsertedLater(Object valueToBeInsertedLater) {
        if (this.valueToBeInsertedLater instanceof DataArray) {
            ((DataArray) this.valueToBeInsertedLater).add(valueToBeInsertedLater);
        } else {
            this.valueToBeInsertedLater = valueToBeInsertedLater;
        }
    }

    public void pushArrayToValueToBeInsertedLater() {
        this.valueToBeInsertedLater = new DataArray();
        pushDataArray();
    }

    public void popArrayToValueToBeInsertedLater() {
        if (qtDataArrays > 0) {
            this.valueToBeInsertedLater = pop();
            DataArray lastDataArray = (DataArray) this.valueToBeInsertedLater;
            if (qtDataArrays > 0 || qtDataMaps > 0) {
                this.valueToBeInsertedLater = stackOfDataSets.peek();
                setValueToBeInsertedLater(lastDataArray);
            }
        }
    }

    public void pushMapToValueToBeInsertedLater() {
        this.valueToBeInsertedLater = new DataMap();
        pushDataMap();
    }

    public void popMapToValueToBeInsertedLater() {
        if (qtDataMaps > 0) {
            this.valueToBeInsertedLater = pop();
            DataMap lastDataMap = (DataMap) this.valueToBeInsertedLater;
            if (qtDataArrays > 0 || qtDataMaps > 0) {
                this.valueToBeInsertedLater = stackOfDataSets.peek();
                setValueToBeInsertedLater(lastDataMap);
            }
        }
    }

    private DataMap getLastDataMap() {
        return qtDataMaps == 0 ? maps.get(maps.size() - 1) : (DataMap) stackOfDataSets.peek();
    }

    private void pushDataMap() {
        stackOfDataSets.push((DataMap) this.valueToBeInsertedLater);
        ++qtDataMaps;
    }

    private void pushDataArray() {
        stackOfDataSets.push((DataArray) this.valueToBeInsertedLater);
        ++qtDataArrays;
    }

    private DataSet pop() {
        DataSet popObj = stackOfDataSets.pop();
        if (popObj instanceof DataArray) {
            --qtDataArrays;
        } else {
            if (popObj instanceof DataMap) {
                --qtDataMaps;
            }
        }
        return popObj;
    }
}
