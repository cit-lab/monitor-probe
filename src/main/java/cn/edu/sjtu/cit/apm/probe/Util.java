package cn.edu.sjtu.cit.apm.probe;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by at15 on 15-8-24.
 */
public class Util {

    // TODO: this may not work on windows
    public static String getPID() {
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        String name = rt.getName();
        return name.substring(0, name.indexOf("@"));
    }
}
