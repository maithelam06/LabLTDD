package com.example.lab8.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignInScreen(
    auth: FirebaseAuth,
    onSignInSuccess: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF4E7),
                        Color(0xFFFFFDF8)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeroHeader()
            Spacer(modifier = Modifier.height(28.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                color = Color.White.copy(alpha = 0.96f)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back ",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF161616),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "PIZZERIA!",
                        color = Color(0xFFD90429),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        label = { Text("Password") },
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Password"
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val cleanEmail = email.trim()
                            val cleanPassword = password.trim()

                            when {
                                cleanEmail.isEmpty() || cleanPassword.isEmpty() -> {
                                    Toast.makeText(
                                        context,
                                        "Vui long nhap day du email va mat khau",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> {
                                    Toast.makeText(
                                        context,
                                        "Email khong hop le",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                else -> {
                                    loading = true
                                    auth.signInWithEmailAndPassword(cleanEmail, cleanPassword)
                                        .addOnCompleteListener { task ->
                                            loading = false
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    "Dang nhap thanh cong",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                onSignInSuccess()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    task.exception?.localizedMessage
                                                        ?: "Dang nhap that bai",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !loading,
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD90429)
                        )
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = "Sign In",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Is it first for you?",
                        color = Color(0xFF444444)
                    )
                    TextButton(onClick = {
                        Toast.makeText(
                            context,
                            "Hay tao tai khoan trong Firebase Authentication truoc khi dang nhap",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                        Text(
                            text = "Sign Up now!",
                            color = Color(0xFFD90429),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "OR Sign In with",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF555555)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    RowSocialPlaceholder()
                }
            }
        }
    }
}

@Composable
private fun HeroHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF3D1F14),
                        Color(0xFFAE2012),
                        Color(0xFFFFB703)
                    )
                ),
                shape = RoundedCornerShape(36.dp)
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = "PIZZERIA",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Fresh pizza.\nFast delivery.",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RowSocialPlaceholder() {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialCircle(text = "G", background = Color(0xFFFFFFFF), contentColor = Color(0xFFDB4437))
        SocialCircle(text = "f", background = Color(0xFFFFFFFF), contentColor = Color(0xFF1877F2))
    }
}

@Composable
private fun SocialCircle(
    text: String,
    background: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .background(background, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp
        )
    }
}
