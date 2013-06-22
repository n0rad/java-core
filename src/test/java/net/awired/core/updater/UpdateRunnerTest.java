package net.awired.core.updater;

import static net.awired.core.updater.Version.V;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;

public class UpdateRunnerTest {

    class TestUpdater implements Updater {
        public boolean updated = false;

        @Override
        public void update() {
            updated = true;
        }
    }

    class testUpdateRunner extends UpdateRunner {
        public testUpdateRunner(Collection<Update> updates) {
            super(updates);
        }

        @Override
        protected Version getCurrentVersion() {
            return null;
        }

        @Override
        protected void setNewVersion(Version version) {
        }
    }

    @Test(expected = IllegalStateException.class)
    public void should_detect_global_overlap() throws Exception {
        Update one = new Update(V(1, 0, 0), V(1, 1, 0), new TestUpdater());
        Update two = new Update(V(1, 0, 42), V(1, 2, 0), new TestUpdater());

        new testUpdateRunner(Arrays.asList(one, two)).getCurrentVersion();
    }

    @Test(expected = IllegalStateException.class)
    public void should_detect_starting_overlap() throws Exception {
        Update one = new Update(V(1, 0, 0), V(1, 1, 0), new TestUpdater());
        Update two = new Update(V(1, 0, 0), V(1, 2, 0), new TestUpdater());

        new testUpdateRunner(Arrays.asList(one, two)).getCurrentVersion();
    }

    @Test
    public void should_accept_continuation_version() throws Exception {
        Update one = new Update(V(1, 0, 0), V(1, 1, 0), new TestUpdater());
        Update two = new Update(V(1, 1, 0), V(1, 2, 0), new TestUpdater());

        new testUpdateRunner(Arrays.asList(one, two)).getCurrentVersion();
    }

    @Test(expected = IllegalStateException.class)
    public void should_different_updater() throws Exception {
        Updater updater = new TestUpdater();
        Update one = new Update(V(1, 0, 0), V(1, 1, 0), updater);
        Update two = new Update(V(1, 1, 0), V(1, 2, 0), updater);

        new testUpdateRunner(Arrays.asList(one, two)).getCurrentVersion();
    }
}
