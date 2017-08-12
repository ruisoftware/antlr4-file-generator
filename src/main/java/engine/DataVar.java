package engine;

/**
 * Created by ruisoftware on 07/08/17.
 */
public class DataVar implements DataSet {

    private String varName;
    private Object value;

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
