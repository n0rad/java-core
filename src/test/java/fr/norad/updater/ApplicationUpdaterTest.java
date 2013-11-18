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

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.HashSet;
import java.util.Set;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class ApplicationUpdaterTest {

    class TestUpdate extends Update {
        public TestUpdate(String name) {
            super(name);
        }

        public boolean updated;

        @Override
        public void runUpdate() {
            updated = true;
        }
    }

    class TestApplicationUpdater extends ApplicationUpdater {
        public Version version;
        public Set<String> updatedNames = new HashSet<>();

        public TestApplicationUpdater(ApplicationVersion... updates) {
            super("test", updates);
        }

        @Override
        protected Version getCurrentVersion() {
            return version;
        }

        @Override
        protected Set<String> getUpdatedNames(Version version) {
            if (this.version == null || this.version.equals(version)) {
                return updatedNames;
            }
            return null;
        }

        @Override
        protected void addUpdatedName(Version version, String name) {
            if (this.version == null || !this.version.equals(version)) {
                this.version = version;
                updatedNames = new HashSet<>();
            }
            updatedNames.add(name);
        }
    }

    @Test
    public void should_not_have_to_update() throws Exception {
        TestUpdate update = new TestUpdate("updateA");
        TestApplicationUpdater updater = new TestApplicationUpdater(new ApplicationVersion(Version.V(1), update));
        updater.version = Version.V(42);

        Version newVersion = updater.updateFromOneVersion();

        Assertions.assertThat(newVersion).isNull();
        Assertions.assertThat(update.updated).isFalse();
        Assertions.assertThat(updater.version).isEqualTo(Version.V(42));
    }

    @Test
    public void should_update() throws Exception {
        TestUpdate update = new TestUpdate("updateA");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(1), update));

        Version newVersion = runner.updateFromOneVersion();

        Assertions.assertThat(newVersion).isEqualTo(Version.V(1));
        Assertions.assertThat(update.updated).isTrue();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(1));
    }

    @Test
    public void should_find_good_update() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(0, 2), update02), //
                new ApplicationVersion(Version.V(1), update1));
        runner.updatedNames.add("update");
        runner.version = Version.V(0, 2);

        assertThat(runner.updateFromOneVersion()).isEqualTo(Version.V(1));
        Assertions.assertThat(update02.updated).isFalse();
        Assertions.assertThat(update1.updated).isTrue();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(1));
    }

    @Test
    public void should_find_good_update2() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(0, 2), update02), //
                new ApplicationVersion(Version.V(1), update1));
        runner.version = Version.V(0, 3);

        assertThat(runner.updateFromOneVersion()).isEqualTo(Version.V(1));
        Assertions.assertThat(update02.updated).isFalse();
        Assertions.assertThat(update1.updated).isTrue();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(1));
    }

    @Test
    public void should_update_to_latest() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(1), update1), //
                new ApplicationVersion(Version.V(0, 2), update02));

        runner.updateToLatest();

        Assertions.assertThat(update02.updated).isTrue();
        Assertions.assertThat(update1.updated).isTrue();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(1));
    }

    @Test
    public void should_update_to_specific_version() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestUpdate update2 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(1), update1), //
                new ApplicationVersion(Version.V(0, 2), update02), //
                new ApplicationVersion(Version.V(2), update2) //
        );

        runner.updateTo(Version.V(1));

        Assertions.assertThat(update02.updated).isTrue();
        Assertions.assertThat(update1.updated).isTrue();
        Assertions.assertThat(update2.updated).isFalse();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(1));
    }

    @Test
    public void should_update_to_specific_version3() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestUpdate update2 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(1), update1), //
                new ApplicationVersion(Version.V(0, 2), update02), //
                new ApplicationVersion(Version.V(2), update2) //
        );

        runner.updateTo(Version.V(0, 9));

        Assertions.assertThat(update02.updated).isTrue();
        Assertions.assertThat(update1.updated).isFalse();
        Assertions.assertThat(update2.updated).isFalse();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(0, 2));
    }

    @Test
    public void should_update_to_specific_version2() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestUpdate update2 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(1), update1), //
                new ApplicationVersion(Version.V(0, 2), update02), //
                new ApplicationVersion(Version.V(2), update2) //
        );
        runner.version = Version.V(0, 3);

        runner.updateTo(Version.V(1, 5));

        Assertions.assertThat(update02.updated).isFalse();
        Assertions.assertThat(update1.updated).isTrue();
        Assertions.assertThat(update2.updated).isFalse();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(1));
    }

    @Test
    public void should_run_multiple_update() throws Exception {
        TestUpdate update = new TestUpdate("update");
        TestUpdate updateA = new TestUpdate("updateA");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(1), update, updateA));

        Version newVersion = runner.updateFromOneVersion();

        Assertions.assertThat(newVersion).isEqualTo(Version.V(1));
        Assertions.assertThat(update.updated).isTrue();
        Assertions.assertThat(updateA.updated).isTrue();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(1));
    }

    @Test
    public void should_run_update_not_done_yet() throws Exception {
        TestUpdate update = new TestUpdate("update");
        TestUpdate updateA = new TestUpdate("updateA");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(Version.V(1), update, updateA));
        runner.version = Version.V(1);
        runner.updatedNames.add("update");

        Version newVersion = runner.updateFromOneVersion();

        Assertions.assertThat(newVersion).isEqualTo(Version.V(1));
        Assertions.assertThat(update.updated).isFalse();
        Assertions.assertThat(updateA.updated).isTrue();
        Assertions.assertThat(runner.version).isEqualTo(Version.V(1));
    }

}
