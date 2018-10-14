package wwlib.xlssmartreader;

import java.util.List;
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class SimpleDataItem {

  private Direction via;
  private String find;
  private List<ValueItem> values;

  
  public SimpleDataItem() {
    
  }
  
  public Direction getVia() {
    return via;
  }

  public void setVia(Direction via) {
    this.via = via;
  }

  public String getFind() {
    return find;
  }

  public void setFind(String find) {
    this.find = find;
  }

  public List<ValueItem> getValues() {
    return values;
  }

  public void setValues(List<ValueItem> values) {
    this.values = values;
  }

  public String toSimpleString() {
    return "via=" + via
            + ", find=" + find
            + (values != null ? ", values=" + values : "");

  }

  @Override
  public String toString() {
    return "SimpleDataItem{" + toSimpleString() + "}";
  }

}
