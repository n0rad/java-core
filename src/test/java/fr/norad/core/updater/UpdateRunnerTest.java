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

import static fr.norad.core.updater.Version.V;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import fr.norad.core.updater.Update;
import fr.norad.core.updater.UpdateRunner;
import fr.norad.core.updater.Updater;
import fr.norad.core.updater.Version;

public class UpdateRunnerTest {

    class TestUpdater implements Updater {
        public boolean updated = false;

        @Override
        public void update() {
            updated = true;
        }
    }

    class testUpdateRunner extends UpdateRunner {
        public testUpdateRunner(Set<Update> updates) {
            super("test", updates);
        }

        @Override
        protected Version getCurrentVersion() {
            return null;
        }

        @Override
        protected void setNewVersion(Version version) {
        }
    }

    @Test
    public void should_accept_continuation_version() throws Exception {
        Update one = new Update(V(1, 1, 0), new TestUpdater());
        Update two = new Update(V(1, 2, 0), new TestUpdater());

        new testUpdateRunner(new HashSet<>(Arrays.asList(one, two))).getCurrentVersion();
    }

    @Test(expected = IllegalStateException.class)
    public void should_different_updater() throws Exception {
        Updater updater = new TestUpdater();
        Update one = new Update(V(1, 1, 0), updater);
        Update two = new Update(V(1, 2, 0), updater);

        new testUpdateRunner(new HashSet<>(Arrays.asList(one, two))).getCurrentVersion();
    }
}
