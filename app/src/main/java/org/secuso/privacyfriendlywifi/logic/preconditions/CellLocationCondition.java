package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfoTreeSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class CellLocationCondition extends Precondition {
    private static final String TAG = CellLocationCondition.class.getCanonicalName();
    public final int MIN_CELLS = 3;
    public final double MIN_CELL_PERCENTAGE = 0.3;
    private final String bssid;
    private Set<PrimitiveCellInfo> relatedCells;

    public CellLocationCondition(String bssid) {
        super();
        this.bssid = bssid;
        this.relatedCells = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CellLocationCondition && this.getBssid().equals(((CellLocationCondition) o).getBssid());
    }

    protected CellLocationCondition(Parcel in) {
        super(in);
        this.bssid = in.readString();
        this.relatedCells = new HashSet<PrimitiveCellInfo>(Arrays.asList(in.createTypedArray(PrimitiveCellInfo.CREATOR)));
    }

    public String getBssid() {
        return this.bssid;
    }

    @Override
    public boolean check(Context context, Object obj) {
        if (super.check(context, obj)) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Set<PrimitiveCellInfo> currentCells = (Set<PrimitiveCellInfo>) obj;
                HashSet<PrimitiveCellInfo> union = new HashSet<>(currentCells);
                union.retainAll(this.relatedCells);

                // return true if there are enough known cells in neighborhood or if there are at least MIN_CELLS known cells
                return ((double) union.size() / (double) this.relatedCells.size()) > MIN_CELL_PERCENTAGE || union.size() >= MIN_CELLS;
            } else {
                Log.e(TAG, "ACCESS_COARSE_LOCATION not granted.");
                return false;
            }
        }

        return false; // condition not active
    }

    public boolean addKBestSurroundingCells(int k, PrimitiveCellInfoTreeSet cells) {
        boolean modified = false;
        int i = 0;
        for (PrimitiveCellInfo cell : cells) {
            if (i >= k) {
                break;
            }

            modified |= this.relatedCells.add(cell);
            i++;
        }

        return modified;
    }


    public boolean addKBestSurroundingCells(Context context, int k) {
        return addKBestSurroundingCells(k, PrimitiveCellInfo.getAllCells(context));
    }

    public static boolean hasCoarseLocationPermission(Context context) {
        return (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    public int getNumberOfRelatedCells() {
        return relatedCells.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.bssid);
        dest.writeTypedArray(this.relatedCells.toArray(new PrimitiveCellInfo[this.relatedCells.size()]), 0);
    }


    public static final Creator<CellLocationCondition> CREATOR = new Creator<CellLocationCondition>() {
        @Override
        public CellLocationCondition createFromParcel(Parcel in) {
            return new CellLocationCondition(in);
        }

        @Override
        public CellLocationCondition[] newArray(int size) {
            return new CellLocationCondition[size];
        }
    };
}
