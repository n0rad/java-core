package net.awired.core.updater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class UpdateRunner {

    private final List<Update> updates;

    protected abstract Version getCurrentVersion();

    protected abstract void setNewVersion(Version version);

    public UpdateRunner(Collection<Update> updates) {
        this.updates = new ArrayList<>(updates);
        checkNoOverlap();
        checkDiffUpdater();
        Collections.sort(this.updates);
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
            nextUpdate.getUpdater().update();
            setNewVersion(nextUpdate.getTo());
            return nextUpdate.getTo();
        } catch (Exception e) {
            throw new IllegalStateException("Exception during update : " + nextUpdate, e);
        }
    }

    public void updateTo(Version version) {
        Update next = null;
        do {
            updateFromOneVersion();
            next = findNextUpdate();
        } while (next != null && next.getFrom().compareTo(version) < 0);
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
            return updates.size() > 0 ? updates.get(0) : null;
        }
        for (Update update : updates) {
            if (update.getFrom().compareTo(currentVersion) >= 0) {
                return update;
            }
        }
        return null;
    }

    private void checkNoOverlap() {
        for (Update toCheck : updates) {
            for (Update update : updates) {
                if (toCheck != update && toCheck.getFrom().compareTo(update.getFrom()) > -1
                        && toCheck.getFrom().compareTo(update.getTo()) < 0) {
                    throw new IllegalStateException("Update : " + toCheck + " overlap " + update);
                }
            }
        }
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
