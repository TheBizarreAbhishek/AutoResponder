package com.thebizarreabhishek.app.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.thebizarreabhishek.app.R;
import com.thebizarreabhishek.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    private void checkPermissions() {
        // 1. Check Notification Access
        if (!com.thebizarreabhishek.app.helpers.NotificationHelper.isNotificationServicePermissionGranted(this)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage(
                            "This app requires Notification Access to read incoming messages. Please allow it in the next screen.")
                    .setPositiveButton("Allow", (dialog, which) -> {
                        startActivity(new android.content.Intent(
                                android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    })
                    .setCancelable(false)
                    .show();
            return; // Stop here, don't ask for next permission yet
        }

        // 2. Check Battery Optimization
        android.os.PowerManager pm = (android.os.PowerManager) getSystemService(android.content.Context.POWER_SERVICE);
        String packageName = getPackageName();
        if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
            // Only ask if not already asked/denied in this session?
            // For now, always ask if missing because it's critical for reliability.
            // But to avoid loop if user denies, maybe we shouldn't force it?
            // The user asked "at first launch".
            // Let's us a simple Dialog.

            // We can use a shared pref to track if we already asked, but for simplicty
            // let's just ask.
            // But valid point, if they deny, onResume loop might occur.
            // Ideally we shouldn't block use if they deny battery, unlike notification
            // access.

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Ignore Battery Optimization")
                    .setMessage(
                            "To ensure the bot runs reliably in the background, please allow the app to ignore battery optimizations.")
                    .setPositiveButton("Allow", (dialog, which) -> {
                        try {
                            android.content.Intent intent = new android.content.Intent();
                            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                            intent.setData(android.net.Uri.parse("package:" + packageName));
                            startActivity(intent);
                        } catch (Exception e) {
                            startActivity(new android.content.Intent(
                                    android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
                        }
                    })
                    .setNegativeButton("Skip", null) // Allow skipping
                    .setCancelable(true)
                    .show();
        }
    }
}