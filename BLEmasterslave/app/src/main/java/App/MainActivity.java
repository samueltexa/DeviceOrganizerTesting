package App;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.blemasterslave.databinding.ActivityMainBinding;
import com.example.blemasterslave.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import eazyble.Permissions;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Logic for handling the bottom navigation
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            if (item.getItemId() == R.id.scanner) {
                navController.navigate(R.id.scanner);
                return true;
            } else if (item.getItemId() == R.id.advertiser) {
                navController.navigate(R.id.advertiser);
                return true;
            }
            return false;
        });
    }
    // Results returned from bluetooth decision after requesting permission
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Permissions.handleOnActivityResult(this, requestCode, resultCode);
    }
    public static final long REQUEST_BLUETOOTH_SCAN_PERMISSION = 1;
        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_SCAN_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with advertising
                Toast.makeText(this, "Scanning permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Scanning permission denied", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(requestCode==Permissions.REQUEST_LOCATION_PERMISSION){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    Permissions.grantedPermission(this);
                    //Location permission denied
                } else {
//                    Toast.makeText(this, "Advertisings permission denied", Toast.LENGTH_SHORT).show();
                    Permissions.deniedPermission(this);
                }
            }
        }
    }
}
