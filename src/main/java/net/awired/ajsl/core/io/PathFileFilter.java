package net.awired.ajsl.core.io;

import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

public class PathFileFilter {

    private final Map<String, FileFilter> filterMap = new HashMap<String, FileFilter>();
    private FileFilter                    globalFilter;

    /**
     * 
     * TODO create a superfilter containing all filter (using a filter chain) and return it.
     * 
     * @param path
     * @return
     */
    public FileFilter getFilterFromPath(String path) {
        return globalFilter;
    }

    /**
     * @param globalFilter
     *            the globalFilter to set
     */
    public void setGlobalFilter(FileFilter globalFilter) {
        this.globalFilter = globalFilter;
    }
}
