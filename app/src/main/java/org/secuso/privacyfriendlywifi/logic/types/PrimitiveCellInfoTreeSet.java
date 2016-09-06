package org.secuso.privacyfriendlywifi.logic.types;

import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 */
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
