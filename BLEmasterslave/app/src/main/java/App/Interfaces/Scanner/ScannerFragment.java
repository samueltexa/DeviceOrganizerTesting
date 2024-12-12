package App.Interfaces.Scanner;

import eazyble.MasterSlave.Scanner.ScanResultAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.blemasterslave.R;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import eazyble.MasterSlave.Scanner.Scanning;
import eazyble.MasterSlave.Scanner.ScannerResultsBuilder;

public class ScannerFragment extends Fragment {
    private ScanResultAdapter adapter;
    private final List<ScannerResultsBuilder> scanResults = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scannerlistview, container, false);

        ListView listView = rootView.findViewById(R.id.scannerListView);
        adapter = new ScanResultAdapter(getActivity(), scanResults);
        listView.setAdapter(adapter);

        // Initialize and start scanning
        Scanning scanning = new Scanning(requireActivity(), scanResults, adapter);
        scanning.scanLeDevices();

        return rootView;

    }

    // Method to update scannerListview results
    public void updateScanResults(List<ScannerResultsBuilder> results) {
        scanResults.clear();
        scanResults.addAll(results);
        adapter.notifyDataSetChanged();
    }
}