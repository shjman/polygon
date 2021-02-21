package com.shjman.polygon.ui.camera

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shjman.polygon.R
import com.shjman.polygon.databinding.FragmentCameraBinding
import com.shjman.polygon.ui.home.refueling.RefuelingFragment.Companion.TAKEN_PHOTO_URI_BUNDLE_KEY
import com.shjman.polygon.ui.home.refueling.RefuelingFragment.Companion.TAKE_PHOTO_REQUEST_KEY
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val binding by viewBinding(FragmentCameraBinding::bind)
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.takePhoto.clicks()
            .onEach {
                val takenPhotoUri = takePhoto()
                setFragmentResult(TAKE_PHOTO_REQUEST_KEY, bundleOf(TAKEN_PHOTO_URI_BUNDLE_KEY to takenPhotoUri?.path))
                findNavController().popBackStack()
            }
            .catch { Timber.e(it, "binding.takePhoto.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val processCameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .apply { setSurfaceProvider(binding.viewFinder.surfaceProvider) }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                processCameraProvider.unbindAll()
                processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Timber.e(e, "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    // private suspend fun takePhoto(): ImageProxy? {
    private suspend fun takePhoto(): Uri? {
        // return suspendCoroutine {
        //     imageCapture.takePicture(
        //         ContextCompat.getMainExecutor(requireContext()),
        //         object : ImageCapture.OnImageCapturedCallback() {
        //             override fun onCaptureSuccess(imageProxy: ImageProxy) {
        //                 it.resume(imageProxy)
        //             }
        //
        //             override fun onError(exception: ImageCaptureException) {
        //                 super.onError(exception)
        //                 it.resume(null)
        //             }
        //         }
        //     )
        // }
        val imageCapture = imageCapture ?: return null
        val photoFile = File(outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + PREFIX_JPG)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        return suspendCoroutine {
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        Toast.makeText(requireContext(), "Photo capture succeeded: $savedUri", Toast.LENGTH_SHORT).show()
                        Timber.d("Uri.fromFile(photoFile) == $savedUri")
                        it.resume(savedUri)
                    }

                    override fun onError(e: ImageCaptureException) {
                        Timber.e(e, "Photo capture failed: ${e.message}")
                        it.resume(null)
                    }
                }
            )
        }
        /*imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(requireContext()),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = Uri.fromFile(photoFile)
                            val msg = "Photo capture succeeded: $savedUri"
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                            Timber.d("Uri.fromFile(photoFile) == $msg . output.savedUri == ${output.savedUri}")
                        }

                        override fun onError(e: ImageCaptureException) {
                            Timber.e(e, "Photo capture failed: ${e.message}")
                        }
                    }
                )*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PREFIX_JPG = ".jpg"
    }
}