package generator.engine;

import generator.Data;
import generator.DataArray;
import generator.DataMap;
import generator.DataVar;

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
            for (DataVar dataVar : data.getVars()) {
                writeValueDeclaration(dataVar);
                writeValue(dataVar.getValue(), true);
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

    private void writeValueDeclaration(DataVar dataVar) {
        Object value = dataVar.getValue();
        String javaType = getJavaType(value);
        write(String.format("private static final %s %s = ", javaType, dataVar.getVarName()));
    }

    private String getJavaType(Object value) {
        if (value instanceof Integer) {
            return "int";
        }
        if (value instanceof Float) {
            return "float";
        }
        if (value instanceof String) {
            return String.class.getSimpleName();
        }
        if (value instanceof DataMap) {
            return String.format("Map<String, %s>", ((DataMap) value).getClassOfValues().getSimpleName());
        }
        if (value instanceof DataArray) {
            return String.format("List<%s>", ((DataArray) value).getClassOfElements().getSimpleName());
        }
        return Object.class.getSimpleName();
    }

    private void generateMap(DataMap dataMap) {
        write(String.format("new %s<String, %s>()", mapImplementation.getSimpleName(), dataMap.getClassOfValues().getSimpleName()));
        if (!dataMap.isEmpty()) {
            write(" {{");
            writeNewLine();
            indentMore();
            for (String key : dataMap.keySet()) {
                write("put(\"" + key + "\", ");
                writeValue(dataMap.get(key), false);
                writeln(");");
            }
            indentLess();
            write("}}");
        }
    }

    private void generateArray(DataArray dataArray) {
        write(String.format("new %s<%s>()", arrayImplementation.getSimpleName(), dataArray.getClassOfElements().getSimpleName()));
        if (!dataArray.isEmpty()) {
            writeln(" {{");
            indentMore();
            for (Object obj : dataArray) {
                write("add(");
                writeValue(obj, false);
                writeln(");");
            }
            indentLess();
            write("}}");
        }
    }

    private void writeValue(Object value, boolean usePrimitives) {
        if (value instanceof Integer) {
            String i = Integer.toString(((Integer) value).intValue());
            write(usePrimitives ? i : "new Integer(" + i + ")");
        } else {
            if (value instanceof Float) {
                String f = Float.toString(((Float) value).floatValue());
                write(usePrimitives ? f + "f" : "new Float(" + f + "f)");
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
}
