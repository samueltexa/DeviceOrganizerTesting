package App.Interfaces.Advertiser;

import android.bluetooth.le.AdvertiseSettings;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import eazyble.MasterSlave.Advertiser.Advertising;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.blemasterslave.R;

import java.util.UUID;

public class AdvertiserFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.advertising, container, false);

        Advertising advertising = new Advertising(requireContext());
        Button defaultAdvertise = root.findViewById(R.id.default_advertiser);
        defaultAdvertise.setOnClickListener(v -> {
                // Handle advertise button click
                advertising.startAdvertising(false, null, true);
        });

        Button stopAdvertise = root.findViewById(R.id.stop_advertiser);
        stopAdvertise.setOnClickListener(v -> {
            // Handle advertise button click
            advertising.stopAdvertising();
        });

        Button customAdvertise = root.findViewById(R.id.custom_advertiser);
        customAdvertise.setOnClickListener(v -> {
            // Handle advertise button click
            showPopup();
        });
        return root;
    }

    private void showPopup() {
        // Inflate the popup layout
        View popupView = getLayoutInflater().inflate(R.layout.advertiserconfiguration, null);
        popupView.setBackgroundColor(Color.WHITE);
        // Set the width and height of the popup
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // Get references to the UI elements in the popup
        EditText uuidEditText = popupView.findViewById(R.id.uuid_edit_text);
        CheckBox includeDeviceNameCheckBox = popupView.findViewById(R.id.device_name_checkbox);
        Button startButton = popupView.findViewById(R.id.advertiseButton);

        // Set a dismiss listener to close the popup when it's dismissed
        popupWindow.setOnDismissListener(() -> {
            // Handle dismiss event if needed
        });
         //Set a click listener for the start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = uuidEditText.getText().toString();
                boolean includeDeviceName = includeDeviceNameCheckBox.isChecked();
                // Check if UUID is in the correct format
                if (!isValidUUID(uuid)) {
                    // Show toast for wrong format
                    Toast.makeText(requireContext(), "Wrong UUID format", Toast.LENGTH_SHORT).show();
                    return; // Exit the method without starting advertising
                }

                Advertising advertising = new Advertising(requireContext());
                    advertising.startAdvertising(true, uuid, includeDeviceName);
                // Dismiss the popup
                popupWindow.dismiss();
            }
        });
        // Show the popup at the center of the screen
        popupWindow.showAtLocation(requireActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
