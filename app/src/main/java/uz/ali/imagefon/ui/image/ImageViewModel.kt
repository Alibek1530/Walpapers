package uz.ali.imagefon.ui.image

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import uz.ali.imagefon.models.Photo
import uz.ali.imagefon.network.UnsplashClient
import uz.ali.imagefon.network.UnsplashInterface

class ImageViewModel : ViewModel() {
   private var count = 2


   private var unsplashInterface =
        UnsplashClient().getUnsplashClient()!!.create(UnsplashInterface::class.java)
   private var job: Job? = null

   private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }



    var getPhoto = MutableLiveData<Photo>()
    var photoLoadError = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()
    var putId=MutableLiveData<String>()

    var getPhotoAftora=MutableLiveData<List<Photo>>()

   private var queryString = MutableLiveData<String>()
    fun refresh() {
        fetchPhotos()
    }

    fun fetchPhotos() {
        loading.value = true

        Log.d("ali", "fetchPhotos: ${putId.value}")

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
               var response =unsplashInterface.getPhoto(putId.value,512,1024)
            count++
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    getPhoto.value = response.body()
                    photoLoadError.value = ""
                    loading.value = false
                    Log.d("ali", "fetchPhotos bady: ${response.body()}")

                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun fetchPhotosSheet() {
        loading.value = true

        Log.d("ali", "sheet: ${putId.value}")

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response =unsplashInterface.getPhotosAftora("lukaszischke")
            count++
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    getPhotoAftora.value = response.body()
                    photoLoadError.value = ""
                    loading.value = false
                    Log.d("ali", "xatasiiiifdfi ${response.body()}")

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