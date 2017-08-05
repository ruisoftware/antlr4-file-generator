import org.junit.Test;

/**
 * Created by ruisoftware on 29/07/17.
 */
public class DataFactoryTest {

    @Test
    public void testBuild() throws Exception {
        new DataFactory().gatherAllData(getClass().getResourceAsStream("/datasource.txt"));
    }
}
