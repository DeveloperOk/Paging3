package com.enterprise.paging3.view.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.enterprise.paging3.ui.theme.ListRowBorder
import com.enterprise.paging3.viewmodel.PostViewModel

@Composable
fun PostScreen(
    viewModel: PostViewModel = viewModel()
) {
    val posts = viewModel.posts.collectAsLazyPagingItems()

    when (val refreshState = posts.loadState.refresh) {

        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Green)
            }
        }

        is LoadState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = refreshState.error.localizedMessage
                            ?: "Something went wrong"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        onClick = { posts.retry() }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }

        is LoadState.NotLoading -> {

            if (posts.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No posts found.")
                }
            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(
                        count = posts.itemCount
                    ) { index ->

                        posts[index]?.let { post ->

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .fillMaxWidth()
                                    .border(width = 2.dp, color = ListRowBorder, shape = RoundedCornerShape(size = 15.dp))

                            ) {

                                Text(modifier = Modifier
                                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                    text = post.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(modifier = Modifier
                                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                                    text = post.body)
                            }

                        }
                    }

                    when (val appendState = posts.loadState.append) {

                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color.Green)
                                }
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        appendState.error.localizedMessage
                                            ?: "Failed to load more"
                                    )

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                        onClick = { posts.retry() }
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }

                        is LoadState.NotLoading -> {
                            // No footer
                        }
                    }
                }
            }
        }
    }
}