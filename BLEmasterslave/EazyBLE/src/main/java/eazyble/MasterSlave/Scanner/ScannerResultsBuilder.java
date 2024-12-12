package eazyble.MasterSlave.Scanner;

// Data model to hold information about scanned devices
public class ScannerResultsBuilder {
    private final String deviceName;
    private int rssi;
    private String uuid;
    private final String macAddress;
    private String proximity;
    private final int txPower;

    // Constructor to initialize the scanned device
    public ScannerResultsBuilder(String deviceName, int rssi, String uuid, String macAddress, String proximity, int txPower) {
        this.deviceName = deviceName;
        this.rssi = rssi;
        this.uuid = uuid;
        this.macAddress = macAddress;
        this.proximity = proximity;
        this.txPower = txPower;
    }

    // Getters
    public String getDeviceName() {
        return deviceName;
    }

    public int getRssi() {
        return rssi;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getProximity() {
        return proximity;
    }

    public int getPower() {
        return txPower;
    }

    // Setters to update values in real-time
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

}

