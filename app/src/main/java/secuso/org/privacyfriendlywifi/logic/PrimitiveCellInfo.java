package secuso.org.privacyfriendlywifi.logic;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

/**
 *
 */
public class PrimitiveCellInfo {
    private int cellId; // the ID of the cell //TODO: consider hashing for privacy reasons
    private double signalStrength; // in dBm

    public PrimitiveCellInfo(int cellId, double signalStrength) {
        this.cellId = cellId;
        this.signalStrength = signalStrength;
    }

    public int getCellId() {
        return cellId;
    }

    public double getSignalStrength() {
        return signalStrength;
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
                dBm = -115.5 + cellInfo.getRssi(); // CPICH RSCP to dBm //TODO: check this calculation
                break;
            // TODO: handle other cell types like LTE etc. (is it even possible?)
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
        // TODO: this is really ugly. Think of a way to get better generics
        if (cellInfo instanceof CellInfoCdma) {
            return PrimitiveCellInfo.getPrimitiveCellInfo((CellInfoCdma) cellInfo);
        } else if (cellInfo instanceof CellInfoGsm) {
            return PrimitiveCellInfo.getPrimitiveCellInfo((CellInfoGsm) cellInfo);
        } else if (cellInfo instanceof CellInfoLte) {
            return PrimitiveCellInfo.getPrimitiveCellInfo((CellInfoLte) cellInfo);
        } else if (cellInfo instanceof CellInfoWcdma) {
            return PrimitiveCellInfo.getPrimitiveCellInfo((CellInfoWcdma) cellInfo);
        }
        return null; // TODO: maybe throw exception
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
}
