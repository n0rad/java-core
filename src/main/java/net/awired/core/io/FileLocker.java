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
