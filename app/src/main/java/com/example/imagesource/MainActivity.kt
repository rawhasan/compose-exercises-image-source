package com.example.imagesource

import android.os.Bundle
import android.webkit.URLUtil
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

// Display an image selected by the user from gallery,
// or from a provided URL.
// TODO: Display image from the camera when the Camera API from Compose arrives

class MainActivity : ComponentActivity() {
    private val imageViewModel by viewModels<ImageViewModel>()

    private val selectImageLauncher = registerForActivityResult(GetContent()) { uri ->
        imageViewModel.setPreviousImageUri(imageViewModel.getCurrentlySelectedImageUri())
        imageViewModel.setCurrentlySelectedImageUri(uri)
    }

    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageSourceActivityScreen(selectImageLauncher, imageViewModel)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageSourceActivityScreen(
    selectImageLauncher: ActivityResultLauncher<String>,
    imageViewModel: ImageViewModel
) {
    var isWebFormVisible by remember { mutableStateOf(false) }
    var urlText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxHeight(0.4f)) {
            Image(
                painter = rememberImagePainter(
                    if (imageViewModel.getCurrentlySelectedImageUri() != null) { // use the currently selected image
                        imageViewModel.getCurrentlySelectedImageUri()
                    } else if (imageViewModel.getPreviousImageUri() != null) { // use the previously selected image
                        imageViewModel.getPreviousImageUri()
                    } else {
                        R.drawable.blank_profile_picture // use the placeholder image
                    }, builder = {
                        placeholder(R.drawable.blank_profile_picture)
                        error(R.drawable.blank_profile_picture_error) // FIXME: Set the previousImageUri
                    }),
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { isWebFormVisible = true }) {
                Text("Web")
            }
            Button(
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = {
                    selectImageLauncher.launch("image/*")
                    isWebFormVisible = false
                }) {
                Text("Gallery")
            }
            Button(onClick = { }) {
                Text("Camera")
            }

        }

        // form to enter web url of an image
        if (isWebFormVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    label = { Text("Image URL") },
                    modifier = Modifier.fillMaxWidth(.65f),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
                )

                Button(
                    onClick = {
                        isWebFormVisible = false

                        // FIXME: Verify image exists on provided URL
                        if (URLUtil.isValidUrl(urlText.trim())) {
                            imageViewModel.setPreviousImageUri(imageViewModel.getCurrentlySelectedImageUri())
                            imageViewModel.setCurrentlySelectedImageUri(urlText.toUri())
                            urlText = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done, contentDescription = "Save",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}