package secuso.org.privacyfriendlywifi.logic.types;

import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 */
public class PrimitiveCellInfoTreeSet extends TreeSet<PrimitiveCellInfo> {
    public PrimitiveCellInfoTreeSet() {
        super(new Comparator<PrimitiveCellInfo>() {
            @Override
            public int compare(PrimitiveCellInfo lhs, PrimitiveCellInfo rhs) {
                return lhs.equals(rhs) ? 0 : (int) (rhs.getSignalStrength() - lhs.getSignalStrength());
            }
        });
    }
}
