package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.os.Parcelable;

/**
 * Interface representing a precondition to check for.
 */
public interface Precondition extends Parcelable {

    /**
     * Check whether the precondition applies.
     *
     * @return True, if the precondition applies.
     */
    boolean check(Context context, Object obj);
}
