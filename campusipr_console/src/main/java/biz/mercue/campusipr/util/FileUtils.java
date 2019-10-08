package biz.mercue.campusipr.util;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    private static Logger log = Logger.getLogger(FileUtils.class.getName());

    public static final File MultipartFile2File(MultipartFile mpFile, String id) throws IOException {
        log.info("MultipartFile2File");
        log.info("Constants.FILE_UPLOAD_PATH :" + Constants.FILE_UPLOAD_PATH);
        File convFile = null;
        InputStream inputStream = null;
        try {
            CommonsMultipartFile cFile = (CommonsMultipartFile) mpFile;
            DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
            inputStream = fileItem.getInputStream();
            String extensionName = FilenameUtils.getExtension(mpFile.getOriginalFilename());
            String finalFileName = Constants.FILE_UPLOAD_PATH + File.separator + id + "." + extensionName;
            convFile = new File(finalFileName);
            mpFile.transferTo(convFile);
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return convFile;
    }

    public static String readHtml(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            File file = new File(filePath);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                contentBuilder.append(str);
            }
            bufferedReader.close();
        } catch (IOException e) {
            log.error("read file error");
            return null;
        }
        return contentBuilder.toString();
    }
}
