package biz.mercue.campusipr.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelUtils {
    private static Logger log = Logger.getLogger(ExcelUtils.class.getName());

    public static Workbook file2Workbook(FileInputStream fileInStream, String fileName) throws IOException {
        String extensionName = FilenameUtils.getExtension(fileName);
        Workbook workbook = null;
        boolean is_support = false;
        if (!StringUtils.isNULL(extensionName)) {
            if ("xlsx".equalsIgnoreCase(extensionName)) {
                workbook = new XSSFWorkbook(fileInStream);
                is_support = true;
            } else if ("xls".equalsIgnoreCase(extensionName)) {
                workbook = new HSSFWorkbook(fileInStream);
                is_support = true;
            }
        }
        return workbook;
    }


    public static Map<String, Integer> readExcelTitle(Workbook book) throws IOException {
        Map<String, Integer> titleMap = new HashMap<String, Integer>();
        Sheet sheet = book.getSheetAt(0);
        Row row = sheet.getRow(0);
        for (Cell cell : row) {
//			log.info("cell :"+cell.getColumnIndex()+" /"+cell.getStringCellValue());
            titleMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }

        LinkedHashMap<String, Integer> sortMap = sortHashMapByValues(titleMap);
        return sortMap;
    }

    public static LinkedHashMap<String, Integer> sortHashMapByValues(
            Map<String, Integer> titleMap) {
        List<String> mapKeys = new ArrayList<>(titleMap.keySet());
        List<Integer> mapValues = new ArrayList<>(titleMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap =
                new LinkedHashMap<>();

        for (Integer val : mapValues) {
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = titleMap.get(key);
                if (comp1.equals(val)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
