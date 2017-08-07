package generator.engine;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Created by ruisoftware on 26.07.2017.
 */
public abstract class EngineGenerator {

    private final static String OUTPUT_DIR = "output/";
    private Writer writer;
    private byte indentation;
    private final String INDENT = "    ";
    private boolean BOL = true; // true - cursor at the beginning of the line (next write should be indented); false - otherwise
    private String generatedFile;

    public void indentMore() {
        indentation++;
    }

    public void indentLess() {
        if (indentation > 0) {
            indentation--;
        }
    }

    public void writeIndentation() {
        if (BOL) {
            for (byte i = indentation; i > 0; i--) {
                doWrite(INDENT, false);
            }
        }
        BOL = false;
    }

    public void writeNewLine() {
        write(System.lineSeparator());
        BOL = true;
    }

    private void doWrite(String data, boolean withIndentation) {
        if (data != null) {
            try {
                if (writer == null) {
                    File path = new File(OUTPUT_DIR);
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    File file = new File(OUTPUT_DIR + getFileNameOnDisk());
                    writer = Files.newBufferedWriter(file.toPath(), Charset.defaultCharset());
                    generatedFile = file.getAbsolutePath();
                }
                if (withIndentation) {
                    writeIndentation();
                }
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String data) {
        doWrite(data, true);
    }

    public void writeln(String data) {
        write(data);
        writeNewLine();
    }

    protected abstract String getFileName();
    protected abstract String getFileNameOnDisk();
    protected abstract String generate();

    public String getGeneratedFile() {
        return generatedFile;
    }

    public void finish() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
