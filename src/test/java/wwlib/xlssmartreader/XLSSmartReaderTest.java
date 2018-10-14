package wwlib.xlssmartreader;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class XLSSmartReaderTest {
  
  private final static String RULE_FILE = "form-1K.yaml";
  private final static String XLS1_NAME = "test1.xls";
  private final static String XLS2_NAME = "test2.xls";
  private final static String XLS3_NAME = "test3.xls";

  /**
   * Test of getRules method, of class XLSSmartReader.
   */
  @Test
  public void testReadRules() {
    XLSSmartReader instance = new XLSSmartReader();
    instance.readRules(RULE_FILE);
    RulesData result = instance.getRules();
    assertEquals(5, result.getItems().size());
    assertTrue(result.getItems().containsKey("edrpou"));
    assertTrue(result.getItems().containsKey("name"));
    assertTrue(result.getItems().containsKey("address"));
    assertTrue(result.getItems().containsKey("SectionI"));
    assertEquals(Direction.COLUMN,result.getItems().get("name").getVia());
    assertEquals("Найменування / прізвище, ім'я, по батькові.*",result.getItems().get("name").getFind());
  }

  /**
   * Test of processXLS method, of class XLSSmartReader.
   * @throws java.lang.Exception
   */
  @Test
  public void testProcessXLS_1() throws Exception {
    XLSSmartReader instance = new XLSSmartReader();
    instance.processXLS(RULE_FILE, XLS1_NAME);
    RulesData result = instance.getRules();
    assertEquals("12345678", result.getItems().get("edrpou").getValue());
    assertEquals("Test Org 1", result.getItems().get("name").getValue());
    assertEquals("88000, Org address 1", result.getItems().get("address").getValue());
    assertEquals(4, result.getItems().get("SectionI").getItems().size());
    assertTrue(result.getItems().get("SectionI").getItems().containsKey("code"));
    assertEquals(49, result.getItems().get("SectionI").getItems().get("code").getValues().size());
    assertEquals(151, result.getItems().get("SectionI").getItems().get("1").getValues().get(1).getCell().getNumericCellValue(), 0);
    assertEquals(0.03, result.getItems().get("SectionII").getItems().get("1").getValues().get(2).getCell().getNumericCellValue(), 0);
  }

  @Test
  public void testProcessXLS_2() throws Exception {
    XLSSmartReader instance = new XLSSmartReader();
    instance.processXLS(RULE_FILE, XLS2_NAME);
    RulesData result = instance.getRules();
    assertEquals("87654321", result.getItems().get("edrpou").getValue());
    assertEquals(4, result.getItems().get("SectionI").getItems().size());
    assertTrue(result.getItems().get("SectionI").getItems().containsKey("code"));
    assertEquals(49, result.getItems().get("SectionI").getItems().get("code").getValues().size());
    assertEquals("-", result.getItems().get("SectionI").getItems().get("1").getValues().get(1).getStringValue());
    assertEquals(3.5, result.getItems().get("SectionII").getItems().get("1").getValues().get(2).getCell().getNumericCellValue(), 0);
  }

  @Test
  public void testProcessXLS_3() throws Exception {
    XLSSmartReader instance = new XLSSmartReader();
    instance.processXLS(RULE_FILE, XLS3_NAME);
    RulesData result = instance.getRules();
    assertEquals("12344321", result.getItems().get("edrpou").getValue());
    assertEquals("Test Org 3", result.getItems().get("name").getValue());
    assertEquals("99000, Org address 3", result.getItems().get("address").getValue());
    assertEquals(4, result.getItems().get("SectionI").getItems().size());
    assertTrue(result.getItems().get("SectionI").getItems().containsKey("code"));
    assertEquals(49, result.getItems().get("SectionI").getItems().get("code").getValues().size());
    assertEquals(0, result.getItems().get("SectionI").getItems().get("1").getValues().get(1).getCell().getNumericCellValue(), 0);
    assertEquals(4.838, result.getItems().get("SectionII").getItems().get("1").getValues().get(2).getCell().getNumericCellValue(), 0);
  }
  
  
  
}
