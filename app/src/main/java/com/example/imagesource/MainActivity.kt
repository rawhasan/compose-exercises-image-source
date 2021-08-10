package com.example.imagesource

import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
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
import coil.compose.rememberImagePainter

var imageUriState = mutableStateOf<Uri?>(null)
var imageSetUrl: Uri? = null

class MainActivity : ComponentActivity() {
    private val selectImageLauncher = registerForActivityResult(GetContent()) { uri ->
        imageSetUrl = imageUriState.value
        imageUriState.value = uri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageSourceActivityScreen(selectImageLauncher)
        }
    }
}

@Composable
fun ImageSourceActivityScreen(selectImageLauncher: ActivityResultLauncher<String>) {
    var isWebFormVisible by remember { mutableStateOf(false) }

    var webUrl by rememberSaveable { mutableStateOf("") }
    var urlText by rememberSaveable { mutableStateOf("") }

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
                        // TODO: Verify valid image URL + image exists + valid image
                        if (URLUtil.isValidUrl(urlText.trim())) {
                            imageSetUrl = urlText.toUri()
                            imageUriState.value = urlText.toUri()
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