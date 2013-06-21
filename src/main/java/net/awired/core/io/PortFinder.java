package net.awired.core.io;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class PortFinder {

    private static int MAX_TRIES = 100;

    public static int randomBetween(int start, int end) {
        return start + (int) (Math.random() * ((end - start) + 1));
    }

    public static Integer findAvailableBetween(int start, int end) {
        int tested = randomBetween(start, end);

        for (int i = 0; i < MAX_TRIES; i++) {
            if (isAvailable(tested)) {
                return tested;
            }
            tested = randomBetween(start, end);
        }
        throw new IllegalStateException("no available port found between " + start + " and " + end + " after "
                + MAX_TRIES + "tries");
    }

    /**
     * http://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java
     */
    public static boolean isAvailable(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
}
