package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruisoftware on 05/08/17.
 */
public class DataArray extends ArrayList<Object> implements DataSet {

    private Class arrayOf;

    @Override
    public boolean add(Object param) {
        if (param != null) {
            Class classOfParam = param.getClass();
            if (arrayOf == null) {
                arrayOf = classOfParam;
            } else {
                if (classOfParam != arrayOf) {
                    arrayOf = Object.class;
                }
            }
        }
        return super.add(param);
    }

    /**
     * Returns the most specific class common to all elements in this array
     */
    public Class getClassOfElements() {
        if (arrayOf == null) {
            return Object.class;
        }
        if (arrayOf == DataArray.class) {
            return List.class;
        }
        if (arrayOf == DataMap.class) {
            return Map.class;
        }
        return arrayOf;

    }
}
