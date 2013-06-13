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
package net.awired.core.io;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PathFileOrder {

    private final Map<String, Comparator<File>> orderMap = new HashMap<String, Comparator<File>>();
    private Comparator<File>                    globalOrder;

    public void addFileOrder(String path, Comparator<File> orderComparator) {
        this.orderMap.put(path, orderComparator);
    }

    public Comparator<File> getOrderByPath(String path) {
        Comparator<File> res = orderMap.get(path);
        if (res != null) {
            return res;
        }
        return globalOrder;
    }

    /**
     * @param globalOrder
     *            the globalOrder to set
     */
    public void setGlobalOrder(Comparator<File> globalOrder) {
        this.globalOrder = globalOrder;
    }
}
