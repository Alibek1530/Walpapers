package uz.ali.imagefon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.ali.imagefon.R
import uz.ali.imagefon.models.Photo


class PhotosAdapterSheet(
    var Photos: ArrayList<Photo>
) : RecyclerView.Adapter<PhotosAdapterSheet.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var photo: Photo = Photos!![position]


        Picasso.get()
            .load(photo.urls.regular)
            .resize(300, 300)
            .centerCrop()
            .into(holder.imageView)
        holder.textLike.text = photo.likes.toString()
    }

    fun addPhotos(photos: List<Photo>) {
        val lastCount = itemCount
        Photos.addAll(photos)
        notifyItemRangeInserted(lastCount, photos.size)
    }

    override fun getItemCount(): Int {
        return Photos!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        var textLike: TextView

        init {
            imageView = view.findViewById(R.id.imgview)
            textLike = view.findViewById(R.id.text_like)
        }
    }



}