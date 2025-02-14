package com.example.fullhealthcareapplication.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.ui.components.Back
import com.example.fullhealthcareapplication.ui.components.OnboardingItems
import com.example.fullhealthcareapplication.ui.components.WhiteText
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
@Preview
fun OnboardingScreen(){
    val items = OnboardingItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(pageCount = 5)
    Back( // TODO - remove the back arrow and skip button because we need this data
        toBack = {},
        color = Color.White
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopSection(
            onSkipClick = {
                if (pageState.currentPage + 1 < items.size) scope.launch {
                    pageState.scrollToPage(items.size - 1)
                }
            }        )
        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth()
        ) { page ->
            OnboardingItem(items = items[page])
        }
        Box(
        ){
            Indicators(size = items.size, index = pageState.currentPage)
        }
        Button(
            onClick = {
                if (pageState.currentPage + 1 < items.size) scope.launch {
                    pageState.scrollToPage(pageState.currentPage + 1)
                }
            },
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(0.3f)
        ) {
            Text(text = "Next")
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TopSection(onSkipClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        TextButton(
            onClick = onSkipClick,
            modifier = Modifier.align(Alignment.CenterEnd),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = "Skip", color = MaterialTheme.colorScheme.onBackground)
        }
    }}

@Composable
fun BoxScope.Indicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.align(Alignment.CenterStart)
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7)
            )
    ) {
    }
}

@Composable
fun OnboardingItem(items: OnboardingItems){
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo"
        )
        WhiteText(items.title)

    }
}