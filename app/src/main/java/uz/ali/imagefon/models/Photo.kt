package uz.ali.imagefon.models

data class Photo (
    var id: String,
    var width: Int,
    var height: Int,
   var description:String,
    var urls: Urls,
   var likes:Int

)