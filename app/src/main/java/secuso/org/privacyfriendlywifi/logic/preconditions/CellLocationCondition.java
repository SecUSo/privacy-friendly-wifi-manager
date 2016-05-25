package secuso.org.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.os.Parcel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import secuso.org.privacyfriendlywifi.logic.types.PrimitiveCellInfo;

/**
 *
 */
public class CellLocationCondition implements Precondition {
    private Set<PrimitiveCellInfo> allowedCells;

    public CellLocationCondition() {
        super();
    }

    @Override
    public boolean check(Context context) {


        // TODO: DO STUFF with cellsInRange


        return false;
    }

    public void addKBestSurroundingCells(Context context, int k) {
        int i = 0;
        for (PrimitiveCellInfo cell : PrimitiveCellInfo.getAllCells(context)) {
            if (i >= k) {
                break;
            }

            this.allowedCells.add(cell);
        }
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
