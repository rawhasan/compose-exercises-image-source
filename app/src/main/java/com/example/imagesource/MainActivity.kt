package com.example.imagesource

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

class MainActivity : ComponentActivity() {
    private var imageUriState = mutableStateOf<Uri?>(null)
    private var imageSetUrl: Uri? = null

    private val selectImageLauncher = registerForActivityResult(GetContent()) { uri ->
        imageSetUrl = imageUriState.value
        imageUriState.value = uri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageSourceActivityScreen()
        }
    }

    @Composable
    fun ImageSourceActivityScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberImagePainter(
                    if (imageUriState.value != null) {
                        imageUriState.value
                    } else {
                        if (imageSetUrl != null) {
                            imageSetUrl
                        } else {
                            R.drawable.blank_profile_picture
                        }
                    }
                ),
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { }) {
                    Text("Web")
                }
                Button(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = { selectImageLauncher.launch("image/*") }) {
                    Text("Gallery")
                }
                Button(onClick = { }) {
                    Text("Camera")
                }

            }

        }
    }
}