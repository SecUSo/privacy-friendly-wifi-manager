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
