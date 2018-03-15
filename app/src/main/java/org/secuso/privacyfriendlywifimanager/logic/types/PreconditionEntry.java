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

package org.secuso.privacyfriendlywifimanager.logic.types;

import org.secuso.privacyfriendlywifimanager.logic.util.SerializationHelper;

import java.util.Observable;
import java.util.Observer;

/**
 * A serializable precondition entry.
 */
public abstract class PreconditionEntry extends SerializationHelper implements Observer, Comparable<PreconditionEntry> {
    private static final long serialVersionUID = -8344028945131554037L;

    protected void notifyChanged() {
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public void update(Observable observable, Object data) {
        this.notifyChanged();
    }
}
