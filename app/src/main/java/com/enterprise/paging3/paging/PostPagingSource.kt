package com.enterprise.paging3.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.enterprise.paging3.model.Post
import com.enterprise.paging3.remotedatasource.retrofit.PostApi

class PostPagingSource(
    private val api: PostApi
) : PagingSource<Int, Post>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Post> {

        return try {

            val page = params.key ?: 1

            val posts = api.getPosts(
                page = page,
                limit = params.loadSize
            )

            LoadResult.Page(
                data = posts,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (posts.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, Post>
    ): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}