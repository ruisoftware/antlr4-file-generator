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

            // region enter methods
            @Override
            public void enterMap(MappingParser.MapContext ctx) {
                data.appendNewMap();
            }

            @Override
            public void enterArray(MappingParser.ArrayContext ctx) {
                data.pushArrayToValueToBeInsertedLater();
            }

            @Override
            public void enterMapValue(MappingParser.MapValueContext ctx) {
                data.pushMapToValueToBeInsertedLater();
            }
            // endregion


            // region exit methods
            @Override
            public void exitConfig(MappingParser.ConfigContext ctx) {
                data.setFilename(ctx.filename.getText());
            }

            @Override
            public void exitMapEntry(MappingParser.MapEntryContext ctx) {
                data.putToLastMap(ctx.key.getText());
            }

            @Override
            public void exitValue(MappingParser.ValueContext ctx) {
                if (ctx.isNumber != null) {
                    data.setValueToBeInsertedLater(new Float(ctx.isNumber.getText()));
                } else {
                    if (ctx.isStr != null) {
                        data.setValueToBeInsertedLater(ctx.isStr.getText());
                    }
                }
            }

            @Override
            public void exitArray(MappingParser.ArrayContext ctx) {
                data.popArrayToValueToBeInsertedLater();
            }

            @Override
            public void exitMapHeader(MappingParser.MapHeaderContext ctx) {
                data.setMapName(ctx.name.getText());
            }

            @Override
            public void exitMapValue(MappingParser.MapValueContext ctx) {
                data.popMapToValueToBeInsertedLater();
            }
            // endregion
        });
        parser.config();

        return data;
    }

}
