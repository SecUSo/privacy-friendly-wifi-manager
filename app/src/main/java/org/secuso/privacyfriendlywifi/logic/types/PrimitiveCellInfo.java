package org.secuso.privacyfriendlywifi.logic.types;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import org.secuso.privacyfriendlywifi.logic.util.Logger;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class PrimitiveCellInfo implements Serializable {
    private static String TAG = PrimitiveCellInfo.class.getSimpleName();
    private static int DEFAULT_SIGNAL_STRENGTH = 0;
    private int cellId; // the ID of the cell
    private double signalStrength; // in dBm
    private double minStrength;

    public PrimitiveCellInfo(int cellId, double signalStrength) {
        this.cellId = cellId;
        this.signalStrength = signalStrength;
    }

    public int getCellId() {
        return this.cellId;
    }

    public double getSignalStrength() {
        return this.signalStrength;
    }

    public boolean updateRange(double strength) {

        if (strength < this.minStrength) {
            Logger.d(TAG, "Updated signal strength of cid='" + this.cellId + "' to dBm=" + strength + ".");
            this.minStrength = strength;

            return true;
        }
        Logger.d(TAG, "Did not update cid=" + this.cellId + " - signal strength of " + strength + "dBm >= " + this.minStrength + "dBm");
        return false;
    }

    public boolean inRange(double strength) {
        return strength >= this.minStrength;
    }

    private static double getCurrentSignalStrength(NeighboringCellInfo cellInfo) {
        double dBm;
        switch (cellInfo.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                Logger.d(TAG, "CellType=GPRS");
            case TelephonyManager.NETWORK_TYPE_EDGE:
                Logger.d(TAG, "CellType=EDGE");
                if (cellInfo.getRssi() != NeighboringCellInfo.UNKNOWN_RSSI) {
                    dBm = -113 + 2 * cellInfo.getRssi(); //ASU to dBm conversion
                } else {
                    dBm = PrimitiveCellInfo.DEFAULT_SIGNAL_STRENGTH;
                }
                break;

            case TelephonyManager.NETWORK_TYPE_UMTS:
                Logger.d(TAG, "CellType=UMTS");
            case TelephonyManager.NETWORK_TYPE_HSPA:
                Logger.d(TAG, "CellType=HSPA");
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                Logger.d(TAG, "CellType=HSDPA");
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                Logger.d(TAG, "CellType=HSUPA");
                if (cellInfo.getRssi() != NeighboringCellInfo.UNKNOWN_RSSI) {
                    dBm = -115.5 + cellInfo.getRssi(); // CPICH RSCP to dBm
                    //dBm = cellInfo.getRssi(); // which one is right? no one knows
                } else {
                    dBm = PrimitiveCellInfo.DEFAULT_SIGNAL_STRENGTH;
                }
                break;
            // handle other cell types like LTE etc. (is it even possible? -> no, not possible)
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_LTE:
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                Logger.d(TAG, "CellType=OTHER");
                dBm = PrimitiveCellInfo.DEFAULT_SIGNAL_STRENGTH;
        }
        Logger.d(TAG, "Cell cid=" + cellInfo.getCid() + ", dBm=" + dBm);

        return dBm;
    }

    public static PrimitiveCellInfo getPrimitiveCellInfo(NeighboringCellInfo cellInfo) {
        int cellId = cellInfo.getCid(); // the cellId or NeighboringCellInfo.UNKNOWN_CID (-1)
        if (cellId == NeighboringCellInfo.UNKNOWN_CID) {
            return null; // unknown ID -> cell should not be considered
        }

        double dBm = getCurrentSignalStrength(cellInfo);

        return new PrimitiveCellInfo(cellId, dBm);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static PrimitiveCellInfo getPrimitiveCellInfo(CellInfo cellInfo) {
        /*
            There is no way to make this more appealing as Google did it like this themselves:
            https://github.com/android/platform_frameworks_base/blob/marshmallow-mr2-release/services/core/java/com/android/server/connectivity/NetworkMonitor.java
         */
        if (cellInfo instanceof CellInfoCdma) {
            return PrimitiveCellInfo.getPrimitiveCellInfo((CellInfoCdma) cellInfo);
        } else if (cellInfo instanceof CellInfoGsm) {
            return PrimitiveCellInfo.getPrimitiveCellInfo((CellInfoGsm) cellInfo);
        } else if (cellInfo instanceof CellInfoLte) {
            return PrimitiveCellInfo.getPrimitiveCellInfo((CellInfoLte) cellInfo);
        } else if (cellInfo instanceof CellInfoWcdma) {
            return PrimitiveCellInfo.getPrimitiveCellInfo((CellInfoWcdma) cellInfo);
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static PrimitiveCellInfo getPrimitiveCellInfo(CellInfoCdma cellInfo) {
        int cellId = cellInfo.getCellIdentity().getBasestationId();
        if (cellId == Integer.MAX_VALUE || cellId == NeighboringCellInfo.UNKNOWN_CID) {
            return null; // unknown ID -> cell should not be considered
        }

        double dBm = cellInfo.getCellSignalStrength().getDbm();

        Logger.d(TAG, "(new API) CellType=CDMA, cid=" + cellId + ", dBm=" + dBm);
        return new PrimitiveCellInfo(cellId, dBm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static PrimitiveCellInfo getPrimitiveCellInfo(CellInfoGsm cellInfo) {
        int cellId = cellInfo.getCellIdentity().getCid();
        if (cellId == Integer.MAX_VALUE || cellId == NeighboringCellInfo.UNKNOWN_CID) {
            return null; // unknown ID -> cell should not be considered
        }

        double dBm = cellInfo.getCellSignalStrength().getDbm();

        Logger.d(TAG, "(new API) CellType=GSM, cid=" + cellId + ", dBm=" + dBm);
        return new PrimitiveCellInfo(cellId, dBm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static PrimitiveCellInfo getPrimitiveCellInfo(CellInfoLte cellInfo) {
        int cellId = cellInfo.getCellIdentity().getCi();
        if (cellId == Integer.MAX_VALUE || cellId == NeighboringCellInfo.UNKNOWN_CID) {
            return null; // unknown ID -> cell should not be considered
        }

        double dBm = cellInfo.getCellSignalStrength().getDbm();
        if (dBm > 0) { // at least some manufacturers return values between 0 and +1130 instead of -113 to 0
            dBm = dBm / -10.0;
        }

        Logger.d(TAG, "(new API) CellType=LTE, cid=" + cellId + ", dBm=" + dBm);
        return new PrimitiveCellInfo(cellId, dBm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static PrimitiveCellInfo getPrimitiveCellInfo(CellInfoWcdma cellInfo) {
        int cellId = cellInfo.getCellIdentity().getCid();
        if (cellId == Integer.MAX_VALUE || cellId == NeighboringCellInfo.UNKNOWN_CID) {
            return null; // unknown ID -> cell should not be considered
        }

        double dBm = cellInfo.getCellSignalStrength().getDbm();

        Logger.d(TAG, "(new API) CellType=WCDMA, cid=" + cellId + ", dBm=" + dBm);
        return new PrimitiveCellInfo(cellId, dBm);
    }

    public static PrimitiveCellInfoTreeSet getAllCells(Context context) {

        // read cells in reach
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PrimitiveCellInfoTreeSet cellsInRange = new PrimitiveCellInfoTreeSet();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // get all cells for newer androids

            List<CellInfo> cells = telephonyManager.getAllCellInfo();

            if (cells != null && !cells.isEmpty()) { // this check is necessary since Samsung devices do not return any cell via getAllCellInfo
                for (CellInfo cell : cells) {
                    PrimitiveCellInfo primitiveCellInfo = PrimitiveCellInfo.getPrimitiveCellInfo(cell);
                    if (primitiveCellInfo != null) {
                        cellsInRange.add(primitiveCellInfo);
                    }
                }
            } else {
                cellsInRange = getLegacyCellInfo(telephonyManager);
            }
        } else {
            cellsInRange = getLegacyCellInfo(telephonyManager);
        }

        return cellsInRange;
    }

    static PrimitiveCellInfoTreeSet getLegacyCellInfo(TelephonyManager telephonyManager) {
        PrimitiveCellInfoTreeSet cellsInRange = new PrimitiveCellInfoTreeSet();
        int cellId = NeighboringCellInfo.UNKNOWN_CID;

        // get connected cell
        CellLocation location = telephonyManager.getCellLocation();

        if (location instanceof GsmCellLocation) {
            cellId = ((GsmCellLocation) location).getCid();
        } else if (location instanceof CdmaCellLocation) {
            cellId = ((CdmaCellLocation) location).getBaseStationId();
        }

        boolean found = false;

        // get neighboring cells
        List<NeighboringCellInfo> cells = telephonyManager.getNeighboringCellInfo();

        if (cells != null) {
            for (NeighboringCellInfo cell : cells) {
                PrimitiveCellInfo primitiveCellInfo = PrimitiveCellInfo.getPrimitiveCellInfo(cell);
                if (primitiveCellInfo != null) {
                    cellsInRange.add(primitiveCellInfo);

                    if (cell.getCid() == cellId) {
                        found = true;
                    }
                }
            }

        }

        if (!found && cellId != NeighboringCellInfo.UNKNOWN_CID) {
            // this is not the real strength, but as we are connected we can assume that it is the strongest available to us (in db)
            cellsInRange.add(new PrimitiveCellInfo(cellId, PrimitiveCellInfo.DEFAULT_SIGNAL_STRENGTH));
            Logger.d(TAG, "Connected cell not found in neighboring cells. Adding it with a signal strength of " + PrimitiveCellInfo.DEFAULT_SIGNAL_STRENGTH);
        }

        return cellsInRange;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof PrimitiveCellInfo && ((PrimitiveCellInfo) o).getCellId() == this.getCellId();
    }

    @Override
    public int hashCode() {
        return this.getCellId();
    }

    protected PrimitiveCellInfo(Parcel in) {
        cellId = in.readInt();
        signalStrength = in.readDouble();
    }
}
