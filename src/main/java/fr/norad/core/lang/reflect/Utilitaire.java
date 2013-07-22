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
package fr.norad.core.lang.reflect;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * lister les classes d'un package dans une webapp
 * TODO: a tester!
 */

public class Utilitaire {
    private static final String JAR_URL_SEPARATOR = "!/";
    private static final String FILE_URL_PREFIX = "file:";

    public static ArrayList<String> lireListService(String nom_package) {

        // Instanciation de la liste de service
        ArrayList<String> listService = new ArrayList<String>();

        // Mis en forme du nom du package pour la methode getresources (pour la methode getresource, il faut rajouter / au debut de la chaine 
        String name = nom_package.replace('.', '/');

        // TODO : Necessaire sous Websphere - A ne pas mettre sous Jonas
        name = name + "/";

        URL url = null;
        Enumeration<?> enumm = null;
        try {
            // Recuperation sous forme d'enumeration de la liste des URL contenant ce package
            enumm = Utilitaire.class.getClassLoader().getResources(name);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (enumm != null) {
            // Parcourt la liste des URL
            while (enumm.hasMoreElements()) {

                // Recuperation d'une URL
                url = (URL) enumm.nextElement();

                File directory = new File(url.getFile());

                // New code
                // ======
                if (directory.exists()) {
                    // Recupere la liste des fichiers dans le package (si les classes sont directement dans un repertoire)
                    String[] files = directory.list();
                    for (String file : files) {

                        // Ne prends que le fichier finissant par .class. Ne pas tenir compte des sources si present.
                        if (file.endsWith(".class")) {
                            // removes the .class extension
                            String classname = file.substring(0, file.length() - 6);

                            listService.add(classname);
                        }
                    }
                } else {
                    try {
                        // Si on est pas dans un repertoire
                        // le package doit etre contenu dans un jar
                        URLConnection con = url.openConnection();
                        JarFile jfile = null;
                        String starts = null;

                        if (con instanceof JarURLConnection) {
                            // Should usually be the case for traditional JAR files. 
                            JarURLConnection jarCon = (JarURLConnection) con;
                            jfile = jarCon.getJarFile();
                            starts = jarCon.getEntryName();
                        } else {
                            String urlFile = url.getFile();
                            int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
                            String jarFileUrl = urlFile.substring(0, separatorIndex);
                            if (jarFileUrl.startsWith(FILE_URL_PREFIX)) {
                                jarFileUrl = jarFileUrl.substring(FILE_URL_PREFIX.length());
                            }
                            jfile = new JarFile(jarFileUrl);
                            starts = urlFile.substring(separatorIndex + JAR_URL_SEPARATOR.length());
                        }

                        Enumeration<?> e = jfile.entries();
                        while (e.hasMoreElements()) {
                            // Parcourt les differents elements du jar
                            ZipEntry entry = (ZipEntry) e.nextElement();
                            String entryname = entry.getName();
                            if (entryname.startsWith(starts) && (entryname.lastIndexOf('/') <= starts.length())
                                    && entryname.endsWith(".class")) {
                                // Ne prends que les fichiers class appartenant au package dans le JAR

                                // Ne recupere que le nom de la classe sans le package
                                String classname = entryname.substring(0, entryname.length() - 6);
                                if (classname.startsWith("/")) {
                                    classname = classname.substring(1);
                                }
                                classname = classname.substring(starts.length());
                                listService.add(classname);
                            }
                        }
                    } catch (IOException ioex) {
                        System.err.println(ioex);
                    }
                }
            }
        }

        return listService;

    }

}
