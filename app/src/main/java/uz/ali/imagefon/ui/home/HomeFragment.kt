package uz.ali.imagefon.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter
import github.chenupt.multiplemodel.viewpager.PagerModelManager
import uz.ali.imagefon.R

class HomeFragment : Fragment() {

    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var listTitle:ArrayList<String>
    lateinit var list:ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.pager_main)
        tabLayout = view.findViewById(R.id.tab_layout)

        listTitle= arrayListOf("animal","sport","nature","new","technology")
        list= arrayListOf("animal","sport","nature","new","technology")

        val manager = PagerModelManager()
        manager.addCommonFragment(MainFragment::class.java,list, listTitle)
        val adapter = ModelPagerAdapter(fragmentManager, manager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }
}