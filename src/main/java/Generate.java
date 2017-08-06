
import generator.Data;
import generator.engine.JavaGenerator;
import generator.engine.JavascriptGenerator;

/**
 * Created by ruisoftware on 05/08/17.
 */
public class Generate {

    public static void main(String[] args) throws Exception {
        String datasource = "/datasource.txt";
        Data data = new DataFactory().gatherAllData(Generate.class.getResourceAsStream(datasource));

        System.out.println(String.format("\nGenerating Java and Javascript files from resources%s ...", datasource));
        System.out.println(String.format("Created %s", new JavaGenerator(data).generate()));
        System.out.println(String.format("Created %s", new JavascriptGenerator(data).generate()));
    }
}
