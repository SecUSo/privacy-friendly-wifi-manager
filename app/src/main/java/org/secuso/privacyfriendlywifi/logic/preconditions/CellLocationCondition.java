package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfoTreeSet;
import org.secuso.privacyfriendlywifi.logic.util.Logger;
import org.secuso.privacyfriendlywifi.logic.util.StaticContext;

import java.util.HashSet;
import java.util.Set;

/**
 * A condition that is true when the user is connected to a known cell.
 */
public class CellLocationCondition extends Precondition {
    private static final String TAG = CellLocationCondition.class.getCanonicalName();
    public final int MIN_CELLS = 3;
    public final double MIN_CELL_PERCENTAGE = 0.3;
    private final String bssid;
    private Set<PrimitiveCellInfo> relatedCells; // cells

    /**
     * Creates a new {@link CellLocationCondition}.
     *
     * @param bssid The BSSID (which should be equivalent to a MAC address from this perspective
     */
    public CellLocationCondition(String bssid) {
        super();
        this.bssid = bssid;
        this.relatedCells = new HashSet<>();
    }

    /* GETTERS & SETTERS */
    public int getNumberOfRelatedCells() {
        return relatedCells.size();
    }

    public String getBssid() {
        return this.bssid;
    }

    /**
     * Adds the k best surrounding cells to the {@code relatedCells} using
     * {@code addKBestSurroundingCells(PrimitiveCellInfoTreeSet cells, int k)}.
     *
     * @param context A context to use.
     * @param k       Defines how many surrounding cells should be added.
     * @return True if a cell has been added, false otherwise.
     */
    public boolean addKBestSurroundingCells(Context context, int k) {
        return addKBestSurroundingCells(PrimitiveCellInfo.getAllCells(context), k);
    }

    /**
     * Adds the k best surrounding cells to the {@code relatedCells}
     *
     * @param cells The cells to add.
     * @param k     Defines how many surrounding cells should be added.
     * @return True if a cell has been added, false otherwise.
     */
    public boolean addKBestSurroundingCells(PrimitiveCellInfoTreeSet cells, int k) {
        Logger.d(TAG, "Adding " + k + " best of " + cells.size() + " surrounding cells.");
        boolean modified = false;
        int i = 0;
        for (PrimitiveCellInfo cell : cells) {
            if (i >= k) {
                break;
            }

            modified |= this.relatedCells.add(cell);
            i++;
        }

        if (modified) {
            this.notifyChanged();

            return true;
        }

        return false;
    }

    /**
     * Check whether ACCESS_COARSE_LOCATION permission has been granted.
     *
     * @param context A context to use.
     * @return True if ACCESS_COARSE_LOCATION permission has been granted, false otherwise.
     */
    public static boolean hasCoarseLocationPermission(Context context) {
        return (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public boolean check(Object obj) {
        if (super.check(obj)) {
            if (ContextCompat.checkSelfPermission(StaticContext.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Set<PrimitiveCellInfo> currentCells = (Set<PrimitiveCellInfo>) obj;
                HashSet<PrimitiveCellInfo> union = new HashSet<>(currentCells);
                union.retainAll(this.relatedCells);

                Logger.d(TAG, "size(UNION(cells ^ related)) = " + union.size() + "; relatedCells.size() = " + this.relatedCells.size());

                // return true if there are enough known cells in neighborhood or if there are at least MIN_CELLS known cells
                return ((double) union.size() / (double) this.relatedCells.size()) > MIN_CELL_PERCENTAGE || union.size() >= MIN_CELLS;
            } else {
                Logger.e(TAG, "ACCESS_COARSE_LOCATION not granted.");
                return false;
            }
        }

        return false; // condition not active
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CellLocationCondition && this.getBssid().equals(((CellLocationCondition) o).getBssid());
    }
}
