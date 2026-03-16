package com.example.lab3
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab3.R.drawable
import com.example.lab3.R.drawable.*
import com.example.lab3.ui.theme.Lab3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab3Theme {
                DiceWithButtonAndImage(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
            }
        }
    }
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage()
}
@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(1) }

    val context = LocalContext.current

    val imageResource = when(result) {
        1 -> dice_1
        2 -> dice_2
        3 -> dice_3
        4 -> dice_4
        5 -> dice_5
        else -> dice_6
    }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = result.toString()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val newResult = (1..6).random()
            result = newResult

            // 2. Hiển thị thông báo Toast
            Toast.makeText(
                context,
                "Bạn đã tung được số $newResult!",
                Toast.LENGTH_SHORT
            ).show()
        }) {
            Text(stringResource(R.string.roll))
        }
    }
}