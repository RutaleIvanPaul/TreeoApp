package com.fairventures.treeo.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fairventures.treeo.models.FarmerFromApiItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RequestManager {
    private var disposable: Disposable? = null

    private val apiServe by lazy {
        ApiService.create()
    }

    fun returnPostsFromApi(): MutableLiveData<ArrayList<FarmerFromApiItem>> {
        val items = MutableLiveData<ArrayList<FarmerFromApiItem>>()
        disposable = apiServe.returnFarmers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    items.postValue(result)

                },
                { error ->
                    Log.d("API ERROR", "API Fetch Error: ${error.message} ")
                }
            )
        return items
    }
}