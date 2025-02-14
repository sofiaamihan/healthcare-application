package com.example.fullhealthcareapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.ui.components.Background
import com.example.fullhealthcareapplication.ui.components.WhiteText


@Composable
fun WelcomeScreen(
    toLogin: () -> Unit,
    toSignUp: () -> Unit
){
    Background()
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo"
        )
        WhiteText(stringResource(R.string.welcome_capital))
        WelcomeButton(
            toLogin = {toLogin()},
            toSignUp = {toSignUp()}
        )
    }
}

@Composable
fun WelcomeButton(
    toLogin: () -> Unit,
    toSignUp: () -> Unit
){
    var selectedIndex = remember { mutableIntStateOf(0) }
    val options = listOf(stringResource(R.string.log_in), stringResource(R.string.sign_up))

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth(0.6f)
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    selectedIndex.intValue = index
                    if (index == 0) {
                        toLogin()
                    } else {
                        toSignUp()
                    }
                          },
                selected = index == selectedIndex.intValue,
                label = { Text(label) }
            )
        }
    }
}