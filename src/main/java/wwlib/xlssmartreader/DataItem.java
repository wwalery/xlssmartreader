package wwlib.xlssmartreader;

import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class DataItem extends SimpleDataItem {

  private boolean array;
  private String until;
  private ValueItem value;
  private Map<String, SimpleDataItem> items;

  public DataItem() {
    
  }
  
  public Map<String, SimpleDataItem> getItems() {
    return items;
  }

  public void setItems(Map<String, SimpleDataItem> items) {
    this.items = items;
  }

  
  public String getValue() {
    return value != null ? value.getStringValue() : null;
  }

  public Cell getCell() {
    return value != null ? value.getCell() : null;
  }
  
 public void setValue(ValueItem value) {
    this.value = value;
  }

  public boolean isArray() {
    return array;
  }

  public void setIsArray(boolean value) {
    this.array = value;
  }

  public String getUntil() {
    return until;
  }

  public void setUntil(String until) {
    this.until = until;
  }

  @Override
  public String toString() {
    return "DataItem{"
            + toSimpleString()
            + ", array=" + array 
            + ", until=" + until
            + ", value=" + value
            + (items != null ? "subItems=" + items : "")
            + '}';
  }

}
