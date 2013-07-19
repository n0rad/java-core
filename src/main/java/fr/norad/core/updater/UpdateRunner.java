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
package fr.norad.core.updater;

import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UpdateRunner {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<Update> updates;
    private final String name;

    protected abstract Version getCurrentVersion();

    protected abstract void setNewVersion(Version version);

    public UpdateRunner(String name, Set<Update> updates) {
        this.name = name;
        this.updates = new TreeSet<>(updates);
        checkDiffUpdater();
    }

    /**
     * @return null if nothing needs to be updated. Exception on error. New version on updated
     */
    public Version updateFromOneVersion() {
        Update nextUpdate = findNextUpdate();
        if (nextUpdate == null) {
            return null;
        }
        try {
            log.info("Updating " + name + " to " + nextUpdate.getVersion().toFullString());
            nextUpdate.getUpdater().update();
            setNewVersion(nextUpdate.getVersion());
            return nextUpdate.getVersion();
        } catch (Exception e) {
            throw new IllegalStateException("Exception during update : " + nextUpdate, e);
        }
    }

    public void updateTo(Version version) {
        Update next = null;
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

    public Update findNextUpdate() {
        Version currentVersion = getCurrentVersion();
        if (currentVersion == null) {
            return updates.size() > 0 ? updates.iterator().next() : null;
        }
        for (Update update : updates) {
            if (update.getVersion().compareTo(currentVersion) > 0) {
                return update;
            }
        }
        return null;
    }

    private void checkDiffUpdater() {
        for (Update toCheck : updates) {
            for (Update update : updates) {
                if (update.getUpdater() == null) {
                    throw new IllegalStateException("Updater cannot be null");
                }
                if (toCheck != update && toCheck.getUpdater() == update.getUpdater()) {
                    throw new IllegalStateException("Update " + toCheck + " and " + update + " have same updater");
                }
            }
        }
    }
}
