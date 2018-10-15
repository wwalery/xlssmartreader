package wwlib.xlssmartreader;

import java.util.Objects;
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class ValueItem {

  private String stringValue;
  private Cell cell;

  public ValueItem(String stringValue, Cell cell) {
    this.stringValue = stringValue;
    this.cell = cell;
  }

  public String getStringValue() {
    return stringValue;
  }

  public Cell getCell() {
    return cell;
  }

  @Override
  public String toString() {
    return "ValueItem{" + "stringValue=" + stringValue + ", cell=" + cell + '}';
  }


}
