package com.example.lab2
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Bao quanh bằng Surface để hỗ trợ theme và nền
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                BusinessCardApp()
            }
        }
    }
}

@Composable
fun BusinessCardApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2E8D4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFF073042))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.android_logo),
                    contentDescription = "Android Logo",
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(
                text = "Hai Nguyen",
                fontSize = 40.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Android Developer",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF006D3B)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.padding(bottom = 50.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContactRow(icon = Icons.Default.Phone, text = "+84 333333333")
            ContactRow(icon = Icons.Default.Share, text = "@HaiDev")
            ContactRow(icon = Icons.Default.Email, text = "hainh@gmail.com")
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.width(250.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF006D3B),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color(0xFF000000)
        )
    }
}