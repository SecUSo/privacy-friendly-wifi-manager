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

/**
 * Listener for delete mode in a RemovableRecyclerView.
 */
public interface IOnDeleteModeChangedListener {
    /**
     * Set the delete mode.
     *
     * @param isActive True if delete mode should be active, false otherwise.
     */
    void setDeleteModeActive(boolean isActive);

    /**
     * Returns true if delete mode should be active, false otherwise.
     *
     * @return True if delete mode should be active, false otherwise.
     */
    boolean isDeleteModeActive();
}
