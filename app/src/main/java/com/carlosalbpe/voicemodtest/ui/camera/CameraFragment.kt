package com.carlosalbpe.voicemodtest.ui.camera

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.hardware.camera2.*
import android.media.MediaCodec
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.carlosalbpe.voicemodtest.R
import com.carlosalbpe.voicemodtest.framework.io.FileStorage
import com.carlosalbpe.voicemodtest.framework.utils.Status
import com.carlosalbpe.voicemodtest.ui.camera.viewmodel.CameraViewModel
import com.carlosalbpe.voicemodtest.ui.utils.toast
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine



class CameraFragment @Inject constructor() : DaggerFragment() {

    lateinit var outputFile : File
    lateinit var cameraId : String

    @Inject
    lateinit var cameraManager: CameraManager

    @Inject
    lateinit var fileStorage : FileStorage

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CameraViewModel

    private lateinit var characteristics: CameraCharacteristics

    private var height : Int = 0
    private var width : Int = 0
    private var fps : Int = 0

    private val recorderSurface: Surface by lazy {

        outputFile = fileStorage.createFile()

        // Get a persistent Surface from MediaCodec, don't forget to release when done
        val surface = MediaCodec.createPersistentInputSurface()

        // Prepare and release a dummy MediaRecorder with our new surface
        // Required to allocate an appropriately sized buffer before passing the Surface as the
        //  output target to the capture session
        createRecorder(surface).apply {
            prepare()
            release()
        }

        surface
    }

    private val recorder: MediaRecorder by lazy { createRecorder(recorderSurface) }

    private val cameraThread = HandlerThread("CameraThread").apply { start() }

    private val cameraHandler = Handler(cameraThread.looper)

    private lateinit var viewFinder: AutoFitSurfaceView

    private lateinit var session: CameraCaptureSession

    private lateinit var camera: CameraDevice

    private var recording : Boolean = false
    private var recordingStarted : Boolean = false

    private lateinit var previewRequest: CaptureRequest
    private lateinit var recordRequest: CaptureRequest

    private var recordingStartMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configBackCamera()
     }

    override fun onDestroyView() {
        if (!recordingStarted){
            outputFile.delete()
        }
        super.onDestroyView()
    }

    fun configBackCamera(){
        var cameraIds = cameraManager.cameraIdList

        //Back camera
        cameraId = cameraIds.first { cameraId ->
            cameraManager.getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
        }

        //Camera properties
        characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val cameraConfig = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!

        var maxHeight : Int = 0
        var maxWidth : Int = 0
        var maxFps : Int = 0

        //Get the config with the highest resolution (and the max fps for that resolution (CarlosA)
        val targetClass = MediaRecorder::class.java
        cameraConfig.getOutputSizes(targetClass).forEach { size ->
            val seconds = cameraConfig.getOutputMinFrameDuration(targetClass, size) / 1000000000.0
            val fps = if (seconds > 0) (1.0 / seconds).toInt() else 0
            if (size.height >= maxHeight && size.width >= maxWidth) {
                maxHeight = size.height
                maxWidth = size.width
                maxFps = if (fps > maxFps) fps else maxFps
            }
        }

        height = maxHeight
        width = maxWidth
        fps = maxFps

        requireContext().toast("$height x $width @ $fps")
    }

    fun isFrontCamera() : Boolean {
        return cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
    }

    fun isBackCamera() : Boolean{
        return cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_camera, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CameraViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewFinder = view_finder

        viewFinder.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceDestroyed(holder: SurfaceHolder) = Unit
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int) = Unit

            override fun surfaceCreated(holder: SurfaceHolder) {

                // Selects appropriate preview size and configures view finder
                val previewSize = getPreviewOutputSize(
                    viewFinder.display, characteristics, SurfaceHolder::class.java)
                Log.d(TAG, "View finder size: ${viewFinder.width} x ${viewFinder.height}")
                Log.d(TAG, "Selected preview size: $previewSize")
                viewFinder.setAspectRatio(previewSize.width, previewSize.height)

                // To ensure that size is set, initialize camera in the view's thread
                viewFinder.post { initializeCamera() }
            }
        })
    }

    fun getRotationDegreesForPortrait() : Int {
        val cameraOrientation: Int = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0

        return cameraOrientation
    }

    private fun createRecorder(surface: Surface) = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOrientationHint(getRotationDegreesForPortrait())
        setVideoSource(MediaRecorder.VideoSource.SURFACE)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setOutputFile(outputFile.absolutePath)
        setVideoEncodingBitRate(RECORDER_VIDEO_BITRATE)
        setVideoSize(width, height)
        setVideoFrameRate(fps)
        setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setInputSurface(surface)
    }

    private fun initializeRequests() {
        previewRequest = session.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
            // Add the preview surface target
            addTarget(viewFinder.holder.surface)
        }.build()

        recordRequest = session.device.createCaptureRequest(CameraDevice.TEMPLATE_RECORD).apply {
            // Add the preview and recording surface targets
            addTarget(viewFinder.holder.surface)
            addTarget(recorderSurface)
        }.build()
    }

    private fun initializeCamera() = lifecycleScope.launch(Dispatchers.Main) {

        // Open the selected camera (back camera in this specific case)
        camera = openCamera(cameraManager, cameraId, cameraHandler)

        //Preview
        val targets = listOf(viewFinder.holder.surface, recorderSurface)
        session = createCaptureSession(camera, targets, cameraHandler)
        session.setRepeatingRequest(previewRequest, null, cameraHandler)

        recording_button.setOnClickListener { view ->

            if (recording) {

                recording = false
                recording_button.background = getDrawable(requireContext(), R.drawable.record_red)

                lifecycleScope.launch(Dispatchers.IO) {

                    val elapsedTimeMillis = System.currentTimeMillis() - recordingStartMillis
                    if (elapsedTimeMillis < MIN_REQUIRED_RECORDING_TIME_MILLIS) {
                        delay(MIN_REQUIRED_RECORDING_TIME_MILLIS - elapsedTimeMillis)
                    }

                    Log.d(TAG, "Recording stopped. Output file: $outputFile")
                    recorder.stop()

                    MediaScannerConnection.scanFile(
                        view.context, arrayOf(outputFile.absolutePath), null, null)

                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            viewModel.saveVideo(outputFile).observe(viewLifecycleOwner, androidx.lifecycle.Observer {result ->
                                when (result.status) {
                                    Status.SUCCESS -> Toast.makeText(requireContext(), getString(R.string.video_saved_successfully), Toast.LENGTH_SHORT).show()
                                    Status.ERROR -> {
                                        context?.toast(getString(R.string.video_error))
                                    }
                                }
                            })
                        }
                    }
                }
            }
            else
            {
                recordingStarted = true;
                recording = true
                recording_button.background = getDrawable(requireContext(), R.drawable.stop_red)

                lifecycleScope.launch(Dispatchers.IO) {

                    // Prevents screen rotation during the video recording
                    requireActivity().requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_LOCKED

                    // Start recording repeating requests, which will stop the ongoing preview
                    //  repeating requests without having to explicitly call `session.stopRepeating`
                    session.setRepeatingRequest(recordRequest, null, cameraHandler)

                    // Finalizes recorder setup and starts recording
                    recorder.apply {
                        // Sets output orientation based on current sensor value at start time
                        prepare()
                        start()
                    }
                    recordingStartMillis = System.currentTimeMillis()
                    Log.d(TAG, "Recording started")
                }
            }

            true
        }
    }

    //Permissions are requested outside this fragment (in VideoListFragment)
    @SuppressLint("MissingPermission")
    private suspend fun openCamera(
        manager: CameraManager,
        cameraId: String,
        handler: Handler? = null
    ): CameraDevice = suspendCancellableCoroutine { cont ->
        manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(device: CameraDevice) = cont.resume(device)

            override fun onDisconnected(device: CameraDevice) {
                Log.w(TAG, "Camera $cameraId has been disconnected")
                requireActivity().finish()
            }

            override fun onError(device: CameraDevice, error: Int) {
                val msg = when(error) {
                    ERROR_CAMERA_DEVICE -> "Fatal (device)"
                    ERROR_CAMERA_DISABLED -> "Device policy"
                    ERROR_CAMERA_IN_USE -> "Camera in use"
                    ERROR_CAMERA_SERVICE -> "Fatal (service)"
                    ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                    else -> "Unknown"
                }
                val exc = RuntimeException("Camera $cameraId error: ($error) $msg")
                Log.e(TAG, exc.message, exc)
                if (cont.isActive) cont.resumeWithException(exc)
            }
        }, handler)
    }

    private suspend fun createCaptureSession(
        device: CameraDevice,
        targets: List<Surface>,
        handler: Handler? = null
    ): CameraCaptureSession = suspendCoroutine { cont ->

        // Creates a capture session using the predefined targets, and defines a session state
        // callback which resumes the coroutine once the session is configured
        device.createCaptureSession(targets, object: CameraCaptureSession.StateCallback() {

            override fun onConfigured(session: CameraCaptureSession) = cont.resume(session)

            override fun onConfigureFailed(session: CameraCaptureSession) {
                val exc = RuntimeException("Camera ${device.id} session configuration failed")
                Log.e(TAG, exc.message, exc)
                cont.resumeWithException(exc)
            }
        }, handler)
    }

    override fun onStop() {
        super.onStop()
        try {
            camera.close()
        } catch (exc: Throwable) {
            Log.e(TAG, "Error closing camera", exc)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraThread.quitSafely()
        recorder.release()
        recorderSurface.release()
    }

    companion object {
        private val TAG = CameraFragment::class.java.simpleName

        private const val RECORDER_VIDEO_BITRATE: Int = 10_000_000
        private const val MIN_REQUIRED_RECORDING_TIME_MILLIS: Long = 1000L
    }

}