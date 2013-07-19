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

import static fr.norad.core.updater.Version.V;
import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import fr.norad.core.updater.Update;
import fr.norad.core.updater.Updater;

public class UpdateTest {

    class TestUpdater implements Updater {
        public boolean updated = false;

        @Override
        public void update() {
            updated = true;
        }
    }

    @Test
    public void should_compare_version() throws Exception {
        Update update = new Update(V(2), new TestUpdater());
        Update update2 = new Update(V(3), new TestUpdater());

        assertThat(update.compareTo(update2)).isEqualTo(-100);
    }
}
