package eazyble.MasterSlave.Advertiser;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
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
import eazyble.Permissions;


// Advertising class
public class Advertising {
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private final BluetoothAdapter bluetoothAdapter;
    public static final long REQUEST_BLUETOOTH_ADVERTISE_PERMISSION = 1;
    private boolean advertising;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Context context;

    public Advertising(Context context) {
        this.context = context;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkAdvertisePermission();
        } else {
            bluetoothLeAdvertiser = this.bluetoothAdapter.getBluetoothLeAdvertiser();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkAdvertisePermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.BLUETOOTH_ADVERTISE},
                    (int) REQUEST_BLUETOOTH_ADVERTISE_PERMISSION);
        } //else {
        //Toast.makeText(context, "Advertising permission already granted", Toast.LENGTH_SHORT).show();
        // bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        // }
    }

    public void startAdvertising(boolean useCustomSettings, String uuid, boolean includeDeviceName) {
        Permissions.checkBluetoothSupport((Activity) context);
        if (!advertising) {
            long ADVERTISE_PERIOD = 100000;
            handler.postDelayed(() -> {
                advertising = false;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        checkAdvertisePermission();
                    }
                }
                if (bluetoothLeAdvertiser != null) {
                    bluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
                    Toast.makeText(context, "Advertising stopped due to timeout", Toast.LENGTH_SHORT).show();
                }
            }, ADVERTISE_PERIOD);

            AdvertiseSettings settings;
            AdvertiseData data;

            if (useCustomSettings) {
                settings = new AdvertiseSettings.Builder()
                        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH) // Set TxPower level
                        .setConnectable(false)
                        .build();

                AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
                if (includeDeviceName) {
                    String deviceName = bluetoothAdapter.getName();
                    if (deviceName != null && deviceName.length() > 10) {
                        deviceName = deviceName.substring(0, 5); // Truncate if longer than 10 characters
                    }
                    dataBuilder.setIncludeDeviceName(true);
                }
                if (uuid != null && !uuid.isEmpty()) {
                    dataBuilder.addServiceUuid(ParcelUuid.fromString(uuid));
                } else {
                    dataBuilder.addServiceUuid(ParcelUuid.fromString("0000110E-0000-1000-8000-00805F9B34FB"));
                }
                data = dataBuilder.build();
            } else {
                settings = new AdvertiseSettings.Builder()
                        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH ) // Set TxPower level
                        .setConnectable(false)
                        .build();

                data = new AdvertiseData.Builder()
                        .setIncludeDeviceName(true)
                        .setIncludeTxPowerLevel(true) // Include TxPower level in advertisement data
                        .addServiceUuid(ParcelUuid.fromString("0000110E-0000-1000-8000-00805F9B34FB"))
                        .build();
            }

            bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
            advertising = true;
            if (bluetoothLeAdvertiser != null) {
                bluetoothLeAdvertiser.startAdvertising(settings, data, advertiseCallback);
            } else {
                Toast.makeText(context, "Bluetooth is turned off", Toast.LENGTH_SHORT).show();
            }
        } else {
            advertising = false;
            if (bluetoothLeAdvertiser != null) {
                bluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
                Toast.makeText(context, "Advertising is already in Progress", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Toast.makeText(context, "Advertising started", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Toast.makeText(context, "Advertising failed with error code: " + errorCode, Toast.LENGTH_SHORT).show();
        }
    };

    public void stopAdvertising() {
        if (advertising) {
            advertising = false;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    checkAdvertisePermission();
                }
            }
            bluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
            Toast.makeText(context, "Advertising manually stopped", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Advertising is not active", Toast.LENGTH_SHORT).show();
        }
    }
}
