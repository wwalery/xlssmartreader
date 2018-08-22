package wwlib.xlssmartreader;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class XLSSmartReaderTest {
  
  private String ruleFile = "form-1K.yaml";
//  private String xlsName = "Zvit_1K_Intel-com_2018_06.xls";
  private String xlsName = "zvit14K_1_2018.xls";

  /**
   * Test of getRules method, of class XLSSmartReader.
   */
  @Test
  public void testReadRules() {
    XLSSmartReader instance = new XLSSmartReader();
    instance.readRules(ruleFile);
    RulesData result = instance.getRules();
    assertEquals(3, result.getItems().size());
    assertEquals("edrpou", result.getItems().get(0).getName());
  }

  /**
   * Test of processXLS method, of class XLSSmartReader.
   * @throws java.lang.Exception
   */
  @Test
  public void testProcessXLS() throws Exception {
    System.out.println("processXLS");
    XLSSmartReader instance = new XLSSmartReader();
    instance.processXLS(ruleFile, xlsName);
    RulesData result = instance.getRules();
    assertEquals("36254908", result.getItems().get(0).getValue());
  }
  
}
