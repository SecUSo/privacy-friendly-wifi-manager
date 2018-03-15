/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.logic.preconditions;

import android.content.Context;
import android.util.Pair;

import org.secuso.privacyfriendlywifimanager.logic.util.SerializationHelper;
import org.secuso.privacyfriendlywifimanager.logic.util.TimeHelper;

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
