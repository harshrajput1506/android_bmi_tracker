package com.example.bmitracker.screens.onboard

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bmitracker.R
import com.example.bmitracker.ui.theme.DMSansFamily
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardScreen(
    navigateToProfile : () -> Unit,
    viewModel: OnBoardViewModel = hiltViewModel()
)
{
    val authState = viewModel.state.value
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.e("LoginScreen:Launcher","GoogleSignIn Success with id "+ account.id)
                account.idToken?.let {
                    viewModel.signInWithGoogle(it)
                }

            } catch (e : ApiException) {
                Log.e("LoginScreen:Launcher","GoogleSignIn Google $e")
            }
        }
        else if (it.resultCode == Activity.RESULT_CANCELED){
            Log.e("LoginScreen:Launcher","GoogleSignIn Canceled")
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Image(
            painter = painterResource(id = R.drawable.eating_disorder_cuate),
            contentDescription = "Health Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column (
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "know your numbers,",
                fontFamily = DMSansFamily,
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,

                )
            Text(
                text = "know your health",
                fontFamily = DMSansFamily,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
        }


        Column (
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ){
            Card(
                onClick = {
                    val signInIntent = viewModel.signInClient.signInIntent
                    launcher.launch(signInIntent)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),

                shape = RoundedCornerShape(20.dp)
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 12.dp)
                ) {
                    if(authState.isLoading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "get start with",
                            fontFamily = DMSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            modifier = Modifier.size(24.dp),
                            contentDescription = "Google image"
                        )
                    }
                }
            }
        }
    }
    if(authState.isLoggedIn){
        viewModel.updateIsLoggedIn(false)
        navigateToProfile()
    }
    if(authState.isError){
        Toast.makeText(LocalContext.current,"Something went wrong",Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnBoardScreen(){
    OnBoardScreen({

    })
}