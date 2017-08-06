package generator.engine;

import generator.Data;
import generator.DataArray;
import generator.DataMap;
import generator.DataSet;


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
            for (DataMap dataMap : data.getMaps()) {
                write("var " + dataMap.getMapName() + " = ");
                generateMap(dataMap);
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
        if (!data.getMaps().contains(dataMap)) {
            writeln("// " + dataMap.getMapName());
        }

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
        writeln("[");
        if (!dataArray.isEmpty()) {
            indentMore();
            boolean first = true;
            for (Object value : dataArray) {
                if (!first) {
                    write(",");
                    if (value instanceof DataSet) {
                        write(" ");
                    } else {
                        writeNewLine();
                    }
                }
                first = false;
                writeValue(value);
            }
            writeNewLine();
            indentLess();
        }
        write("]");
    }

    private void writeValue(Object value) {
        if (value instanceof Float) {
            write(Float.toString((Float) value));
        } else {
            if (value instanceof String) {
                write("\"" + value + "\"");
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
