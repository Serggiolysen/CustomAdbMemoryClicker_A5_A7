package one.hix.testadbshell

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(Intent(this, AdbService::class.java))

        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopService(Intent(this, AdbService::class.java))
    }

    override fun onStop() {
        super.onStop()
        stopService(Intent(this, AdbService::class.java))
    }


}