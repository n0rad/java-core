package net.awired.core.updater;

import static net.awired.core.updater.Version.V;
import static org.fest.assertions.api.Assertions.assertThat;
import java.util.Arrays;
import org.junit.Test;

public class UpdateRunnerLaunchedTest {

    class TestUpdater implements Updater {
        public boolean updated = false;

        @Override
        public void update() {
            updated = true;
        }

    }

    class testUpdateRunner extends UpdateRunner {
        public Version version = null;

        public testUpdateRunner(Update... updates) {
            super(Arrays.asList(updates));
        }

        @Override
        protected void setNewVersion(Version version) {
            this.version = version;
        }

        @Override
        protected Version getCurrentVersion() {
            return version;
        }
    }

    @Test
    public void should_not_have_to_update() throws Exception {
        TestUpdater updater = new TestUpdater();
        testUpdateRunner runner = new testUpdateRunner(new Update(V(0), V(1), updater));
        runner.version = V(42);

        assertThat(runner.updateFromOneVersion()).isNull();
        assertThat(updater.updated).isFalse();
        assertThat(runner.version).isEqualTo(V(42));
    }

    @Test
    public void should_update() throws Exception {
        TestUpdater updater = new TestUpdater();
        testUpdateRunner runner = new testUpdateRunner(new Update(V(0), V(1), updater));

        assertThat(runner.updateFromOneVersion()).isEqualTo(V(1));
        assertThat(updater.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_find_good_update() throws Exception {
        TestUpdater updater02 = new TestUpdater();
        TestUpdater updater1 = new TestUpdater();
        testUpdateRunner runner = new testUpdateRunner(new Update(V(0), V(0, 2), updater02), //
                new Update(V(0, 4), V(1), updater1));
        runner.version = V(0, 2);

        assertThat(runner.updateFromOneVersion()).isEqualTo(V(1));
        assertThat(updater02.updated).isFalse();
        assertThat(updater1.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_find_good_update2() throws Exception {
        TestUpdater updater02 = new TestUpdater();
        TestUpdater updater1 = new TestUpdater();
        testUpdateRunner runner = new testUpdateRunner(new Update(V(0), V(0, 2), updater02), //
                new Update(V(0, 4), V(1), updater1));
        runner.version = V(0, 3);

        assertThat(runner.updateFromOneVersion()).isEqualTo(V(1));
        assertThat(updater02.updated).isFalse();
        assertThat(updater1.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_update_to_latest() throws Exception {
        TestUpdater updater02 = new TestUpdater();
        TestUpdater updater1 = new TestUpdater();
        testUpdateRunner runner = new testUpdateRunner(new Update(V(0, 4), V(1), updater1), //
                new Update(V(0), V(0, 2), updater02));

        runner.updateToLatest();

        assertThat(updater02.updated).isTrue();
        assertThat(updater1.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_update_to_specific_version() throws Exception {
        TestUpdater updater02 = new TestUpdater();
        TestUpdater updater1 = new TestUpdater();
        TestUpdater updater2 = new TestUpdater();
        testUpdateRunner runner = new testUpdateRunner(new Update(V(0, 4), V(1), updater1), //
                new Update(V(0), V(0, 2), updater02), //
                new Update(V(1, 5), V(2), updater2) //
        );

        runner.updateTo(V(1, 4));

        assertThat(updater02.updated).isTrue();
        assertThat(updater1.updated).isTrue();
        assertThat(updater2.updated).isFalse();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_update_to_specific_version2() throws Exception {
        TestUpdater updater02 = new TestUpdater();
        TestUpdater updater1 = new TestUpdater();
        TestUpdater updater2 = new TestUpdater();
        testUpdateRunner runner = new testUpdateRunner(new Update(V(0, 4), V(1), updater1), //
                new Update(V(0), V(0, 2), updater02), //
                new Update(V(1, 5), V(2), updater2) //
        );

        runner.updateTo(V(1, 5));

        assertThat(updater02.updated).isTrue();
        assertThat(updater1.updated).isTrue();
        assertThat(updater2.updated).isFalse();
        assertThat(runner.version).isEqualTo(V(1));
    }

}
