# XlsSmartReader

Read XLS/XLSX files in a smart way. Use Apache POI for read XMS/XLSX and parse it by predefined template.
Is used for complex Excel files, created from template.

E.g. when you create Excel template sent it to different people, and, as result, obtain filled templates with shifted cells, rows, columns etc. 
So, when know common structure with constant text in cells, but placement of this cells is changed.
For work it is assumed that in result file has some gaps between constant text and data, but excess data not inserted in cells between.

# Usage

1. create XLS description in YAML format
2. read it
3. read XLS/XLSX file with given description

## XLS description format
This is the description, which described fields (colmns, rows) you need to read.

### Format
* `items` - first and mandatory element only<br/>
  * `item name` - item name, defined by you<br/>
  * `find` - string for search. Regexp.<br/>
  * `via` - direction for search. Possible values<br/>
    * `SELF` - values searched in the current cell. `find` must be regexp with one group<br/>
    * `ROW` - values searched in the current row (first not empty cell in all next columns)<br/>
    * `COLUMN` - values searched in the current column (first not empty cell in all next rows)<br/>
    * `BOTH` - values searched in the BOTH columns and rows (first not empty cell in all next rows and all columns - but in next   columns for current row). Now used only for arrays<br/>
  * `isArray` - find list of items instead of one.<br/>
  * `until` - string for limiting search. Regexp. Previous searches work only to first occurence of this string. When not defined - to end of sheet.<br/>
    * `sub item name` - sub item name, defined by you, when upper `via` defined as `BOTH`. E.g. when you need to collect values from internal table.<br/>
      * `via` - direction for search. Possible values same as in `item` instead of `BOTH`<br/>
      * `find` - string for search column or row header. Regexp.<br/>

### Example
```YAML
items:
   edrpou:
      find: Код за ЄДРПОУ/реєстраційний номер облікової картки платника податків(.*)
      via: ROW
    name:
      find: Найменування / прізвище, ім'я, по батькові.*
      via: COLUMN
    address:
      find: Місцезнаходження / місце проживання.*
      via: COLUMN
    SectionI:
      isArray: true
      find: І. Показники діяльності оператора, провайдера телекомунікацій
      until: ІІ.  Загальні показники діяльності оператора, провайдера телекомунікацій
      via: BOTH
      items:
        code:
          find: ^А$
          via: COLUMN
        1:
          find: ^1$
          via: COLUMN
        2:
          find: ^2$
          via: COLUMN
        3:
          find: ^3$
          via: COLUMN
    SectionII:
      isArray: true
      find: ІІ.  Загальні показники діяльності оператора, провайдера телекомунікацій
      via: BOTH
      items:
        code:
          find: ^А$
          via: COLUMN
        1:
          find: ^1$
          via: COLUMN
```
# Code
Initalization:
```java
XLSSmartReader reader = new XLSSmartReader();
reader.processXLS(yaml_file, xls_file);
```

After read all readed data found in rules data:<br/>
```java
reader.getRules().getItems().get('tax_code').getValue()
```
```java
reader.getRules().getItems().get('tax_code').getCell()
```
or as array:<br/>
```java
reader.getRules.getItems().get('Section1').getItems().get('1').getValues()
````
