package net.awired.ajsl.core.io;

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
