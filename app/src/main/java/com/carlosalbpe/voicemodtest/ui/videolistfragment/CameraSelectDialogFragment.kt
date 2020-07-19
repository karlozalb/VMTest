package com.carlosalbpe.voicemodtest.ui.videolistfragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.carlosalbpe.voicemodtest.R
import kotlinx.android.synthetic.main.camera_select_fragment.*

class CameraSelectDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.camera_select_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        frontCameraBtn.setOnClickListener {
            navigateToCamera(true)
        }

        backCameraBtn.setOnClickListener {
            navigateToCamera(false)
        }
    }

    fun navigateToCamera(isFront :  Boolean){
        var action = CameraSelectDialogFragmentDirections.actionCameraSelectDialogFragmentToCameraFragment(isFront)
        findNavController().navigate(action)
    }

}