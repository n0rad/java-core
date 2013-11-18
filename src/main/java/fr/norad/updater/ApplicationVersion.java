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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

@Data
public class ApplicationVersion implements Comparable<ApplicationVersion> {

    private final Version version;
    private final List<Update> updates;
    private final Version oldestVersionCompatible;

    public ApplicationVersion(Version version, Update updates) {
        this(version, (Version) null, updates);
    }

    public ApplicationVersion(Version version, Update... updates) {
        this(version, (Version) null, updates);
    }

    public ApplicationVersion(Version version, Version oldestVersionCompatible, Update... updates) {
        this.version = version;
        this.updates = Arrays.asList(updates);
        this.oldestVersionCompatible = oldestVersionCompatible;
        for (Update current : updates) {
            for (Update update : updates) {
                if (current != update && current.getName().equals(update.getName())) {
                    throw new IllegalStateException("Updates cannot have the same name : " + this);
                }
            }
        }
    }

    @Override
    public int compareTo(ApplicationVersion other) {
        return version.compareTo(other.getVersion());
    }

    public Update getUpdate(String name) {
        for (Update update : updates) {
            if (name.equals(update.getName())) {
                return update;
            }
        }
        return null;
    }

    public List<String> getUpdateNames() {
        List<String> names = new ArrayList<>();
        for (Update update : updates) {
            names.add(update.getName());
        }
        return names;
    }

}
