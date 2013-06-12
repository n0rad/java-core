package net.awired.ajsl.core.io;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileFlatTree {

    public static final Map<File, String> getTree(File root, String currentPath, PathFileFilter pathfilter,
            PathFileOrder pathOrder) {

        Map<File, String> res = new LinkedHashMap<File, String>();

        runTree(res, root, currentPath, pathfilter, pathOrder);

        return res;
    }

    private static void runTree(Map<File, String> res, File root, String relativeCurrentPath,
            PathFileFilter pathfilter, PathFileOrder pathOrder) {

        File[] files = root.listFiles(pathfilter.getFilterFromPath(relativeCurrentPath));

        if (pathOrder != null) {
            Arrays.sort(files, pathOrder.getOrderByPath(relativeCurrentPath));
        }

        for (File file : files) {
            if (!file.isDirectory()) {
                res.put(file, relativeCurrentPath + '/' + file.getName());
            }
        }
        for (File file : files) {
            if (file.isDirectory()) {
                runTree(res, file, relativeCurrentPath + '/' + file.getName(), pathfilter, pathOrder);
            }
        }

    }

}
