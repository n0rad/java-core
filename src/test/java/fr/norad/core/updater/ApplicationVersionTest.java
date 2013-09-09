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
import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;

public class ApplicationVersionTest {

    @Test
    public void should_compare_version() throws Exception {
        ApplicationVersion appVersion = new ApplicationVersion(V(2), (Update) null);
        ApplicationVersion appVersion2 = new ApplicationVersion(V(3), (Update) null);

        assertThat(appVersion.compareTo(appVersion2)).isEqualTo(-100);
    }

    @Test
    public void should_accept_single_varargs() throws Exception {
        ApplicationVersion appVersion = new ApplicationVersion(V(2), new Update("up") {
            @Override
            public void runUpdate() {
            }
        });

        assertThat(appVersion.getUpdates()).hasSize(1);

    }

    @Test
    public void should_accept_multi_varargs() throws Exception {
        ApplicationVersion appVersion = new ApplicationVersion(V(2), new Update("up") {
            @Override
            public void runUpdate() {
            }
        }, new Update("up2") {
            @Override
            public void runUpdate() {
            }
        });

        assertThat(appVersion.getUpdates()).hasSize(2);
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_accept_same_update_name() throws Exception {
        new ApplicationVersion(V(2), new Update("up") {
            @Override
            public void runUpdate() {
            }
        }, new Update("up") {
            @Override
            public void runUpdate() {
            }
        }).getUpdates();
    }
}
