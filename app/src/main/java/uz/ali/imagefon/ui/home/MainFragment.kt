package uz.ali.imagefon.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.ali.imagefon.R
import uz.ali.imagefon.adapter.EndlessRecyclerViewScrollListener
import uz.ali.imagefon.adapter.PhotosAdapter
import uz.ali.imagefon.models.Photo

class MainFragment : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var progressBar: ProgressBar
    var adapter: PhotosAdapter? = null
    var photoClickListener: PhotosAdapter.OnPhotoClickedListener? = null

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView=view.findViewById(R.id.recyclerViewFr)
        progressBar=view.findViewById(R.id.progressBarFr)


        var query:String = requireArguments().getString("data").toString()


        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.refresh()

        viewModel.queryString.value=query





        photoClickListener = object : PhotosAdapter.OnPhotoClickedListener {

            override fun photoClicked(photo: Photo, imageView: ImageView) {
                Toast.makeText(view.context,"tugadi",Toast.LENGTH_LONG).show()
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
                Toast.makeText(view!!.context,"$page",Toast.LENGTH_SHORT).show()
                Log.d("ttt", "onLoadMore: $page   $totalItemsCount")
                viewModel.fetchPhotos()
            }
        })

        viewModel.getPhotoList.observe(viewLifecycleOwner, Observer {
//            Toast.makeText(this,"$it",Toast.LENGTH_LONG).show()
         //   Log.d("ttt", "onCreate:  joojo  $it")
            //   setAdapterAdd(it as ArrayList<Photo>)
            adapter!!.addPhotos(it)
            //   recyclerView.setAdapter(adapter)
        })

        viewModel.photoLoadError.observe(viewLifecycleOwner, Observer {
//            Toast.makeText(this,"$it",Toast.LENGTH_LONG).show()
//            Log.d("ttt", "onCreate: $it")
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            Log.d("ttt", "onCreate999: $it")
            if (it){
                progressBar.visibility=View.VISIBLE
            }else{
                progressBar.visibility=View.GONE
            }
//            Toast.makeText(this,"$it",Toast.LENGTH_LONG).show()
//            Log.d("ttt", "onCreate: $it")
        })







    }


}