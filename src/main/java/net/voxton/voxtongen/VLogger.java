/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simplyianm
 */
public class VLogger {
    private static final Logger LOGGER = Logger.getLogger("Minecraft.VoxtonGen");

    public static final int VERBOSITY = 0;

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static void log(Level level, String msg) {
        msg = "[VG] " + msg;
        LOGGER.log(level, msg);
    }

    public static void log(Level level, String msg, Throwable thrown) {
        msg = "[VG] " + msg;
        LOGGER.log(level, msg, thrown);
    }

    public static void logVerbose(String message) {
        logVerbose(message, 1);
    }

    public static void logVerbose(String message, int level) {
        if (level > VERBOSITY) {
            return;
        }
        log("[V" + level + "] " + message);
    }

}
