package eazyble.MasterSlave.Scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.eazyble.R;

import java.util.List;

public class ScanResultAdapter extends ArrayAdapter<ScannerResultsBuilder> {
    public ScanResultAdapter(Context context, List<ScannerResultsBuilder> scanResults) {
        super(context, 0, scanResults);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.scannerlistbulder, parent, false);
        }
        // Getting the data items for the positions
        ScannerResultsBuilder result = getItem(position);

        // Finding views within layout
        TextView deviceNameTextView = convertView.findViewById(R.id.deviceNameTextView);
        TextView rssiTextView = convertView.findViewById(R.id.rssiTextView);
        TextView uuidTextView = convertView.findViewById(R.id.uuidTextView);
        TextView macAddressTextView = convertView.findViewById(R.id.macAddressTextView);
        TextView proximity = convertView.findViewById(R.id.proximity);
        TextView txPower = convertView.findViewById(R.id.txPower);


        // Populating the data into the template view using the data object
        if (result != null) {
            deviceNameTextView.setText("Device Name: " + result.getDeviceName());
            rssiTextView.setText("RSSI: " + result.getRssi() + " dBms");
            uuidTextView.setText("UUID: "+result.getUuid());
            macAddressTextView.setText("MAC ADDRESS: " + result.getMacAddress());
            proximity.setText("Proximity: " + result.getProximity());
            txPower.setText("txPower: " + result.getPower());
        }
        return convertView;
    }
}


