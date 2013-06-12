package net.awired.ajsl.core.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import com.google.common.io.ByteStreams;

public class JarUtils {

    public static File getUnpackedJarDir() {
        File file = new File(JarUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        //        Files.createTempDir();
        //        File tempDirectory = FileUtils.createTempDirectory();
        return file;
    }

    public static void unpackJarPart(File jarFile, String destDir, String matcher) throws IOException {
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> enume = jar.entries();
        while (enume.hasMoreElements()) {
            JarEntry file = enume.nextElement();
            if (!file.getName().matches(matcher)) {
                continue;
            }
            File f = new java.io.File(destDir + java.io.File.separator + file.getName());
            if (file.isDirectory()) {
                f.mkdirs();
                continue;
            }
            InputStream is = jar.getInputStream(file);
            FileOutputStream fos = new FileOutputStream(f);
            ByteStreams.copy(is, fos);
            fos.close();
            is.close();
        }
    }

    public static void unpackJar(File jarFile, String destDir) throws IOException {
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> enume = jar.entries();
        while (enume.hasMoreElements()) {
            JarEntry file = enume.nextElement();
            File f = new java.io.File(destDir + java.io.File.separator + file.getName());
            if (file.isDirectory()) {
                f.mkdir();
                continue;
            }
            InputStream is = jar.getInputStream(file);
            FileOutputStream fos = new FileOutputStream(f);
            ByteStreams.copy(is, fos);
            fos.close();
            is.close();
        }
    }
}
