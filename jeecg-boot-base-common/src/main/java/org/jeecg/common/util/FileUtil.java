package org.jeecg.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Administrator on 2020/2/26.
 */
@Component
public class FileUtil {
    @Value(value = "${jeecg.path.upload}")
    private static String uploadpath;

    public static void delete(String filePath){
        String ctxPath = uploadpath;
        File file = new File(ctxPath + File.separator + filePath);
        if(file.exists()){
            file.delete();
        }
    }
}
