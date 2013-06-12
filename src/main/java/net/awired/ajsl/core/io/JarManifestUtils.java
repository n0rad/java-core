package net.awired.ajsl.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

public class JarManifestUtils {

    public static String getManifestAttribute(String attributeName) {
        try {
            Enumeration<URL> manifests = JarManifestUtils.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (manifests.hasMoreElements()) {
                URL res = manifests.nextElement();
                InputStream openStream = res.openStream();
                try {
                    Manifest manifest = new Manifest(openStream);
                    String versionManifest = manifest.getMainAttributes().getValue(attributeName);
                    if (versionManifest != null) {
                        return versionManifest;
                    }
                } finally {
                    try {
                        openStream.close();
                    } catch (IOException e) {
                    }
                }

            }
        } catch (IOException e) {
        }
        return null;
    }

}
