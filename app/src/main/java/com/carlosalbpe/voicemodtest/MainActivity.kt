package com.carlosalbpe.voicemodtest

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import dagger.android.support.DaggerAppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            requestPremissions()
        }

        //Nav listener to hide/shiw FAB and ActionBar
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { controller, destination, _ ->
            if(controller.graph.startDestination == destination.id){
                supportActionBar?.show()
                fab?.show()
            }else{
                supportActionBar?.hide()
                fab.hide()
            }
        }
    }

    fun isShown() = fab.isVisible

    fun hideFab(){
        fab.hide()
    }

    fun showFab(){
        fab.show()
    }

    fun navigateToCamera(){
        findNavController(R.id.nav_host_fragment).navigate(R.id.action_VideoFragment_to_cameraFragment)
    }

    fun requestPremissions(){
        if (hasPermissions(this)) {
            onPermissionsGranted()
        } else {
            requestPermissions(
                PERMISSIONS_REQUIRED,
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    fun onPermissionsGranted(){
        navigateToCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionsGranted()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {

        const val PERMISSIONS_REQUEST_CODE = 1
        private val PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO)

    }

}
