package net.awired.ajsl.core.io;

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
        Map<InetAddress, String> found = new HashMap<InetAddress, String>();
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
