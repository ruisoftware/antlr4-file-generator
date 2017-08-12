package engine.generator;

import engine.Data;
import engine.DataArray;
import engine.DataMap;
import engine.DataSet;
import engine.DataVar;


/**
 * Created by ruisoftware on 26.07.2017.
 */
public class JavascriptGenerator extends EngineGenerator {

    private Data data;

    public JavascriptGenerator(Data data) {
        this.data = data;
    }

    @Override
    protected String getFileName() {
        return Character.toLowerCase(data.getFilename().charAt(0)) + data.getFilename().substring(1);
    }

    @Override
    protected String getFileNameOnDisk() {
        return getFileName() + ".js";
    }

    @Override
    public String generate() {
        try {
            for (DataVar dataVar : data.getVars()) {
                write("var " + dataVar.getVarName() + " = ");
                writeValue(dataVar.getValue());
                writeln(";");
                writeNewLine();
            }
            return getGeneratedFile();
        } finally {
            finish();
        }
    }

    private void generateMap(DataMap dataMap) {
        writeln("{");
        indentMore();
        boolean first = true;
        for (String key : dataMap.keySet()) {
            if (!first) {
                write(",");
                writeNewLine();
            }
            first = false;
            write("\"" + key + "\": ");

            writeValue(dataMap.get(key));
        }
        writeNewLine();
        indentLess();
        write("}");
    }

    private void generateArray(DataArray dataArray) {
        Class commonClassAllElements = dataArray.getClassOfElements();
        // if all elements in this array are all numbers or all Strings, then put them in a single line
        // otherwise, put each element in a new line
        boolean useNewLines = commonClassAllElements == DataSet.class || commonClassAllElements == Object.class;

        write("[");
        if (useNewLines && !dataArray.isEmpty()) {
            writeNewLine();
            indentMore();
        }
        boolean first = true;
        for (Object value : dataArray) {
            if (!first) {
                write(",");
                if (value instanceof DataSet || !useNewLines) {
                    write(" ");
                } else {
                    writeNewLine();
                }
            }
            first = false;
            writeValue(value);
        }
        if (useNewLines && !dataArray.isEmpty()) {
            writeNewLine();
            indentLess();
        }
        write("]");
    }

    private void writeValue(Object value) {
        if (value instanceof Integer) {
            write(Integer.toString((Integer) value));
        } else {
            if (value instanceof String) {
                write("\"" + value + "\"");
            } else {
                if (value instanceof Float) {
                    write(Float.toString((Float) value));
                } else {
                    if (value instanceof DataMap) {
                        generateMap((DataMap) value);
                    } else {
                        if (value instanceof DataArray) {
                            generateArray((DataArray) value);
                        }
                    }
                }
            }
        }
    }
}
