package eazyble.MasterSlave.Scanner;

import eazyble.Permissions;
import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scanning {
    // Declaring variables
    private BluetoothLeScanner bluetoothLeScanner;
    public static final long REQUEST_BLUETOOTH_SCAN_PERMISSION = 1;
    private boolean scanning;
    // Stop scanning after 10 seconds
    private static final long SCAN_PERIOD = 100000;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Context context;
    private final List<ScannerResultsBuilder> scanResults;
    private final ScanResultAdapter adapter;
    private final Set<String> scannedDevices = new HashSet<>();
    private final BluetoothAdapter bluetoothAdapter;

    public Scanning(Context context, List<ScannerResultsBuilder> scanResults, ScanResultAdapter adapter) {
        this.context = context;
        this.scanResults = scanResults;
        this.adapter = adapter;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkScanPermission();
        } else {
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        }
    }

    // Method to check Bluetooth permissions
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkScanPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN},
                    (int) REQUEST_BLUETOOTH_SCAN_PERMISSION);
        } //else {
        // BLUETOOTH_SCAN permission is already granted
        // Toast.makeText(context, "Scanning permission already granted", Toast.LENGTH_SHORT).show();
        // }
    }

    // Method to start scanning
    public void scanLeDevices() {
        Permissions.checkBluetoothSupport((Activity) context);
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(context, "Please Enable Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(() -> {
                scanning = false;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        checkScanPermission();
                    }
                }
                if (bluetoothLeScanner != null) {
                    bluetoothLeScanner.stopScan(scanCallback);
                    Toast.makeText(context, "Scanning stopped due to timeout", Toast.LENGTH_SHORT).show();
                }
            }, SCAN_PERIOD);

            scanning = true;
            if (bluetoothLeScanner == null) {
                bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            }
            if (bluetoothLeScanner != null) {
                ScanSettings settings = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    settings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build();
                }
                List<ScanFilter> filters = new ArrayList<>();
                bluetoothLeScanner.startScan(filters, settings, scanCallback);
            } else {
                // Handle null bluetoothLeScanner object
                Toast.makeText(context, "Bluetooth is turned off", Toast.LENGTH_SHORT).show();
            }
        } else {
            scanning = false;
            if (bluetoothLeScanner != null) {
                bluetoothLeScanner.stopScan(scanCallback);
                Toast.makeText(context, "Scanning is already in progress", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Callback for scanned results
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            List<ParcelUuid> uuids = result.getScanRecord().getServiceUuids();
            StringBuilder uuidMessage = new StringBuilder();
            if (uuids != null) {
                for (ParcelUuid uuid : uuids) {
                    uuidMessage.append(uuid.toString());
                }
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    checkScanPermission();
                }
            }
            String macAddress = result.getDevice().getAddress();
            String deviceName = result.getDevice().getName();

            // if the scanned device is not ble device
            if (deviceName == null || deviceName.isEmpty()) {
                deviceName = "N/A";
            }

            int rssi = result.getRssi();  // Get RSSI value
            int txPower = result.getScanRecord().getTxPowerLevel();

            // Define thresholds for proximity determination
            int veryClose = -50;
            int close = -70;
            String proximity;
            if (rssi >= veryClose) {
                proximity = "Near";
            } else if (rssi >= close) {
                proximity = "Close";
            } else {
                proximity = "Far";
            }
            // Update existing device in the list or add a new one
            boolean deviceUpdated = false;
            for (int i = 0; i < scanResults.size(); i++) {
                ScannerResultsBuilder scannedDevice = scanResults.get(i);
                if (scannedDevice.getMacAddress().equals(macAddress)) {
                    // Updating the existing device with new data
                    scannedDevice.setRssi(rssi);
                    scannedDevice.setProximity(proximity);
                    scannedDevice.setUuid(uuidMessage.toString());
                    deviceUpdated = true;
                    break;
                }
            }

            if (!deviceUpdated) {
                // Add new device to the scanResults if not already there
                scanResults.add(new ScannerResultsBuilder(deviceName, rssi, uuidMessage.toString(), macAddress, proximity, txPower));
            }

            // Notify the adapter of the changes
            adapter.notifyDataSetChanged();
        }
    };

    // Method to stop scanning
    public void stopScanning() {
        if (scanning) {
            scanning = false;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    checkScanPermission();
                }
            }
            bluetoothLeScanner.stopScan(scanCallback);
            Toast.makeText(context, "Scanning manually stopped", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Scanning is not active", Toast.LENGTH_SHORT).show();
        }
    }
}