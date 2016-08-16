package org.secuso.privacyfriendlywifi.logic.util;

import android.content.Context;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * EXPERIMENTAL
 */
public abstract class SerializationHelper extends Observable implements Serializable {
    public static final String SERIALIZATION_ERROR = "SERIALIZATION ERROR";

    abstract public void initialize(Context context, Object[] args) throws IOException;

    abstract protected Object[] getPersistentFields();

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        for (Object obj : getPersistentFields()) {
            out.writeObject(obj);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        List<Object> restored = new ArrayList<>();

        for (Object obj : getPersistentFields()) {
            restored.add(in.readObject());
        }

        initialize(StaticContext.getContext(), restored.toArray());
    }
}
