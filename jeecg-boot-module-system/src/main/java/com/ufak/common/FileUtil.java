package com.ufak.common;

import java.io.File;


public class FileUtil {

    /**
     * 根据文件路路径删除文件
     *
     * @param filePath
     */
    public static void delete(String filePath) {
        String ctxPath = (String) ApplicationYmlUtil.getValue("jeecg.path.upload");
        File file = new File(ctxPath + File.separator + filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
