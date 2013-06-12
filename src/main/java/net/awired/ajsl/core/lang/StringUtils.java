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
package net.awired.ajsl.core.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils {

    public static String ucFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static StringBuilder inputStreamToStringBuilder(InputStream in, int minimumCapacity) {
        StringBuilder builder = new StringBuilder(minimumCapacity);
        inputStreamToStringBuilder(in, builder);
        return builder;
    }

    public static StringBuilder inputStreamToStringBuilder(InputStream in) {
        return inputStreamToStringBuilder(in, 16);
    }

    public static void inputStreamToStringBuilder(InputStream in, StringBuilder builder) {
        byte[] b = new byte[4096];
        try {
            for (int n; (n = in.read(b)) != -1;) {
                builder.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void inputStreamReaderToStringBuilder(InputStreamReader reader, StringBuilder builder) {
        char[] b = new char[4096];
        try {
            for (int n; (n = reader.read(b)) != -1;) {
                builder.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
