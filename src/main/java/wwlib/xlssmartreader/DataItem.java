package wwlib.xlssmartreader;

import java.util.List;

/**
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class DataItem extends SimpleDataItem {

  private boolean array;
  private String until;
  private String value;
  private List<SimpleDataItem> items;

  public DataItem() {
    
  }
  
  public List<SimpleDataItem> getItems() {
    return items;
  }

  public void setItems(List<SimpleDataItem> items) {
    this.items = items;
  }

  
  public String getValue() {
    return value;
  }

 public void setValue(String value) {
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
            + (items != null ? "subItems=" + items : "")
            + '}';
  }

}
