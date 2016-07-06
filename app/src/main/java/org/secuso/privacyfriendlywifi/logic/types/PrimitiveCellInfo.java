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

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class PrimitiveCellInfo implements Serializable {
    private int cellId; // the ID of the cell
    private double signalStrength; // in dBm

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

    public static PrimitiveCellInfo getPrimitiveCellInfo(NeighboringCellInfo cellInfo) {
        int cellId = cellInfo.getCid();
        double dBm;
        switch (cellInfo.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
                dBm = -113 + 2 * cellInfo.getRssi(); //ASU to dBm conversion
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                dBm = -115.5 + cellInfo.getRssi(); // CPICH RSCP to dBm
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
                dBm = Double.MIN_VALUE;
        }
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
        double dBm = cellInfo.getCellSignalStrength().getDbm();
        return new PrimitiveCellInfo(cellId, dBm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static PrimitiveCellInfo getPrimitiveCellInfo(CellInfoGsm cellInfo) {
        int cellId = cellInfo.getCellIdentity().getCid();
        double dBm = cellInfo.getCellSignalStrength().getDbm();
        return new PrimitiveCellInfo(cellId, dBm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static PrimitiveCellInfo getPrimitiveCellInfo(CellInfoLte cellInfo) {
        int cellId = cellInfo.getCellIdentity().getCi();
        double dBm = cellInfo.getCellSignalStrength().getDbm();
        return new PrimitiveCellInfo(cellId, dBm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static PrimitiveCellInfo getPrimitiveCellInfo(CellInfoWcdma cellInfo) {
        int cellId = cellInfo.getCellIdentity().getCid();
        double dBm = cellInfo.getCellSignalStrength().getDbm();
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
                    cellsInRange.add(PrimitiveCellInfo.getPrimitiveCellInfo(cell));
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
        int cellId = Integer.MIN_VALUE;

        // get connected cell
        CellLocation location = telephonyManager.getCellLocation();

        if (location instanceof GsmCellLocation) {
            cellId = ((GsmCellLocation) location).getCid();
        } else if (location instanceof CdmaCellLocation) {
            cellId = ((CdmaCellLocation) location).getBaseStationId();
        }

        // this is not the real strength, but as we are connected we can assume that it is the strongest available to us (in db)
        cellsInRange.add(new PrimitiveCellInfo(cellId, 0));

        // get neighboring cells
        List<NeighboringCellInfo> cells = telephonyManager.getNeighboringCellInfo();
        for (NeighboringCellInfo cell : cells) {
            cellsInRange.add(PrimitiveCellInfo.getPrimitiveCellInfo(cell));
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
