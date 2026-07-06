package com.enterprise.paging3.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enterprise.paging3.model.Post
import com.enterprise.paging3.paging.PostPagingSource
import com.enterprise.paging3.remotedatasource.retrofit.PostApi
import kotlinx.coroutines.flow.Flow


class PostRepository(
    private val api: PostApi
) {

    fun getPosts(): Flow<PagingData<Post>> {

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PostPagingSource(api)
            }
        ).flow
    }
}