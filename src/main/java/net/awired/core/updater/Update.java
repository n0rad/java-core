package net.awired.core.updater;

import lombok.Data;

@Data
public class Update implements Comparable<Update> {

    private final Version version;
    private final Updater updater;

    public Update(Version from, Updater updater) {
        this.version = from;
        this.updater = updater;
    }

    @Override
    public int compareTo(Update other) {
        return version.compareTo(other.getVersion());
    }

}
