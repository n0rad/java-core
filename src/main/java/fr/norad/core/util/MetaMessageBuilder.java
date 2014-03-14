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

import java.util.ArrayList;
import java.util.List;

public class MetaMessageBuilder {
    private String message;
    private List<String> metadatas = new ArrayList<>();

    private MetaMessageBuilder(String message) {
        this.message = message;
    }

    public static MetaMessageBuilder metaMsg(String message) {
        return new MetaMessageBuilder(message);
    }

    private static String decorateValue(String value) {
        return value;
    }

    private static String joinKeyValue(String separator, Object key, Object value) {
        return key + separator + decorateValue(value.toString());
    }

    public MetaMessageBuilder meta(Object key, Object value) {
        metadatas.add(joinKeyValue("=", key, value));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        if (!metadatas.isEmpty()) {
            builder.append(" : ");
            int i = 0;
            for (String meta : metadatas) {
                if (i++ > 0) {
                    builder.append(",");
                }
                builder.append(meta);
            }
        }
        return builder.toString();
    }

}
