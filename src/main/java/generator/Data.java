package generator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by ruisoftware on 28.07.2017.
 */
public class Data {

    private String filename;
    private List<DataVar> vars = new LinkedList<>();
    private Object valueToBeInsertedLater;
    private Stack<DataSet> stackOfDataSets = new Stack<>();
    private byte qtMapsInStack, qtArraysInStack;
    private Set<Class> importsRequired = new HashSet<>();

    /**
     * Determines if there is at least one Map or Array being used.
     * Needed by the JavaGenerator, to know if a Java <code>import</code> should be generated for the classes that implement Map or Array.
     **/
    private boolean containsMap, containsArray;

    public List<DataVar> getVars() {
        return vars;
    }

    public boolean isContainsMap() {
        return containsMap;
    }

    public boolean isContainsArray() {
        return containsArray;
    }

    public Set<Class> getImportsRequired() {
        return importsRequired;
    }

    public void appendNewVar() {
        vars.add(new DataVar());
    }

    public void setVar(String varName) {
        DataVar lastDataVar = vars.get(vars.size() - 1);
        lastDataVar.setVarName(varName);
        lastDataVar.setValue(this.valueToBeInsertedLater);
    }

    public void putToLastMap(String key) {
        DataMap lastMap = (DataMap) stackOfDataSets.peek();
        lastMap.put(key, valueToBeInsertedLater);
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
        if (qtArraysInStack > 0) {
            this.valueToBeInsertedLater = pop();
            DataArray lastDataArray = (DataArray) this.valueToBeInsertedLater;
            importsRequired.add(lastDataArray.getClassOfElements());
            if (qtArraysInStack > 0 || qtMapsInStack > 0) {
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
        if (qtMapsInStack > 0) {
            this.valueToBeInsertedLater = pop();
            DataMap lastDataMap = (DataMap) this.valueToBeInsertedLater;
            importsRequired.add(lastDataMap.getClassOfValues());
            if (qtArraysInStack > 0 || qtMapsInStack > 0) {
                this.valueToBeInsertedLater = stackOfDataSets.peek();
                setValueToBeInsertedLater(lastDataMap);
            }
        }
    }

    private void pushDataMap() {
        stackOfDataSets.push((DataMap) this.valueToBeInsertedLater);
        ++qtMapsInStack;
        containsMap = true;
    }

    private void pushDataArray() {
        stackOfDataSets.push((DataArray) this.valueToBeInsertedLater);
        ++qtArraysInStack;
        containsArray = true;
    }

    private DataSet pop() {
        DataSet popObj = stackOfDataSets.pop();
        if (popObj instanceof DataArray) {
            --qtArraysInStack;
        } else {
            if (popObj instanceof DataMap) {
                --qtMapsInStack;
            }
        }
        return popObj;
    }
}
