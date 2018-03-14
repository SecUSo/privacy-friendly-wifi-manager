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

package org.secuso.privacyfriendlywifi.logic.types;

import java.util.Comparator;
import java.util.TreeSet;

public class PrimitiveCellInfoTreeSet extends TreeSet<PrimitiveCellInfo> {
    private static final long serialVersionUID = 8437449073091988702L;

    public PrimitiveCellInfoTreeSet() {
        super(new Comparator<PrimitiveCellInfo>() {
            @Override
            public int compare(PrimitiveCellInfo lhs, PrimitiveCellInfo rhs) {
                //      return > 0 if rhs should be before lhs
                //      return < 0 if lhs should be before rhs
                //      return 0 otherwise
                return lhs.equals(rhs) ? 0 : (int) (rhs.getSignalStrength() - lhs.getSignalStrength());
            }
        });
    }
}
