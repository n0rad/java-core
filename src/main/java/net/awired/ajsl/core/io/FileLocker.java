package net.awired.ajsl.core.io;

import java.io.Closeable;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class FileLocker implements Closeable {
    private File file;
    private FileChannel channel;
    private FileLock lock;
    private Thread hook;

    public FileLocker(File file) {
        this.file = file;
    }

    public boolean isLocked() {
        try {
            channel = new RandomAccessFile(file, "rw").getChannel();
            try {
                lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                close();
                return true;
            }

            if (lock == null) {
                close();
                return true;
            }

            hook = new Thread() {
                @Override
                public void run() {
                    close();
                    deleteFile();
                }
            };
            Runtime.getRuntime().addShutdownHook(hook);
            return false;
        } catch (Exception e) {
            close();
            return true;
        }
    }

    @Override
    public void close() {
        if (hook != null) {
            try {
                Runtime.getRuntime().removeShutdownHook(hook);
            } catch (Exception e) {
                // skip cannot remove because shutdown is in progress
            }
        }

        try {
            lock.release();
        } catch (Exception e) {
        }
        try {
            channel.close();
        } catch (Exception e) {
        }
        deleteFile();
    }

    private void deleteFile() {
        try {
            file.delete();
        } catch (Exception e) {
        }
    }
}
