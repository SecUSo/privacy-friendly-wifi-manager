package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 */
public class Logger {
    private static final String TAG = Logger.class.getSimpleName();

    public static final int LOG_LEVEL_VERBOSE = 4;
    public static final int LOG_LEVEL_DEBUG = 3;
    public static final int LOG_LEVEL_INFO = 2;
    public static final int LOG_LEVEL_WARNING = 1;
    public static final int LOG_LEVEL_ERROR = 0;

    private static OutputStream output_stream = null;
    private static ArrayList<String> buffer = new ArrayList<>();

    /* Configurable parameters */
    /* For file output */
    private static DateFormat df = new SimpleDateFormat("MM-dd, HH:mm:ss", Locale.GERMANY);
    private static final String LOGFILE_NAME = "LOG";
    private static final int BUFFER_SIZE = 50;
    private static final boolean STORE_TO_FILE = true;

    /* General */
    private static final int LOG_LEVEL = LOG_LEVEL_VERBOSE;

    /**
     * Used to initialize FileOutputStream when context has been set.
     *
     * @return true, if the OutputStream could be created.
     */
    private static boolean init() {
        Context context = StaticContext.getContext();

        if (context != null) {
            try {
                Logger.output_stream = new FileOutputStream(new File(context.getFilesDir().getAbsolutePath().concat(File.separator).concat(Logger.LOGFILE_NAME)), true);
                return true;
            } catch (FileNotFoundException e) {
                Logger.logADB("e", TAG, "Log file not found.");
            }
        } else {
            Logger.logADB("e", TAG, "Cannot write log to file (context null).");
        }

        return false;
    }

    /**
     * Deletes the logfile if there is one. No exception will be thrown if there is no logfile. Check return value instead.
     * @return True, if logfile has been deleted, false otherwise.
     */
    public static boolean deleteLogFile() {
        Context context = StaticContext.getContext();
        File logfile = new File(context.getFilesDir().getAbsolutePath().concat(File.separator).concat(Logger.LOGFILE_NAME));
        return logfile.delete();
    }

    /**
     * Flushes the log buffer used for file logging.
     */
    public static void flush() {
        if (buffer.size() > 0) {
            try {
                if (Logger.init()) { // ensure there is a context

                    // assemble single string
                    StringBuilder sb = new StringBuilder();

                    for (String out : buffer) {
                        sb.append(out).append("\n");
                    }

                    FileHandler.storeObject(Logger.output_stream, sb.toString());
                    Logger.output_stream.close();
                }
            } catch (IOException e) {
                Logger.logADB("e", TAG, "Could not flush log.");
            }

            // clear buffer for next entries
            buffer.clear();
        }
    }

    /**
     * Logs a message to ADB and potentially to a file.
     *
     * @param level Log level out of {v,i,d,w,e}
     * @param tag   A tag to use.
     * @param msg   Message to log.
     */
    private static void log(String level, String tag, String msg) {
        if (Logger.STORE_TO_FILE) {
            buffer.add(df.format(GregorianCalendar.getInstance().getTime()) + " | [" + level + "] " + tag + ": " + msg);

            // flush if appropriate
            if (buffer.size() >= Logger.BUFFER_SIZE) {
                Logger.flush();
            }
        }

        Logger.logADB(level, tag, msg);
    }

    /**
     * Logs a message directly to ADB.
     *
     * @param level Log level out of {v,i,d,w,e}
     * @param tag   A tag to use.
     * @param msg   Message to log.
     */
    public static void logADB(String level, String tag, String msg) {

        try {
            Method logMethod = Log.class.getMethod(level, String.class, String.class); // get method
            logMethod.invoke(null, tag, msg); // invoke method
        } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            Logger.logADB("e", TAG, "Could not log data: " + tag + ": " + msg + "[" + level + "] because of: " + e.getClass().getSimpleName());
        }

    }

    /**
     * Logs a date.
     *
     * @param level Log level out of {v,i,d,w,e}
     * @param tag   A tag to use.
     * @param date  Date to log in format "MM-dd, HH:mm:ss".
     */
    public static void logDate(String level, String tag, Date date) {
        Logger.logADB(level, tag, Logger.df.format(date));
    }

    /**
     * Works as v(String tag, String msg) in {@link Log}.
     */
    public static void v(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_VERBOSE) {
            log("v", tag, msg);
        }
    }

    /**
     * Works as d(String tag, String msg) in {@link Log}.
     */
    public static void d(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_DEBUG) {
            log("d", tag, msg);
        }
    }

    /**
     * Works as i(String tag, String msg) in {@link Log}.
     */
    public static void i(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_INFO) {
            log("i", tag, msg);
        }
    }

    /**
     * Works as w(String tag, String msg) in {@link Log}.
     */
    public static void w(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_WARNING) {
            log("w", tag, msg);
        }
    }

    /**
     * Works as e(String tag, String msg) in {@link Log}.
     */
    public static void e(String tag, String msg) {
        if (LOG_LEVEL >= LOG_LEVEL_ERROR) {
            log("e", tag, msg);
        }
    }
}
