package com.example.fullhealthcareapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullhealthcareapplication.ui.components.DiscoverButton
import com.example.fullhealthcareapplication.ui.components.NavigationDrawer
import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.data.viewmodel.AddContentViewModel
import com.example.fullhealthcareapplication.data.factory.DiscoverServiceViewModelFactory
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.viewmodel.GetAllContentViewModel
import com.example.fullhealthcareapplication.ui.components.AddContentDialog
import kotlinx.coroutines.flow.first

@Composable
fun DiscoverScreen(
    tokenDataStore: TokenDataStore,
    toProfile: () -> Unit,
    toHealthLogs: () -> Unit,
    toHealthReport: () -> Unit,
    toHome: () -> Unit,
    toDiscoverScreen: () -> Unit,
    viewModelFactory: DiscoverServiceViewModelFactory,
    toBlogScreen: (Int, String, String, String, Int, String) -> Unit,

){
    val getAllContentViewModel: GetAllContentViewModel = viewModel(factory = viewModelFactory)
    val state = getAllContentViewModel.state
    val categoryIds = listOf(0, 1, 2, 3, 4, 5, 6)

    var showModal = remember { mutableStateOf(false) }
    val contentCategoryId = remember { mutableIntStateOf(0) }
    val title = remember { mutableStateOf("") }
    val summary = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val picture = remember { mutableStateOf("") }

    val role = remember { mutableStateOf("") }

    val addContentViewModel: AddContentViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        getAllContentViewModel.getAllContent()
        role.value = tokenDataStore.getRole.first().toString()
    }

    NavigationDrawer(
        title = "Discover",
        toProfile = {toProfile()},
        toHealthLogs = {toHealthLogs()},
        toHealthReport = {toHealthReport()},
        toHome = {toHome()}
    ){ padding ->
        if (role.value == "Admin"){
            Box(
                modifier = Modifier
                    .padding(top = 200.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer),
            ){
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .height(50.dp)
                            .fillMaxWidth(0.9f),
                        onClick = {
                            title.value = ""
                            summary.value = ""
                            description.value = ""
                            showModal.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Add Content")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(top = 200.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .fillMaxWidth()
                    .height(20.dp) // -60
                    .background(color = MaterialTheme.colorScheme.secondaryContainer),
            ){
            }
        }
        Column (
            modifier = Modifier
                .padding(top = if (role.value == "Admin") 280.dp else 220.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (state.loadingState) {
                Column (
                    modifier = Modifier.padding(top = 100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()
                }
            } else if (state.errorState) {
                Column (
                    modifier = Modifier.padding(top = 100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text("Error: ${state.errorMessage}")
                }
            } else {
                val isDropDownExpanded = remember { mutableStateOf(false) }
                val roles = listOf("All", "Diet", "Exercise", "Medicine", "Mental Health", "Environment", "Disease")
                val itemPosition = remember { mutableIntStateOf(0) }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ){
                    Box{
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    isDropDownExpanded.value = true
                                }
                                .padding(start = 48.dp, end = 48.dp)
                        ) {

                            Text(
                                text = roles[itemPosition.intValue],
                                fontSize = 14.sp,
                                textAlign = TextAlign.Left,
                                lineHeight = 16.sp,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Arrow down")
                        }
                        DropdownMenu(
                            expanded = isDropDownExpanded.value,
                            onDismissRequest = {
                                isDropDownExpanded.value = false
                            },
                        ) {
                            roles.forEachIndexed { index, role ->
                                DropdownMenuItem(
                                    text = { Text(
                                        role,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Left,
                                        lineHeight = 16.sp,
                                        modifier = Modifier
                                            .padding(top = 10.dp, start = 10.dp)
                                    ) },
                                    onClick = {
                                        isDropDownExpanded.value = false
                                        itemPosition.intValue = index
                                        contentCategoryId.intValue = categoryIds[index]
                                    }
                                )
                            }
                        }
                    }
                }
                val filteredContentList = if (contentCategoryId.intValue == 0) {
                    state.contentList
                } else {
                    state.contentList.filter { it.contentCategoryId == contentCategoryId.intValue }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 125.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(filteredContentList.size) { index ->
                        val content = filteredContentList[index]
                        val bannerResId = when (content.contentCategoryId) {
                            1 -> R.drawable.diet1
                            2 -> R.drawable.exercise2
                            3 -> R.drawable.medicine3
                            4 -> R.drawable.mental_health4
                            5 -> R.drawable.environment5
                            6 -> R.drawable.disease6
                            else -> R.drawable.exercise2
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            DiscoverButton(
                                banner = bannerResId,
                                title = state.contentList[index].title,
                                summary = state.contentList[index].summary,
                                description = state.contentList[index].description,
                                contentCategoryId = state.contentList[index].contentCategoryId,
                                toBlog = {
                                    toBlogScreen(
                                        state.contentList[index].id,
                                        state.contentList[index].title,
                                        state.contentList[index].summary,
                                        state.contentList[index].description,
                                        state.contentList[index].contentCategoryId,
                                        role.value
                                    )
                                },
                                role = role.value
                            )
                        }
                    }
                }
            }

        }
        if (showModal.value) {
            AddContentDialog (
                onDismiss = { showModal.value = false },
                onAddContent = { categoryId, title, summary, description, picture ->
                    addContentViewModel.addContent(categoryId, title, summary, description, picture)
                    showModal.value = false
                },
                contentCategoryId = contentCategoryId.intValue,
                onContentCategoryIdChange = { contentCategoryId.intValue = it },
                title = title.value,
                onTitleChange = { title.value = it },
                summary = summary.value,
                onSummaryChange = { summary.value = it },
                description = description.value,
                onDescriptionChange = { description.value = it },
                picture = picture.value,
//                onPictureChange = { picture.value = it }
            )
        }
    }
}
