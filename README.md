# XlsSmartReader

Read XLS/XLSX files in a smart way. Use Apache POI for read XMS/XLSX and parse it by predefined template.
Is used for complex Excel files, created from template.

E.g. when you create Excel template sent it to different people, and, as result, obtain filled templates with shifted cells, rows, columns etc. 
So, when know common structure with constant text in cells, but placement of this cells is changed.
For work it is assumed that in result file has some gaps between constant text and data, but excess data not inserted in cells between.
