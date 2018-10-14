package wwlib.xlssmartreader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * Main XLS reader class
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public class XLSSmartReader {

  private final static Logger LOG = LoggerFactory.getLogger(XLSSmartReader.class);

  private RulesData rules;

  private FormulaEvaluator evaluator;

  protected String getCellValue(Cell cell) {
    DataFormatter formatter = new DataFormatter();
    return formatter.formatCellValue(cell, evaluator);
  }

  public RulesData getRules() {
    return rules;
  }

  public void processXLS(String ruleFile, String xlsName) throws IOException, InvalidFormatException, IllegalAccessException {
    try (InputStream dataStream = Files.newInputStream(new File(xlsName).toPath(), StandardOpenOption.READ);
            InputStream ruleStream = Files.newInputStream(new File(ruleFile).toPath(), StandardOpenOption.READ)) {
      processXLS(ruleStream, dataStream);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void processXLS(InputStream ruleFile, InputStream xls) throws IOException, InvalidFormatException, IllegalAccessException {
    readRules(ruleFile);
    Workbook workbook = WorkbookFactory.create(xls);
    evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    for (Sheet sheet : workbook) {
      processSheet(sheet);
    }
  }

  protected void readRules(InputStream ruleStream) {
    Yaml yaml = new Yaml();
//      System.out.println(yaml.load(stream).toString());
    rules = yaml.loadAs(ruleStream, RulesData.class);
  }

  protected void readRules(String ruleFile) {
    try (InputStream stream = Files.newInputStream(new File(ruleFile).toPath(), StandardOpenOption.READ)) {
      readRules(stream);
    } catch (IOException ex) {
      LOG.error(String.format("Error on read file [%s]", ruleFile), ex);
    }
  }

  protected void processSheet(Sheet sheet) throws IllegalAccessException {
    for (Map.Entry<String, DataItem> entry : rules.getItems().entrySet()) {
      processItem(entry.getKey(), entry.getValue(), sheet, null);
    }
  }

  protected Cell findInRow(Cell cell, Cell lastCell) {
    Row row = cell.getRow();
    int lastCellIndex = row.getLastCellNum();
    for (int i = cell.getColumnIndex() + 1; i <= lastCellIndex; i++) {
      Cell curCell = row.getCell(i);
      if (curCell == lastCell) {
        return null;
      }
      String value = getCellValue(curCell);
      if ((value != null) && !value.isEmpty()) {
        return curCell;
      }
    }
    return null;
  }

  protected Cell findInColumn(Cell cell, Cell lastCell) {
    Sheet sheet = cell.getSheet();
    int rowIndex = cell.getRowIndex();
    int columnIndex = cell.getColumnIndex();
    int lastRowIndex = sheet.getLastRowNum();
    for (int i = rowIndex + 1; i <= lastRowIndex; i++) {
      Cell curCell = sheet.getRow(i).getCell(columnIndex);
      if (curCell == lastCell) {
        return null;
      }
      String value = getCellValue(curCell);
      if ((value != null) && !value.isEmpty()) {
        return curCell;
      }
    }
    return null;
  }

  protected Cell findCell(Sheet sheet, Cell startCell, String text) {
    Pattern fromPattern = Pattern.compile(text);
    int startRow = startCell == null ? 0 : startCell.getRowIndex();
    for (int row = startRow; row <= sheet.getLastRowNum(); row++) {
      Row rowItem = sheet.getRow(row);
      if (rowItem == null) {
        continue;
      }
      int startColumn = (startCell == null) || (row > startRow) ? 0 : startCell.getColumnIndex() + 1;
      for (int col = startColumn; col < rowItem.getLastCellNum(); col++) {
        Cell cell = rowItem.getCell(col);
        if (cell == null) {
          continue;
        }
        String value = getCellValue(cell);
        if ((value == null) || value.isEmpty()) {
          continue;
        }
        Matcher matcher = fromPattern.matcher(value);
        if (!matcher.find()) {
          continue;
        }
        return cell;
      }
    }
    return null;
  }

  protected void processArrayItem(SimpleDataItem item, Sheet sheet, Cell beginFrom) {
    List<String> values = new ArrayList<>();
    switch (item.getVia()) {
      case COLUMN:
        for (int i = beginFrom.getRowIndex() + 1; i <= sheet.getLastRowNum(); i++) {
          Cell curCell = sheet.getRow(i).getCell(beginFrom.getColumnIndex());
          String value = getCellValue(curCell);
          if ((value == null) || value.isEmpty()) {
            break;
          }
          values.add(getCellValue(curCell));
        }
        break;
      case ROW:
        for (int i = beginFrom.getColumnIndex() + 1; i <= beginFrom.getRow().getLastCellNum(); i++) {
          Cell curCell = beginFrom.getRow().getCell(i);
          String value = getCellValue(curCell);
          if ((value == null) || value.isEmpty()) {
            break;
          }
          values.add(getCellValue(curCell));
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid via [" + item.getVia() + "] in sub item array");
    }
    item.setValues(values);
  }

  protected void processItem(String itemName, DataItem item, Sheet sheet, Cell beginFrom) {
    if ((item.getFind() == null) || item.getFind().isEmpty()) {
      LOG.error("Empty [find] field in item [{}]", itemName);
    }
    Cell startCell = findCell(sheet, beginFrom, item.getFind());
    if (startCell == null) {
      LOG.warn("Item with string [{}] not found in file", item.getFind());
      return;
    }
    Cell stopCell = (item.getUntil() == null) || item.getUntil().isEmpty()
            ? null
            : findCell(sheet, startCell, item.getUntil());
    switch (item.getVia()) {
      case SELF:
        Pattern fromPattern = Pattern.compile(item.getFind());
        Matcher matcher = fromPattern.matcher(getCellValue(startCell));
        item.setValue(matcher.group(1));
        break;
      case COLUMN:
        if (item.isArray()) {
          throw new IllegalArgumentException("Via [" + item.getVia() + "] for array not implemented");
        } else {
          item.setValue(getCellValue(findInColumn(startCell, stopCell)));
        }
        break;
      case ROW:
        if (item.isArray()) {
          throw new IllegalArgumentException("Via [" + item.getVia() + "] for array not implemented");
        } else {
          item.setValue(getCellValue(findInRow(startCell, stopCell)));
        }
        break;
      case BOTH:
        if (!item.isArray()) {
          throw new IllegalArgumentException("Via [" + item.getVia()
                  + "] must be used only for arrays");
        }
        if (item.getItems() == null) {
          throw new IllegalArgumentException("Items must be defined for [" + item.getVia() + "] way");
        }
        for (Map.Entry<String, SimpleDataItem> entry : item.getItems().entrySet()) {
          Cell startArrayFrom;
          if (entry.getValue().getFind() == null) {
            startArrayFrom = startCell;
          } else {
            startArrayFrom = findCell(sheet, startCell, entry.getValue().getFind());
            if (startArrayFrom == null) {
              throw new IllegalArgumentException("String [" + entry.getValue().getFind()
                      + "] not found for subitem " + itemName + "->" + entry.getKey());
            }
          }
          processArrayItem(entry.getValue(), sheet, startArrayFrom);
        }
        break;
      default:
        throw new IllegalArgumentException("Illegal item 'via' value: [" + item.getVia() + "]");
    }
  }

}
