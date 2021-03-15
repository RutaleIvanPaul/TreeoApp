package org.fairventures.treeo.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.fairventures.treeo.models.*
import org.fairventures.treeo.util.errors

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
                    errors.postValue(error.message)
                }
            )
        return items
    }

    fun googleSignUP(
            googleAuthToken: String
    ): MutableLiveData<GoogleUser> {
        val items = MutableLiveData<GoogleUser>()
        disposable = apiServe.googleSignUp(
                GoogleToken(googleAuthToken,"mobile")
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            items.postValue(result)
                        },
                        { error ->
                            Log.d("API ERROR", "API Fetch Error: ${error.message} ")
                            errors.postValue(error.message)
                        }
                )
        return items
    }

    fun facebookSignUp(access_token: String)
            :MutableLiveData<FacebookUser>{
        val items = MutableLiveData<FacebookUser>()
        disposable = apiServe.facebookSignUp(
            access_token
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    items.postValue(result)
                    Log.d("Facebook API", result.toString())
                },
                { error ->
                    Log.d("API ERROR", "API Fetch Error: ${error.message} ")
                    errors.postValue(error.message)
                }
            )
        return items
    }

    fun login(loginDetails: LoginDetails):
    MutableLiveData<LoginToken>{
        val items = MutableLiveData<LoginToken>()
        disposable = apiServe.login(
            loginDetails
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    items.postValue(result)
                },
                { error ->
                    Log.d("API ERROR", "API Fetch Error: ${error.message} ")
                    errors.postValue(error.message)
                }
            )
        return items
    }

    fun logout(token: String):
            MutableLiveData<LogoutResponse>{
        val items = MutableLiveData<LogoutResponse>()
        disposable = apiServe.logOut(
            token
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    items.postValue(result)
                },
                { error ->
                    Log.d("API ERROR", "API Fetch Error: ${error.message} ")
                    errors.postValue(error.message)
                }
            )
        return items
    }

}