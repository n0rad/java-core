package net.awired.core.updater;

import lombok.Data;

@Data
public class Update implements Comparable<Update> {

    private final Version from;
    private final Version to;
    private final Updater updater;

    public Update(Version from, Version to, Updater updater) {
        if (from.compareTo(to) > -1) {
            throw new IllegalStateException(
                    "The 'to' version of update cannot be lower or equal to the 'from' version for : " + this);
        }
        this.from = from;
        this.to = to;
        this.updater = updater;
    }

    @Override
    public int compareTo(Update other) {
        return from.compareTo(other.getFrom());
    }

}
