package com.fairventures.treeo.repository

import com.fairventures.treeo.services.RequestManager
import javax.inject.Inject

class MainRepository @Inject constructor(
    val requestManager: RequestManager
) {
    fun getFarmersfromApi() = requestManager.returnPostsFromApi()
}