/**
 *
 *     Copyright (C) norad.fr
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
package fr.norad.core.io;

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
