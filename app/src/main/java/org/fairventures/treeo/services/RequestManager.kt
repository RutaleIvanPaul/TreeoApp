package org.fairventures.treeo.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.fairventures.treeo.models.NewRegisteredUser
import org.fairventures.treeo.models.RegisterUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RequestManager {
    private var disposable: Disposable? = null

    private val apiServe by lazy {
        ApiService.create()
    }

    fun createUser(
        registerUser: RegisterUser
    ): MutableLiveData<NewRegisteredUser> {
        val items = MutableLiveData<NewRegisteredUser>()
        disposable = apiServe.createUser(
            registerUser
        )
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