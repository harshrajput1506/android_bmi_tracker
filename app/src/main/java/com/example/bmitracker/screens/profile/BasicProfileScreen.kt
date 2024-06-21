package com.example.bmitracker.screens.profile

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bmitracker.R
import com.example.bmitracker.ui.theme.DMSansFamily
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicProfileScreen(
    goToHome:() -> Unit, onBack:() -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState = viewModel.state.value
    val datePickerState = rememberDatePickerState()
    val context = LocalContext.current
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
    val currentDate = Date()

    Box (
        modifier = Modifier
            .fillMaxSize()
            //.verticalScroll(rememberScrollState())
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
        ) {
            Text(
                text = "welcome,",
                fontFamily = DMSansFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = viewModel.user?.displayName?.lowercase() ?: "master",
                fontFamily = DMSansFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "select your born day",
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Card (
                    onClick = { showDatePicker = true },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = profileState.user.profile.dob ?: formatter.format(currentDate),
                        fontFamily = DMSansFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Row {
                    Column (
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "your weight",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = DMSansFamily
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = profileState.user.profile.weight,
                            onValueChange ={
                                if (it.isValidWeightOrHeight()) {
                                    viewModel.updateWeightInput(it)
                                } },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            suffix = {
                                Text(
                                    text = "kg",
                                    fontSize = 16.sp,
                                    fontFamily = DMSansFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = DMSansFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            ),
                            isError = profileState.isError && profileState.user.profile.weight.isBlank(),
                            supportingText = {
                                if (profileState.isError && profileState.user.profile.weight.isBlank()) {
                                    Text(
                                        text = "weight cannot be empty",
                                        color = MaterialTheme.colorScheme.error,
                                        fontFamily = DMSansFamily
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column (
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "your height",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = DMSansFamily
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = profileState.user.profile.height,
                            onValueChange ={
                                if (it.isValidWeightOrHeight()) {
                                    viewModel.updateHeightInput(it)
                                } },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            suffix = {
                                Text(
                                    text = "cm",
                                    fontSize = 16.sp,
                                    fontFamily = DMSansFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = DMSansFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            ),
                            isError = profileState.isError && profileState.user.profile.height.isBlank(),
                            supportingText = {
                                if (profileState.isError && profileState.user.profile.height.isBlank()) {
                                    Text(
                                        text = "height cannot be empty",
                                        color = MaterialTheme.colorScheme.error,
                                        fontFamily = DMSansFamily
                                    )
                                }
                            },
                            //visualTransformation = DecimalVisualTransformation()

                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Row {
                    val isMaleState = profileState.user.profile.gender == "male"
                    Card(
                        onClick = { viewModel.updateGender(true) },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if(isMaleState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(40.dp)

                    ) {
                        Column (
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.male),
                                contentDescription = "Male Icon",
                                colorFilter = ColorFilter.tint(if(isMaleState) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier.size(32.dp)
                            )

                            Text(
                                text = "male",
                                fontSize = 14.sp,
                                fontFamily = DMSansFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = if(isMaleState) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Card(
                        onClick = { viewModel.updateGender(false) },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if(!isMaleState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(40.dp)

                    ) {
                        Column (
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.female),
                                contentDescription = "Female Icon",
                                colorFilter = ColorFilter.tint(if(!isMaleState) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier.size(32.dp)
                            )

                            Text(
                                text = "female",
                                fontSize = 14.sp,
                                fontFamily = DMSansFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = if(!isMaleState) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Column (
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = { onBack() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(
                                text = "back",
                                modifier = Modifier.padding(18.dp),
                                fontFamily = DMSansFamily,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = { if(!profileState.isLoading) viewModel.onSave() },
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(18.dp)
                            ) {
                                if (profileState.isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(
                                        text = "save",
                                        fontFamily = DMSansFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                }

            }

            if(profileState.isUpdated){
                viewModel.setIsChangedFalse()
                goToHome()
            }

            if(profileState.isError && profileState.user.profile.dob.isNullOrEmpty()) {
                Toast.makeText(LocalContext.current, "Please select your born day", Toast.LENGTH_SHORT).show()
                viewModel.updateError(false)
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { /*TODO*/ },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val date = Calendar.getInstance().apply {
                                    timeInMillis = datePickerState.selectedDateMillis!!
                                }
                                if (date.before(Calendar.getInstance())) {
                                    val selectedDate = formatter.format(date.time)
                                    viewModel.updateSelectedDate(selectedDate)
                                    showDatePicker = false
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Selected date should be before today, please select again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                            }
                        ) { Text("Cancel") }
                    }
                )
                {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }
        }
    }
}

@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}

fun String.isValidWeightOrHeight(): Boolean {
    return this.isEmpty() || this.matches(Regex("^\\d*\\.?\\d{0,1}\$"))
}
