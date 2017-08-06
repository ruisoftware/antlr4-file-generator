package generator.engine;

import generator.Data;
import generator.DataArray;
import generator.DataMap;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ruisoftware on 26.07.2017.
 */
public class JavaGenerator extends EngineGenerator {

    private Data data;
    private Class mapImplementation;
    private Class arrayImplementation;

    public JavaGenerator(Data data, Class mapImplementation, Class arrayImplementation) {
        this.data = data;
        this.mapImplementation = mapImplementation == null ? LinkedHashMap.class : mapImplementation;
        this.arrayImplementation = arrayImplementation == null ? LinkedList.class : arrayImplementation;
    }

    public JavaGenerator(Data data) {
        this(data, null, null);
    }

    @Override
    protected String getFileName() {
        return data.getFilename();
    }

    @Override
    protected String getFileNameOnDisk() {
        return data.getFilename() + ".java";
    }

    @Override
    public String generate() {
        try {
            generateImports();
            writeln("public class " + data.getFilename() + " {");
            writeNewLine();
            indentMore();
            for (DataMap dataMap : data.getMaps()) {
                write(String.format("private static final Map<String, %s> %s = ", dataMap.getClassOfValues().getSimpleName(), dataMap.getMapName()));
                generateMap(dataMap);
                writeln(";");
                writeNewLine();
            }
            indentLess();
            writeln("}");
            return getGeneratedFile();
        } finally {
            finish();
        }
    }

    private void generateMap(DataMap dataMap) {
        write(String.format("new %s<String, %s>()", getMapImplementationClass(), dataMap.getClassOfValues().getSimpleName()));
        if (!dataMap.isEmpty()) {
            write(" {{");
            if (!data.getMaps().contains(dataMap)) {
                write(" // " + dataMap.getMapName());
            }
            writeNewLine();
            indentMore();
            for (String key : dataMap.keySet()) {
                write("put(\"" + key + "\", ");
                writeValue(dataMap.get(key));
                writeln(");");
            }
            indentLess();
            write("}}");
        }
    }

    private void generateArray(DataArray dataArray) {
        write(String.format("new %s<%s>()", getArrayImplementationClass(), dataArray.getClassOfElements().getSimpleName()));
        if (!dataArray.isEmpty()) {
            writeln(" {{");
            indentMore();
            for (Object obj : dataArray) {
                write("add(");
                writeValue(obj);
                writeln(");");
            }
            indentLess();
            write("}}");
        }
    }

    private void writeValue(Object value) {
        if (value instanceof Float) {
            write("new Float(" + Float.toString((Float) value) + "f)");
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

    private void generateImports() {
        if (data.isContainsArray() || data.isContainsMap() || !data.getImportsRequired().isEmpty()) {
            Set<String> imports = new TreeSet();
            if (data.isContainsArray()) {
                imports.add(arrayImplementation.getName());
                imports.add(List.class.getName());
            }
            if (data.isContainsMap()) {
                imports.add(mapImplementation.getName());
                imports.add(Map.class.getName());
            }
            for (Class aClass : data.getImportsRequired()) {
                if (aClass != Object.class && aClass != DataArray.class && aClass != DataMap.class) {
                    imports.add(aClass.getName());
                }
            }
            for (String anImport : imports) {
                writeln("import " + anImport + ";");
            }
            writeNewLine();
        }
    }

    private String getArrayImplementationClass() {
        return getClassName(arrayImplementation);
    }

    private String getMapImplementationClass() {
        return getClassName(mapImplementation);
    }

    private String getClassName(Class packageClass) {
        if (packageClass != null) {
            return packageClass.getSimpleName();
        }
        return null;
    }
}
