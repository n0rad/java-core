package net.awired.ajsl.core.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Exec {

    private static final Logger LOG = Logger.getLogger(Exec.class.getName());

    public static List<String> runExec(String cmd) {
        try {
            //TODO change list type ? 
            List<String> res = new ArrayList<String>();

            Process process = Runtime.getRuntime().exec(cmd);
            LOG.info("Exec : " + cmd);
            process.waitFor();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                res.add(line);
            }
            LOG.info("Exec ExitValue : " + process.exitValue());
            return res;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
