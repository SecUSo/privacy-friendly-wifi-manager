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

package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Helper to serialize a class manually.
 * Comes with the advantage that serialized objects can be deserialized even if the related class has been modified.
 */
public abstract class SerializationHelper extends Observable implements Serializable {
    public static final String SERIALIZATION_ERROR = "SERIALIZATION ERROR";
    private static final long serialVersionUID = 5473512705567192831L;

    /**
     * Initializes a SerializationHelper inheritor using passed args.
     * @param context A context to use.
     * @param args Object array of args to pass.
     * @throws IOException when the arguments do not suit the parameters.
     */
    abstract public void initialize(Context context, Object[] args) throws IOException;

    /**
     * Returns all the objects that should be serialized.
     * @return Array of all objects to serialize.
     */
    abstract protected Object[] getPersistentFields();

    /**
     * @see Serializable
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        for (Object obj : getPersistentFields()) {
            out.writeObject(obj);
        }
    }

    /**
     * @see Serializable
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        List<Object> restored = new ArrayList<>();

        for (Object obj : getPersistentFields()) {
            restored.add(in.readObject());
        }

        initialize(StaticContext.getContext(), restored.toArray());
    }
}
