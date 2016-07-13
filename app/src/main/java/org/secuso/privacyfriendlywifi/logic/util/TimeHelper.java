package org.secuso.privacyfriendlywifi.logic.util;

/**
 * Util class for time arithmetic
 */
public class TimeHelper {
    public static int getTimeDifference(int current_hour, int current_minute, int target_hour, int target_minute) {
        return ((target_hour - current_hour) * 60 + (target_minute - current_minute)) * 60;
    }

    public static boolean inTimespan(int start_hour, int start_minute, int end_hour, int end_minute, int current_hour, int current_minute) {
        if (start_hour < end_hour) {

            if (current_hour == start_hour) {
                return current_minute >= start_minute;
            }

            if (current_hour == end_hour) {
                return current_minute < end_minute;
            }

            return current_hour > start_hour && current_hour < end_hour;
        } else if (start_hour > end_hour) {
            if (current_hour == start_hour) {
                return current_minute >= start_minute;
            }

            if (current_hour == end_hour) {
                return current_minute < end_minute;
            }

            return current_hour > start_hour || current_hour < end_hour;
        } else {
            if (start_minute == end_minute) {
                return false;
            }
            if (current_hour == start_hour) {
                if (start_minute < end_minute) {
                    return current_minute >= start_minute && current_minute < end_minute;
                } else if (start_minute > end_minute) {
                    return current_minute <= start_minute || current_minute > end_minute;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

}
