package wwlib.xlssmartreader;

import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * Command line interface.
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class CLI {

  private static void printValues(SimpleDataItem data, String ident) {
    System.out.print(ident + data.getName() + " (" + data.getVia() + ") : ");
    if (data.getValues() != null) {
      for (String item : data.getValues()) {
        System.out.print(item + " | ");
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
    for (DataItem data : reader.getRules().getItems()) {
      if (data.isArray()) {
        System.out.println(data.getName() + " : ");
        if (data.getVia() == Direction.BOTH) {
          for (SimpleDataItem item : data.getItems()) {
            printValues(item, " ");
          }
        } else {
          printValues(data, "");
        }
      } else {
        System.out.println(data.getName() + " : " + data.getValue());
      }
    }

  }

}
