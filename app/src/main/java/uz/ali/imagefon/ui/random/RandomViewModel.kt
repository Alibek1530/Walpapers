package uz.ali.imagefon.ui.random

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import uz.ali.imagefon.models.Photo
import uz.ali.imagefon.network.UnsplashClient
import uz.ali.imagefon.network.UnsplashInterface

class RandomViewModel : ViewModel() {
    var count = 2


    var unsplashInterface =
        UnsplashClient().getUnsplashClient()!!.create(UnsplashInterface::class.java)
    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }


    var getPhotoList = MutableLiveData<List<Photo>>()
    var photoLoadError = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()

    var queryString = MutableLiveData<String>()
    fun refresh() {
        fetchPhotos()
    }

    fun fetchPhotos() {
        loading.value = true



        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            //   var response =unsplashInterface.getPhotos(count,null,"latest")
         //   var response = unsplashInterface.searchPhotos(queryString.value, count, null, null)
            var response = unsplashInterface.getRandomPhotos(null,true,null,null,null,null,null,10)
            count++
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    getPhotoList.value = response.body()
                    photoLoadError.value = ""
                    loading.value = false

                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
//        photoLoadError.value = ""
//        loading.value = false
    }


    private fun onError(message: String) {
        photoLoadError.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}