package biz.mercue.campusipr.util;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class UTF8BomRemover extends DirectoryWalker {
    private static Logger log = Logger.getLogger(ServiceTaiwanPatent.class.getName());
    private String extension = null;
    private static final String UTF8_BOM = "\uFEFF";

    public UTF8BomRemover(String extension) {
        super();
        this.extension = extension;
    }

    @SuppressWarnings("unchecked")
    public void start(File rootDir) throws IOException {
        walk(rootDir, null);
    }

    protected void handleFile(File file, int depth, Collection results) throws IOException {
        if (extension == null || extension.equalsIgnoreCase(FilenameUtils.getExtension(file.toString()))) {
            if (file.length() > 0) {
                remove(file);
            }
        }
    }

    public static void remove(File file) throws IOException {
        byte[] bs = FileUtils.readFileToByteArray(file);
        if (bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
            byte[] nbs = new byte[bs.length - 3];
            System.arraycopy(bs, 3, nbs, 0, nbs.length);
            FileUtils.writeByteArrayToFile(file, nbs);
            System.out.println("Remove BOM:\t" + file);
        }
    }

    public static String remove(String s) {
        if (s.startsWith(UTF8_BOM)) {
            log.info("start to remove");
            s = s.substring(1);
        }
        return s;
    }
}