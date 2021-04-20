package uz.ali.imagefon.ui.popular

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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.ali.imagefon.R
import uz.ali.imagefon.adapter.EndlessRecyclerViewScrollListener
import uz.ali.imagefon.adapter.PhotosAdapter
import uz.ali.imagefon.models.Photo
import java.io.Serializable

class PopularFragment : Fragment() {

    private lateinit var popularViewModel: PopularViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    var adapter: PhotosAdapter? = null
    var photoClickListener: PhotosAdapter.OnPhotoClickedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        popularViewModel = ViewModelProvider(this).get(PopularViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_popular, container, false)
        recyclerView = root.findViewById(R.id.recyclerViewPo)
        progressBar = root.findViewById(R.id.progressBarPo)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        popularViewModel.refresh()
        photoClickListener = object : PhotosAdapter.OnPhotoClickedListener {

            override fun photoClicked(photo: Photo, imageView: ImageView) {
                Toast.makeText(view.context,"tugadi",Toast.LENGTH_LONG).show()
                Log.d("ali", "put jonatti  $photo")
                var navController= Navigation.findNavController(activity!!,R.id.nav_host_fragment)
                var args=Bundle()
                args.putString("key",photo.id)
                navController.navigate(R.id.imageFragment,args)
            }
        }

        var layoutManager = GridLayoutManager(view.context, 3)
        recyclerView.setLayoutManager(layoutManager)
        adapter = PhotosAdapter(ArrayList<Photo>(),
            photoClickListener as PhotosAdapter.OnPhotoClickedListener
        )

        recyclerView.setAdapter(adapter)

        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Toast.makeText(view!!.context,"$page",Toast.LENGTH_SHORT).show()
                Log.d("ttt", "onLoadMore: $page   $totalItemsCount")
                popularViewModel.fetchPhotos()
            }
        })

        popularViewModel.getPhotoList.observe(viewLifecycleOwner, Observer {
            adapter!!.addPhotos(it)
        })

        popularViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        popularViewModel.photoLoadError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
        })
    }
}