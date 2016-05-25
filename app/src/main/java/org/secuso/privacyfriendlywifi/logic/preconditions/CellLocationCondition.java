package org.secuso.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.os.Parcel;

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
    private Set<PrimitiveCellInfo> allowedCells;

    public CellLocationCondition() {
        super();
    }

    @Override
    public boolean check(Context context, Object obj) {
        Set<PrimitiveCellInfo> currentCells = (Set<PrimitiveCellInfo>) obj;

        HashSet<PrimitiveCellInfo> union = new HashSet<>(currentCells);
        union.retainAll(this.allowedCells);

        return ((double) union.size() / (double) this.allowedCells.size()) > MIN_CELL_PERCENTAGE || union.size() > MIN_CELLS;
    }

    public void addKBestSurroundingCells(Context context, int k, PrimitiveCellInfoTreeSet cells) {
        int i = 0;
        for (PrimitiveCellInfo cell : cells) {
            if (i >= k) {
                break;
            }

            this.allowedCells.add(cell);
        }
    }


    public void addKBestSurroundingCells(Context context, int k) {
        addKBestSurroundingCells(context, k, PrimitiveCellInfo.getAllCells(context));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.allowedCells.toArray(new PrimitiveCellInfo[this.allowedCells.size()]), 0);
    }

    protected CellLocationCondition(Parcel in) {
        this.allowedCells = new HashSet<PrimitiveCellInfo>(Arrays.asList(in.createTypedArray(PrimitiveCellInfo.CREATOR)));
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
