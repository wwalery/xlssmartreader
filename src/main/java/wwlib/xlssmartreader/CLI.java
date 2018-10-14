package wwlib.xlssmartreader;

import java.io.IOException;
import java.util.Map;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * Command line interface.
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class CLI {

  private static void printValues(String name, SimpleDataItem data, String ident) {
    System.out.print(ident + name + " (" + data.getVia() + ") : ");
    if (data.getValues() != null) {
      for (ValueItem item : data.getValues()) {
        System.out.print(item.getStringValue() + " | ");
      }
    }
    System.out.println();
  }

  public static void main(String... args) throws IOException, InvalidFormatException, IllegalAccessException {
    if (args.length < 2) {
      System.out.println("Usage: " + CLI.class.getCanonicalName() + " rileFile.yaml file.xls");
      System.exit(1);
    }
    XLSSmartReader reader = new XLSSmartReader();
    reader.processXLS(args[0], args[1]);
    for (Map.Entry<String, DataItem> entry : reader.getRules().getItems().entrySet()) {
      if (entry.getValue().isArray()) {
        System.out.println(entry.getKey() + " : ");
        if (entry.getValue().getVia() == Direction.BOTH) {
          for (Map.Entry<String, SimpleDataItem> subEntry : entry.getValue().getItems().entrySet()) {
            printValues(subEntry.getKey(), subEntry.getValue(), " ");
          }
        } else {
          printValues(entry.getKey(), entry.getValue(), "");
        }
      } else {
        System.out.println(entry.getKey() + " : " + entry.getValue().getValue());
      }
    }

  }

}
