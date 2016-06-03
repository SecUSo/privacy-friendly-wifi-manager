package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;

import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import org.secuso.privacyfriendlywifi.logic.types.PrimitiveCellInfoTreeSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class CellLocationCondition implements Precondition {
    public final int MIN_CELLS = 3;
    public final double MIN_CELL_PERCENTAGE = 0.3;
    private Set<PrimitiveCellInfo> relatedCells;

    public CellLocationCondition() {
        super();
        this.relatedCells = new HashSet<>();
    }

    public int getNumberOfRelatedCells() {
        return relatedCells.size();
    }

    @Override
    public boolean check(Context context, Object obj) {
        Set<PrimitiveCellInfo> currentCells = (Set<PrimitiveCellInfo>) obj;
        HashSet<PrimitiveCellInfo> union = new HashSet<>(currentCells);
        union.retainAll(this.relatedCells);
        return ((double) union.size() / (double) this.relatedCells.size()) > MIN_CELL_PERCENTAGE || union.size() > MIN_CELLS;
    }

    public void addKBestSurroundingCells(Context context, int k, PrimitiveCellInfoTreeSet cells) {
        int i = 0;
        for (PrimitiveCellInfo cell : cells) {
            if (i >= k) {
                break;
            }
            this.relatedCells.add(cell);
        }
    }


    public void addKBestSurroundingCells(Context context, int k) {
        addKBestSurroundingCells(context, k, PrimitiveCellInfo.getAllCells(context));
    }

    public static boolean hasCoarseLocationPermission(Context context) {
        return (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.relatedCells.toArray(new PrimitiveCellInfo[this.relatedCells.size()]), 0);
    }

    protected CellLocationCondition(Parcel in) {
        this.relatedCells = new HashSet<PrimitiveCellInfo>(Arrays.asList(in.createTypedArray(PrimitiveCellInfo.CREATOR)));
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
