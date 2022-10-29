package com.clevermustafa.todo_kotlin_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.StackView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.clevermustafa.todo_kotlin_app.ui.theme.Todo_kotlin_appTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    val isLoading = mutableStateOf(false)
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {

            Todo_kotlin_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    var email by remember {
                        mutableStateOf(TextFieldValue(""))
                    }
                    var password by remember {
                        mutableStateOf(TextFieldValue(""))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxHeight()
                            .fillMaxWidth()

                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween

                        ) {
                            if(isLoading.value) {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .padding(
                                        all = 20.dp
                                    ),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "Email")
                                CustomTextField("Enter Email", value = email, onValueChange = {
                                    email = it
                                })
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(text = "Password")
                                CustomTextField(labelText = "Enter password", value = password) {
                                    password = it
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Button(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        loginWithEmailAndPassword(email.text, password.text)
                                    }
                                ) {
                                    Text("Login")
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    // function to signin with email and password
    fun loginWithEmailAndPassword(email: String, password: String) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                isLoading.value = true
                auth.signInWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main){
                isLoading.value = false
                    val intent = Intent(applicationContext, HomePageActivity::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                isLoading.value = false
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Composable
fun CustomTextField(labelText: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
//    var email by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = {Text(text = labelText)}
    )
}


