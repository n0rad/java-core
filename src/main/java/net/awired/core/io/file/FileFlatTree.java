/**
 *
 *     Copyright (C) Awired.net
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package net.awired.core.io.file;

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
