/*
 This file is part of Privacy Friendly Wifi Manager.
 Privacy Friendly Wifi Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Wifi Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Wifi Manager. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.util.Pair;

import org.secuso.privacyfriendlywifi.logic.util.SerializationHelper;
import org.secuso.privacyfriendlywifi.logic.util.TimeHelper;

import java.io.IOException;

public class ScheduleCondition extends Precondition {
    private static final long serialVersionUID = -8932924693319958978L;
    private int start_hour, start_minute, end_hour, end_minute;

    /**
     * Creates a new ScheduleCondition with the given time slot.
     * @param start_hour Start hour.
     * @param start_minute Start minute.
     * @param end_hour End hour.
     * @param end_minute End minute.
     */
    public ScheduleCondition(int start_hour, int start_minute, int end_hour, int end_minute) {
        initialize(start_hour, start_minute, end_hour, end_minute);
    }

    /**
     * Sets current instance to the given time slot.
     * @param start_hour Start hour.
     * @param start_minute Start minute.
     * @param end_hour End hour.
     * @param end_minute End minute.
     */
    public void initialize(int start_hour, int start_minute, int end_hour, int end_minute) {
        this.start_hour = start_hour;
        this.start_minute = start_minute;
        this.end_hour = end_hour;
        this.end_minute = end_minute;
    }

    /**
     * @see SerializationHelper
     */
    public void initialize(Context context, Object[] args) throws IOException {
        if (args[0] instanceof Integer && args[1] instanceof Integer && args[2] instanceof Integer && args[3] instanceof Integer) {
            initialize((int) args[0], (int) args[1], (int) args[2], (int) args[3]);
        } else {
            throw new IOException(SerializationHelper.SERIALIZATION_ERROR);
        }
    }

    /**
     * @see SerializationHelper
     */
    protected Object[] getPersistentFields() {
        return new Object[]{this.start_hour, this.start_minute, this.end_hour, this.end_minute};
    }

    @Override
    public boolean check(Object... obj) {
        Pair<Integer, Integer> currentTime = (Pair<Integer, Integer>) obj[0];
        // check, whether isActive and the given (current) time lies in between the start and end time
        return super.check(obj)
                && TimeHelper.inTimespan(this.start_hour, this.start_minute, this.end_hour, this.end_minute, currentTime.first, currentTime.second);
    }

    /**
     * Returns the start hour.
     * @return the start hour.
     */
    public int getStartHour() {
        return this.start_hour;
    }

    /**
     * Returns the start minute.
     * @return the start minute.
     */
    public int getStartMinute() {
        return this.start_minute;
    }

    /**
     * Returns the end hour.
     * @return the end hour.
     */
    public int getEndHour() {
        return this.end_hour;
    }

    /**
     * Returns the end minute.
     * @return the end minute.
     */
    public int getEndMinute() {
        return this.end_minute;
    }
}
