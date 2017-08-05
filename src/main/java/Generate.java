
import generator.Data;

/**
 * Created by ruisoftware on 05/08/17.
 */
public class Generate {

    public static void main(String[] args) throws Exception {
        Data data = new DataFactory().gatherAllData(Generate.class.getResourceAsStream("/datasource.txt"));


    }

}
