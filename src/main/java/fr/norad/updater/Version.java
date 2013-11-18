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
package fr.norad.updater;

import static java.lang.Integer.compare;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * if the suffix is different with same versions info, the comparison will be 0 but equals will be different.
 * if one has a suffix and not the other, the suffix version is lower.
 * number of compare is : (-100 +100 for major diff) + (-10 +10 for minor diff) + (-1 +1 for bugfix diff)
 */
public class Version implements Comparable<Version> {

    private static Pattern pattern = Pattern
            .compile("(Version\\ |V)?(?<major>[0-9]+)(?:\\.(?<minor>[0-9]+))?(?:\\.(?<bugfix>[0-9]+))?(?:\\-(?<suffix>[a-zA-Z0-9]*))?");

    private final int major;
    private final int minor;
    private final int bugfix;
    private final String suffix;

    public Version(int major, int minor, int bugfix) {
        this.major = major;
        this.minor = minor;
        this.bugfix = bugfix;
        this.suffix = null;
    }

    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
        this.bugfix = 0;
        this.suffix = null;
    }

    public Version(int major) {
        this.major = major;
        this.minor = 0;
        this.bugfix = 0;
        this.suffix = null;
    }

    public Version(int major, int minor, int bugfix, String suffix) {
        this.major = major;
        this.minor = minor;
        this.bugfix = bugfix;
        this.suffix = suffix;
    }

    public Version(int major, int minor, String suffix) {
        this.major = major;
        this.minor = minor;
        this.bugfix = 0;
        this.suffix = suffix;
    }

    public Version(int major, String suffix) {
        this.major = major;
        this.minor = 0;
        this.bugfix = 0;
        this.suffix = suffix;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bugfix;
        result = prime * result + major;
        result = prime * result + minor;
        result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version v2 = (Version) obj;
            if (suffix == null && v2.suffix != null || suffix != null && !suffix.equals(v2.suffix)) {
                return false;
            }
            return v2.major == major && v2.minor == minor && v2.bugfix == bugfix;
        }
        return false;
    }

    @Override
    public String toString() {
        return "V" + toVersionString();
    }

    public String toFullString() {
        return "Version " + toVersionString();
    }

    public String toVersionString() {
        StringBuilder builder = new StringBuilder();
        builder.append(major);
        if (minor != 0 || bugfix != 0) {
            builder.append('.');
            builder.append(minor);
        }
        if (bugfix != 0) {
            builder.append('.');
            builder.append(bugfix);
        }
        if (suffix != null) {
            builder.append('-');
            builder.append(suffix);
        }
        return builder.toString();
    }

    public String toFullVersionString() {
        StringBuilder builder = new StringBuilder();
        builder.append(major);
        builder.append('.');
        builder.append(minor);
        builder.append('.');
        builder.append(bugfix);
        if (suffix != null) {
            builder.append('-');
            builder.append(suffix);
        }
        return builder.toString();
    }

    public static Version parse(String versionString) {
        Matcher matcher = pattern.matcher(versionString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Version '" + versionString + "' cannot be parsed");
        }
        return new Version(Integer.parseInt(matcher.group("major")), //
                matcher.group("minor") == null ? 0 : Integer.parseInt(matcher.group("minor")), //
                matcher.group("bugfix") == null ? 0 : Integer.parseInt(matcher.group("bugfix")), //
                matcher.group("suffix"));
    }

    public static Version V(int major, int minor, int bugfix) {
        return new Version(major, minor, bugfix);
    }

    public static Version V(int major, int minor) {
        return new Version(major, minor);
    }

    public static Version V(int major) {
        return new Version(major);
    }

    public static Version V(int major, int minor, int bugfix, String suffix) {
        return new Version(major, minor, bugfix, suffix);
    }

    public static Version V(int major, int minor, String suffix) {
        return new Version(major, minor, suffix);
    }

    public static Version V(int major, String suffix) {
        return new Version(major, suffix);
    }

    @Override
    public int compareTo(Version other) {
        int diff = compare(major, other.major) * 100 + compare(minor, other.minor) * 10
                + compare(bugfix, other.bugfix);
        if (diff == 0) {
            if (suffix != null && other.suffix == null || suffix == null && other.suffix != null) {
                return suffix == null ? 1 : -1;
            }
            return 0;
        }
        return diff;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getBugfix() {
        return bugfix;
    }

    public String getSuffix() {
        return suffix;
    }

}
