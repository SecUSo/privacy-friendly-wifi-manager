package secuso.org.privacyfriendlywifi.logic.preconditions;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import secuso.org.privacyfriendlywifi.logic.PrimitiveCellInfo;

/**
 *
 */
public class CellLocationCondition implements Precondition {
    List<PrimitiveCellInfo> allowedCells;

    @Override
    public boolean check(Context context) {

        // read cells in reach
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        List<PrimitiveCellInfo> cellsInRange = new LinkedList<>();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // get all cells for newer androids

            List<CellInfo> cells = telephonyManager.getAllCellInfo();

            if (cells != null && !cells.isEmpty()) { // this check is necessary since Samsung devices do not return any cell via getAllCellInfo
                for (CellInfo cell : cells) {
                    cellsInRange.add(PrimitiveCellInfo.getPrimitiveCellInfo(cell));
                }
            } else {
                cellsInRange = getLegacyCellInfo(telephonyManager);
            }
        } else {
            cellsInRange = getLegacyCellInfo(telephonyManager);
        }
        Log.i("TAG", "Number of cells detected: " + cellsInRange.size());
        // TODO: DO STUFF with cellsInRange


        return false;
    }

    List<PrimitiveCellInfo> getLegacyCellInfo(TelephonyManager telephonyManager) {
        List<PrimitiveCellInfo> cellsInRange = new LinkedList<>();
        int cellId = Integer.MIN_VALUE;

        // get connected cell
        CellLocation location = telephonyManager.getCellLocation();

        if (location instanceof GsmCellLocation) {
            cellId = ((GsmCellLocation) location).getCid();
        } else if (location instanceof CdmaCellLocation) {
            cellId = ((CdmaCellLocation) location).getBaseStationId();
        }

        // TODO this is not the real strength, but as we are connected we can assume that it is the strongest available to us
        cellsInRange.add(new PrimitiveCellInfo(cellId, Double.MAX_VALUE));

        // get neighboring cells
        List<NeighboringCellInfo> cells = telephonyManager.getNeighboringCellInfo();
        for (NeighboringCellInfo cell : cells) {
            cellsInRange.add(PrimitiveCellInfo.getPrimitiveCellInfo(cell));
        }

        return cellsInRange;
    }
}
