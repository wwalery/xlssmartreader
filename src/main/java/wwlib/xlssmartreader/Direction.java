package wwlib.xlssmartreader;

/**
 *
 * @author Walery Wysotsky <dev@wysotsky.info>
 */
public enum Direction {

  /**
   * Find in current cell. Find string must be regex with group.
   */
  SELF,
  
  /**
   * Find in row
   */
  ROW,
  
  /**
   * Find in column
   */
  COLUMN,
  
  /**
   * Find in rows and columns. Generally used in array.
   */
  BOTH;
  
}
