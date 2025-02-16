package com.example.fullhealthcareapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.data.factory.DiscoverServiceViewModelFactory
import com.example.fullhealthcareapplication.data.viewmodel.DeleteContentViewModel
import com.example.fullhealthcareapplication.data.viewmodel.EditContentViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllContentCategoriesViewModel
import com.example.fullhealthcareapplication.ui.components.BlogTitle
import com.example.fullhealthcareapplication.ui.components.EditContentDialog

@Composable
fun BlogScreen(
    id: Int = 1,
    title: String = "",
    summary: String = "",
    description: String = "",
    contentCategoryId: Int = 1,
    toDiscoverScreen: () -> Unit,
    viewModelFactory: DiscoverServiceViewModelFactory,
    role: String = ""
){
    val contentCategoryIdState = remember { mutableIntStateOf(0) }
    val titleState = remember { mutableStateOf("") }
    val summaryState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val pictureState = remember { mutableStateOf("") }
    val editContentViewModel: EditContentViewModel = viewModel(factory = viewModelFactory)
    val deleteContentViewModel: DeleteContentViewModel = viewModel(factory = viewModelFactory)
    val getAllContentCategoriesViewModel: GetAllContentCategoriesViewModel = viewModel(factory = viewModelFactory)
    var showEditContentModal = remember { mutableStateOf(false) }
    val categoriesState = getAllContentCategoriesViewModel.state


    LaunchedEffect(Unit) { // TODO - Make it such that when the edit is successful, show immediately
        getAllContentCategoriesViewModel.getAllContentCategories()
        contentCategoryIdState.intValue = contentCategoryId
        titleState.value = title
        summaryState.value = summary
        descriptionState.value = description
    }
    if (categoriesState.loadingState) {
        Text("Loading content...")
    } else if (categoriesState.errorState) {
        Text("Error: ${categoriesState.errorMessage}")
    } else {
        val categories = listOf(categoriesState.categoriesList)
        val category = categories[0].toString()
        val names = extractNamesFromResponse(category)
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { toDiscoverScreen() },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
            Image(
                painter = painterResource(R.drawable.banner2),
                contentDescription = "Blog Image",
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clip(shape = RoundedCornerShape(32.dp))
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.3f),
                contentScale = ContentScale.FillWidth
            )
            if (role == "Admin") {
                Row(
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, bottom = 24.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilledTonalButton(
                        onClick = {
                            showEditContentModal.value = true
                        },
                        modifier = Modifier
                            .width(150.dp)
                    ) {
                        Text("Edit")
                    }
                    FilledTonalButton(
                        onClick = {
                            deleteContentViewModel.deleteContent(id)
                            toDiscoverScreen()
                        },
                        modifier = Modifier
                            .width(150.dp)
                    ) {
                        Text("Delete")
                    }
                }
            }
            BlogTitle(title)
            if(names.isNotEmpty()){
                Text(
                    text = "Category: ${names[contentCategoryId-1]}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = summary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            )
            Text(
                // Need to make this entire thing scrollable
                text = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            )
            if (showEditContentModal.value) {
                EditContentDialog(
                    onDismiss = { showEditContentModal.value = false },
                    onEditContent = { id, categoryId, title, summary, description, picture ->
                        editContentViewModel.editContent(
                            id,
                            categoryId,
                            title,
                            summary,
                            description,
                            picture
                        )
                        showEditContentModal.value = false
                    },
                    id = id,
                    contentCategoryId = contentCategoryIdState.intValue,
                    onContentCategoryIdChange = { contentCategoryIdState.intValue = it },
                    title = titleState.value,
                    onTitleChange = { titleState.value = it },
                    summary = summaryState.value,
                    onSummaryChange = { summaryState.value = it },
                    description = descriptionState.value,
                    onDescriptionChange = { descriptionState.value = it },
                    picture = pictureState.value,
                    onPictureChange = { pictureState.value = it }
                )
            }
        }
    }
}

@Composable
fun extractNamesFromResponse(response: String): List<String> {
    val regex = Regex("name=([^),]+)")
    return regex.findAll(response).map { it.groupValues[1] }.toList()
}