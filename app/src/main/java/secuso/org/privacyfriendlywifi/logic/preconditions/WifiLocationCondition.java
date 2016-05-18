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
public class WifiLocationCondition implements Precondition {
    @Override
    public boolean check(Context context) {


        // read cells in reach
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        List<PrimitiveCellInfo> cellsInRange = new LinkedList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // get all cells for newer androids
            List<CellInfo> cells = telephonyManager.getAllCellInfo();
            for (CellInfo cell : cells) {
                cellsInRange.add(PrimitiveCellInfo.getPrimitiveCellInfo(cell));
            }
        } else {
            // get connected cell
            CellLocation location = telephonyManager.getCellLocation();
            int cellId = Integer.MIN_VALUE;
            if(location instanceof GsmCellLocation){
                cellId = ((GsmCellLocation) location).getCid();
            }else if (location instanceof CdmaCellLocation){
                cellId = ((CdmaCellLocation) location).getBaseStationId();
            }
            cellsInRange.add(new PrimitiveCellInfo(cellId, /*TODO get real signal strength*/ Double.MIN_VALUE));

            // get neighboring cells
            List<NeighboringCellInfo> cells = telephonyManager.getNeighboringCellInfo();
            for (NeighboringCellInfo cell : cells) {
                cellsInRange.add(PrimitiveCellInfo.getPrimitiveCellInfo(cell));
            }
        }
        Log.i("TAG", "Number of cells detected: " + cellsInRange.size());
        // TODO: DO STUFF with cellsInRange


        return false;
    }
}
