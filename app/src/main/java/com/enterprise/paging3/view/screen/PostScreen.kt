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
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.enterprise.paging3.R
import com.enterprise.paging3.remotedatasource.retrofit.RetrofitClient
import com.enterprise.paging3.remotedatasource.retrofit.exception.NoInternetConnectionException
import com.enterprise.paging3.repository.PostRepository
import com.enterprise.paging3.ui.theme.ListRowBorder
import com.enterprise.paging3.viewmodel.PostViewModel
import com.enterprise.paging3.viewmodel.PostViewModelFactory

@Composable
fun PostScreen(
) {

    val context = LocalContext.current

    val repository = retain {
        PostRepository(RetrofitClient.getRetrofitPostApi(context = context))
    }

    val viewModel: PostViewModel = viewModel(
        factory = PostViewModelFactory(repository = repository)
    )

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

                    var errorMessage: String = stringResource(R.string.general_error_message)

                    if(refreshState.error is NoInternetConnectionException){
                        errorMessage = refreshState.error.message?:errorMessage
                    }

                    Text(
                        text = errorMessage
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        onClick = { posts.retry() }
                    ) {
                        Text(text = stringResource(R.string.button_retry))
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


                                    var errorMessage: String = stringResource(R.string.error_message_failed_to_load_more)

                                    if(appendState.error is NoInternetConnectionException){
                                        errorMessage = appendState.error.message?:errorMessage
                                    }


                                    Text( text = errorMessage )

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                        onClick = { posts.retry() }
                                    ) {
                                        Text(text = stringResource(R.string.button_retry))
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