package uz.ali.imagefon.ui.random

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.ali.imagefon.R
import uz.ali.imagefon.adapter.EndlessRecyclerViewScrollListener
import uz.ali.imagefon.adapter.PhotosAdapter
import uz.ali.imagefon.models.Photo
import uz.ali.imagefon.ui.popular.PopularViewModel

class RandomFragment : Fragment() {

    private lateinit var randomViewModel: RandomViewModel
    private lateinit var recyclerView: RecyclerView

    private lateinit var progressBar: ProgressBar
    var adapter: PhotosAdapter? = null

    var photoClickListener: PhotosAdapter.OnPhotoClickedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        randomViewModel = ViewModelProvider(this).get(RandomViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_random, container, false)
        recyclerView = root.findViewById(R.id.recyclerViewRa)
        progressBar = root.findViewById(R.id.progressBarRa)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        randomViewModel.refresh()
        photoClickListener = object : PhotosAdapter.OnPhotoClickedListener {

            override fun photoClicked(photo: Photo, imageView: ImageView) {
                Toast.makeText(view.context,"tugadi", Toast.LENGTH_LONG).show()
            }
        }

        var layoutManager = GridLayoutManager(view.context, 1)
        recyclerView.setLayoutManager(layoutManager)
        adapter = PhotosAdapter(ArrayList<Photo>(),
            photoClickListener as PhotosAdapter.OnPhotoClickedListener
        )

        recyclerView.setAdapter(adapter)

        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Toast.makeText(view!!.context,"$page", Toast.LENGTH_SHORT).show()
                Log.d("ttt", "onLoadMore: $page   $totalItemsCount")
                randomViewModel.fetchPhotos()
            }
        })

        randomViewModel.getPhotoList.observe(viewLifecycleOwner, Observer {
            adapter!!.addPhotos(it)
        })

        randomViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        randomViewModel.photoLoadError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
        })
    }
}