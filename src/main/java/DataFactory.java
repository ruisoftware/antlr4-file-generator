import generator.Data;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;


import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ruisoftware on 29/07/17.
 */
public class DataFactory {

    public Data gatherAllData(InputStream in) throws IOException {
        Data data = new Data();

        MappingLexer lexer = new MappingLexer(new ANTLRInputStream(in));
        MappingParser parser = new MappingParser(new CommonTokenStream(lexer));
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException(String.format("[%d:%d] Failed to parse due to: %s", line, charPositionInLine,  msg), e);
            }
        });
        parser.addParseListener(new MappingBaseListener() {

            @Override
            public void enterConfig(MappingParser.ConfigContext ctx) {
                System.out.println("enterConfig");
            }

            @Override public void exitConfig(MappingParser.ConfigContext ctx) {
                System.out.println("exitConfig");
            }

            @Override
            public void enterMap(MappingParser.MapContext ctx) {
                data.appendNewMap();
            }

            @Override
            public void exitMap(MappingParser.MapContext ctx) {

            }

            @Override
            public void exitMapHeeader(MappingParser.MapHeaderContext ctx) {
                data.setMapName(ctx.mapName.getText());
            }
        });
        parser.config();

        return data;
    }

}
