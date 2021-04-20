package uz.ali.imagefon.ui.popular

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import uz.ali.imagefon.models.Photo
import uz.ali.imagefon.network.UnsplashClient
import uz.ali.imagefon.network.UnsplashInterface

class PopularViewModel : ViewModel() {
   private var count = 2


   private var unsplashInterface =
        UnsplashClient().getUnsplashClient()!!.create(UnsplashInterface::class.java)
   private var job: Job? = null

   private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }


    var getPhotoList = MutableLiveData<List<Photo>>()
    var photoLoadError = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()

   private var queryString = MutableLiveData<String>()
    fun refresh() {
        fetchPhotos()
    }

    fun fetchPhotos() {
        loading.value = true



        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
               var response =unsplashInterface.getPhotos(count,null,"latest")
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