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
package fr.norad.core.util;

import static fr.norad.core.util.MetaMessageBuilder.metaMsg;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MetaMessageBuilderTest {
    @Test
    public void should_create_log_with_empty_meta() throws Exception {
        Assertions.assertThat(metaMsg("hello").toString()).isEqualTo("hello");
    }

    @Test
    public void should_create_log() throws Exception {
        Assertions.assertThat(metaMsg("hello").meta("key", "val").toString()).isEqualTo("hello : key=val");
    }

    @Test
    public void should_create_log_with_metadatas() throws Exception {
        Assertions.assertThat(metaMsg("hello").meta("key", "value").meta("key2", "val2").toString())
                .isEqualTo("hello : key=value,key2=val2");
    }

}
