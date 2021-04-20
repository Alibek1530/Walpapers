package uz.ali.imagefon.ui.image

import android.app.Activity
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import uz.ali.imagefon.R
import uz.ali.imagefon.adapter.EndlessRecyclerViewScrollListener
import uz.ali.imagefon.adapter.PhotosAdapter
import uz.ali.imagefon.adapter.PhotosAdapterSheet
import uz.ali.imagefon.models.Photo
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class ImageFragment : Fragment() {

    lateinit var imageView: ImageView
    lateinit var imageViewModel: ImageViewModel
    lateinit var progressBar: ProgressBar
    lateinit var wallpaperManager: WallpaperManager

    lateinit var conMenu: ConstraintLayout
    lateinit var zagruzka: ImageView
    lateinit var mobile: ImageView
    lateinit var like: ImageView

    lateinit var conJoylash: ConstraintLayout
    lateinit var blok: ImageView
    lateinit var home: ImageView
    lateinit var all: ImageView

    lateinit var photo: Photo

    lateinit var  adapter:PhotosAdapterSheet

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>
    private lateinit var BottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var recyclerView: RecyclerView
    private lateinit var frameLayout: LinearLayout
  //  var adapter: PhotosAdapter? = null
   var photoClickListener: PhotosAdapter.OnPhotoClickedListener?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_image, container, false)
        imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        imageView = root.findViewById(R.id.images)
        progressBar = root.findViewById(R.id.progressBarImage)

        conMenu = root.findViewById(R.id.con_menu)
        zagruzka = root.findViewById(R.id.zagruzka)
        mobile = root.findViewById(R.id.mobile)
        like = root.findViewById(R.id.like)


        conJoylash = root.findViewById(R.id.con_joylsh)
        blok = root.findViewById(R.id.blok)
        home = root.findViewById(R.id.home)
        all = root.findViewById(R.id.all)


        recyclerView = root.findViewById(R.id.recyclerViewSheet)
        frameLayout = root.findViewById(R.id.bottomSheet)


      //  bottomSheetBehavior = BottomSheetBehavior.from(BottomSheet)


        wallpaperManager = WallpaperManager.getInstance(context)

        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var id = requireArguments().getString("key")
        Log.d("ali", "photo 1 dona :  $id")
        imageViewModel.putId.value = id
        imageViewModel.refresh()

        imageViewModel.fetchPhotosSheet()



        BottomSheetBehavior.from(frameLayout).apply {
            this.peekHeight = 170
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        var layoutManager = GridLayoutManager(view.context, 1)
//        var recler=frameLayout.get(1) as RecyclerView
//        recler.setLayoutManager(layoutManager)
//        adapter =PhotosAdapterSheet(ArrayList<Photo>() )
//        recler.setAdapter(adapter)

        recyclerView.layoutManager=layoutManager
        adapter=PhotosAdapterSheet(ArrayList<Photo>() )
        recyclerView.adapter=adapter


//        bottomSheetBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                // handle onSlide
//             //   Toast.makeText(context, "STATE_Ali", Toast.LENGTH_SHORT).show()
//                Log.d("TAG", "onSlide: ali")
//            }
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_COLLAPSED -> Toast.makeText(context, "STATE_COLLAPSED", Toast.LENGTH_SHORT).show()
//              //      BottomSheetBehavior.STATE_EXPANDED ->     imageViewModel.fetchPhotosSheet()
//                    BottomSheetBehavior.STATE_DRAGGING -> Toast.makeText(context, "STATE_DRAGGING", Toast.LENGTH_SHORT).show()
//                    BottomSheetBehavior.STATE_SETTLING -> Toast.makeText(context, "STATE_SETTLING", Toast.LENGTH_SHORT).show()
//                    BottomSheetBehavior.STATE_HIDDEN -> Toast.makeText(context, "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
//                    else -> Toast.makeText(context, "OTHER_STATE", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })


//        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                Toast.makeText(view!!.context,"$page",Toast.LENGTH_SHORT).show()
//                Log.d("ttt", "onLoadMore: $page   $totalItemsCount")
//                imageViewModel.fetchPhotosSheet()
//            }
//        })

        imageViewModel.getPhotoAftora.observe(viewLifecycleOwner, Observer {


            adapter=PhotosAdapterSheet(it as ArrayList<Photo>)
            //recler.setAdapter(adapter)
            recyclerView.adapter=adapter

        })





        imageViewModel.getPhoto.observe(viewLifecycleOwner, Observer {
//            adapter!!.addPhotos(arrayListOf())

            photo = it
            Log.d("ali", "ali:  ${it.description}")
            Log.d("ali", "ali:  ${it.likes}")
            Log.d("ali", "ali:  ${it.urls.regular}")

            Picasso.get()
                .load(it.urls.regular)
                .into(imageView)


        })
        imageViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        imageViewModel.photoLoadError.observe(viewLifecycleOwner, Observer {

            Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
            Log.d("ali", "photoiuyt :  $it")
        })

        zagruzka.setOnClickListener {
//bu joyda zagruzka moladi

//            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            else
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        }

        mobile.setOnClickListener {
            conMenu.visibility = View.GONE
            conJoylash.visibility = View.VISIBLE
        }
        like.setOnClickListener {
//like bosiladi dataga saqlanadi
        }


        blok.setOnClickListener {
            var bitmap = imageView.drawable.toBitmap()
            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
            Snackbar.make(view, "Rasm blakirofkaga joylandi", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        home.setOnClickListener {
            var bitmap = imageView.drawable.toBitmap()
            Snackbar.make(view, "Rasm asosiy menuga joylandi", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
        }
        all.setOnClickListener {
            var bitmap = imageView.drawable.toBitmap()
            Snackbar.make(view, "Rasm asosiy va blakirofkaga joylandi", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            wallpaperManager.setBitmap(bitmap)
        }


    }

}