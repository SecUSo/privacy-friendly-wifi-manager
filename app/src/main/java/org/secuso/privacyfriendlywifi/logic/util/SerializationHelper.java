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
