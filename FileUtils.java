package net.awired.ajsl.core.io;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class FileUtils {

    /**
     * Maximum loop count when creating temp directories.
     */
    private static final int TEMP_DIR_ATTEMPTS = 10000;

    static {
        try {
            Class.forName(Files.class.getName());
        } catch (ClassNotFoundException e1) {
            System.err.println("Cannot load " + Files.class.getName() + " class for " + FileUtils.class.getName()
                    + " shudown hook");
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LinkedHashSet<String> theFiles;

                synchronized (FileUtils.class) {
                    theFiles = files;
                    files = null;
                }

                ArrayList<String> toBeDeleted = new ArrayList<String>(theFiles);
                Collections.reverse(toBeDeleted);
                for (String filename : toBeDeleted) {
                    try {
                        Files.deleteRecursively(new File(filename));
                    } catch (IOException e) {
                        // cannot clean temp directory
                    }
                }
            }
        });
    }

    private static LinkedHashSet<String> files = new LinkedHashSet<String>();

    static synchronized void shutdownDeleteAdd(String file) {
        if (files == null) {
            throw new IllegalStateException("Shutdown in progress");
        }
        files.add(file);
    }

    /**
     * Copy of Files.createTempDir but with prefix
     */
    public static File createTempDir(String prefix) {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, prefix + baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within " + TEMP_DIR_ATTEMPTS
                + " attempts (tried " + prefix + baseName + "0 to " + prefix + baseName + (TEMP_DIR_ATTEMPTS - 1)
                + ')');
    }

    /**
     * This method use memory to keep dirs to delete until the JVM ends, use with caution.
     */
    public static File createTempDirectoryWithDeleteOnExit(String prefix) {
        File tempDir = createTempDir(prefix);
        shutdownDeleteAdd(tempDir.getPath());
        return tempDir;
    }

    /**
     * This method use memory to keep dirs to delete until the JVM ends, use with caution.
     */
    public static File createTempDirectoryWithDeleteOnExit() {
        File tempDir = Files.createTempDir();
        shutdownDeleteAdd(tempDir.getPath());
        return tempDir;
    }
}
