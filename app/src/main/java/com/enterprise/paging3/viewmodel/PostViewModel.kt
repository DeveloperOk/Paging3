package com.enterprise.paging3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enterprise.paging3.remotedatasource.retrofit.RetrofitClient
import com.enterprise.paging3.repository.PostRepository


class PostViewModel(private val repository: PostRepository) : ViewModel() {


    val posts = repository
        .getPosts()
        .cachedIn(viewModelScope)


}