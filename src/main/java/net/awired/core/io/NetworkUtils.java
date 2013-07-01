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
package net.awired.core.io;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    public static String getFirstPublicIp() {
        Map<InetAddress, String> inetAddressMap = getInetAddressMap();
        for (InetAddress inetAddress : inetAddressMap.keySet()) {
            if (!inetAddress.isLoopbackAddress() && !inetAddress.isSiteLocalAddress()
                    && !(inetAddress.getHostAddress().indexOf(":") > -1)) {
                return inetAddress.getHostAddress();
            }
        }
        return null;
    }

    public static String getFirstNonLocalhostIp() {
        Map<InetAddress, String> inetAddressMap = getInetAddressMap();
        for (InetAddress inetAddress : inetAddressMap.keySet()) {
            if (!inetAddress.isLoopbackAddress() && !(inetAddress.getHostAddress().indexOf(":") > -1)) {
                return inetAddress.getHostAddress();
            }
        }
        return null;
    }

    public static String getFirstNonWifiIp() {
        Map<InetAddress, String> inetAddressMap = getInetAddressMap();
        for (InetAddress inetAddress : inetAddressMap.keySet()) {
            if (!inetAddress.isLoopbackAddress() && !(inetAddress.getHostAddress().indexOf(":") > -1)
                    && !inetAddressMap.get(inetAddress).startsWith("wlan")) {
                return inetAddress.getHostAddress();
            }
        }
        return null;
    }

    private static Map<InetAddress, String> getInetAddressMap() {
        Map<InetAddress, String> found = new HashMap<>();
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    found.put(address.nextElement(), ni.getName());
                }
            }
        } catch (SocketException e) {
        }
        return found;
    }

}
