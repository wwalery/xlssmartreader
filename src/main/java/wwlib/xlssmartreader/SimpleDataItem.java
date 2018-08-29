package wwlib.xlssmartreader;

import java.util.List;

/**
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class SimpleDataItem {

  private Direction via;
  private String find;
  private List<String> values;

  
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

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
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
