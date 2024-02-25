package yolojj333.mymod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    public static final Logger log = LoggerFactory.getLogger("anarchy-mod");

    public static void info(String info) {
        log.info(info);
    }

    public static void warn(String warning) {
        log.warn(warning);
    }
}
