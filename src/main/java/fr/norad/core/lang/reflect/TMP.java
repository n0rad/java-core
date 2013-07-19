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
package fr.norad.core.lang.reflect;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 */
public class TMP {

    /**
     * Cette methode permet de lister toutes les classes d'un package donne
     * 
     * @param pckgname
     *            Le nom du package a lister
     * @return La liste des classes
     */
    public List<Class<?>> getClasses(String pckgname) throws ClassNotFoundException, IOException {
        // Creation de la liste qui sera retournee
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        // On recupere toutes les entrees du CLASSPATH
        String[] entries = System.getProperty("java.class.path").split(System.getProperty("path.separator"));

        // Pour toutes ces entrees, on verifie si elles contiennent
        // un repertoire ou un jar
        for (String entrie : entries) {

            if (entrie.endsWith(".jar")) {
                classes.addAll(traitementJar(entrie, pckgname));
            } else {
                classes.addAll(traitementRepertoire(entrie, pckgname));
            }

        }

        return classes;
    }

    /**
     * Cette methode retourne la liste des classes presentes
     * dans un repertoire du classpath et dans un package donne
     * 
     * @param repertoire
     *            Le repertoire ou chercher les classes
     * @param pckgname
     *            Le nom du package
     * @return La liste des classes
     */
    private Collection<Class<?>> traitementRepertoire(String repertoire, String pckgname)
            throws ClassNotFoundException {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        // On genere le chemin absolu du package
        StringBuffer sb = new StringBuffer(repertoire);
        String[] repsPkg = pckgname.split("\\.");
        for (String element : repsPkg) {
            sb.append(System.getProperty("file.separator") + element);
        }
        File rep = new File(sb.toString());

        // Si le chemin existe, et que c'est un dossier, alors, on le liste
        if (rep.exists() && rep.isDirectory()) {
            // On filtre les entrees du repertoire
            FilenameFilter filter = new DotClassFilter();
            File[] liste = rep.listFiles(filter);

            // Pour chaque classe presente dans le package, on l'ajoute a la liste
            for (File element : liste) {
                classes.add(Class.forName(pckgname + "." + element.getName().split("\\.")[0]));
            }
        }

        return classes;
    }

    /**
     * Cette methode retourne la liste des classes presentes dans un jar du classpath et dans un package donne
     * 
     * @param repertoire
     *            Le jar ou chercher les classes
     * @param pckgname
     *            Le nom du package
     * @return La liste des classes
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Collection<Class<?>> traitementJar(String jar, String pckgname) throws IOException,
            ClassNotFoundException {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        JarFile jfile = new JarFile(jar);
        String pkgpath = pckgname.replace(".", "/");

        // Pour chaque entree du Jar
        for (Enumeration<JarEntry> entries = jfile.entries(); entries.hasMoreElements();) {
            JarEntry element = entries.nextElement();

            // Si le nom de l'entree commence par le chemin du package et finit par .class
            if (element.getName().startsWith(pkgpath) && element.getName().endsWith(".class")) {

                String nomFichier = element.getName().substring(pckgname.length() + 1);

                classes.add(Class.forName(pckgname + "." + nomFichier.split("\\.")[0]));

            }

        }

        return classes;
    }

    /**
     * Cette classe permet de filtrer les fichiers d'un repertoire. Il n'accepte que les fichiers .class.
     */
    private class DotClassFilter implements FilenameFilter {

        @Override
        public boolean accept(File arg0, String arg1) {
            return arg1.endsWith(".class");
        }

    }
}
