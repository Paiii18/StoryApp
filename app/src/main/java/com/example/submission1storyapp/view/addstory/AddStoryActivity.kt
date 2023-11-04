package com.example.submission1storyapp.view.addstory

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.submission1storyapp.R
import com.example.submission1storyapp.databinding.ActivityAddStoryBinding
import com.example.submission1storyapp.reduceFileImage
import com.example.submission1storyapp.uriToFile
import com.example.submission1storyapp.view.ViewModelFactory
import com.example.submission1storyapp.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Suppress("DEPRECATION")
class AddStoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null

    private lateinit var binding: ActivityAddStoryBinding

    private val cameraPermissionRequest = 100
    private val requestImageCapture = 1
    private var currentImageBitmap: Bitmap? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) {
            load(it)
        }

        viewModel.uploadSuccess.observe(this) { success ->
            if (success) {
                // Proses upload berhasil, pindahkan ke MainActivity
                Log.d("UploadSuccess", "Proses upload berhasil")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        initAction()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            currentImageBitmap = data?.extras?.get("data") as Bitmap

            binding.ViewImage.setImageBitmap(currentImageBitmap)
        }
    }

    private fun initAction() {

        binding.btnCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            val description = binding.addDeskripsi.text.toString()
            val requestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

            if (currentImageUri == null && currentImageBitmap == null) {
                Toast.makeText(this, "Silakan ambil foto terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
            } else if (description.isEmpty()) {
                Toast.makeText(this, "Silakan isi deskripsi cerita", Toast.LENGTH_SHORT).show()
            } else {
                val imageFile = currentImageUri?.let { uri ->
                    uriToFile(uri, this).reduceFileImage()
                } ?: currentImageBitmap?.let { bitmap ->
                    val imageFile = saveBitmapToFile(bitmap)
                    imageFile
                }

                        val imgPart = MultipartBody.Part.createFormData(
                            "photo", imageFile?.name ?: "default_filename", RequestBody.create(
                                "image/*".toMediaTypeOrNull(),
                                imageFile!!
                            )
                        )
                Log.i("AddStoryActivity", "$imgPart, $requestBody"  )

//                viewModel.getSession().observe(this) { setting ->
//                    if (setting.token.isNotEmpty()) {
//                        val imgPart = MultipartBody.Part.createFormData(
//                            "photo", imageFile?.name ?: "default_filename", RequestBody.create(
//                                "image/*".toMediaTypeOrNull(),
//                                imageFile!!
//                            )
//                        )
//                        viewModel.addStory(setting.token, imgPart, requestBody)
//                    }
//                    messageToast(getString(R.string.storry_added))
//                }
            }
        }

    }

    private fun messageToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val fileName = "dummy.jpg"
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, requestImageCapture)
        } else {
            Toast.makeText(this, "Aplikasi kamera tidak tersedia.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri.let {
            Log.d("Image URI", "showImage: $it")
            binding.ViewImage.setImageURI(it)
        }

    }

    private fun load(result: Boolean) {
        if (result) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}