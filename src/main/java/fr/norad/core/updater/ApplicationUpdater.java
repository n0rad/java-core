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
package fr.norad.core.updater;

import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ApplicationUpdater {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<ApplicationVersion> appVersions;
    private final String applicationName;

    protected abstract Version getCurrentVersion();

    protected abstract Set<String> getUpdatedNames(Version version);

    protected abstract void addUpdatedName(Version version, String name);

    public ApplicationUpdater(String applicationName, ApplicationVersion... appVersions) {
        this.applicationName = applicationName;
        this.appVersions = Collections.unmodifiableSet(new TreeSet<>(asList(appVersions)));
    }

    /**
     * @return null if nothing needs to be updated. Exception on error. New version on updated
     */
    public Version updateFromOneVersion() {
        ApplicationVersion nextUpdate = findNextUpdate();
        if (nextUpdate == null) {
            return null;
        }

        Set<String> updatedNames = getUpdatedNames(nextUpdate.getVersion());
        if (updatedNames == null) {
            updatedNames = new HashSet<>();
        }
        boolean updated = false;
        for (String name : nextUpdate.getUpdateNames()) {
            if (!updatedNames.contains(name)) {
                try {
                    log.info("## Updating " + applicationName + " to " + nextUpdate.getVersion().toFullString()
                            + " : " + name);
                    Update update = nextUpdate.getUpdate(name);
                    if (update == null) {
                        throw new IllegalStateException("Should find update with name : " + name + " at this point");
                    }
                    update.runUpdate();
                    addUpdatedName(nextUpdate.getVersion(), name);
                    updated = true;
                } catch (Exception e) {
                    throw new IllegalStateException("Exception during update : " + nextUpdate, e);
                }
            }
        }
        return updated ? nextUpdate.getVersion() : null;
    }

    public void updateTo(Version version) {
        ApplicationVersion next = null;
        do {
            updateFromOneVersion();
            next = findNextUpdate();
        } while (next != null && next.getVersion().compareTo(version) <= 0);
    }

    public void updateToLatest() {
        Version newVersion = null;
        do {
            newVersion = updateFromOneVersion();
        } while (newVersion != null);
    }

    /////////////////////////////////////////////

    public ApplicationVersion findNextUpdate() {
        Version currentVersion = getCurrentVersion();
        if (currentVersion == null) {
            return appVersions.size() > 0 ? appVersions.iterator().next() : null;
        }
        for (ApplicationVersion appVersion : appVersions) {
            Set<String> updatedNames = getUpdatedNames(currentVersion);
            if (appVersion.getVersion().equals(currentVersion)
                    && (updatedNames == null || !updatedNames.containsAll(appVersion.getUpdateNames()))) {
                return appVersion;
            } else if (appVersion.getVersion().compareTo(currentVersion) > 0) {
                return appVersion;
            }
        }
        return null;
    }

    public Set<ApplicationVersion> getAppVersions() {
        return appVersions;
    }

}
