package com.aminivan.newsapi.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminivan.newsapi.R
import com.aminivan.newsapi.databinding.FragmentArticleBinding
import com.aminivan.newsapi.model.ArticlesItemPaging
import com.aminivan.newsapi.view.adapter.LoadMoreAdapter
import com.aminivan.newsapi.view.adapter.TopHeadlineAdapterPaging
import com.aminivan.newsapi.viewmodel.SourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ArticleFragment"

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    lateinit var binding : FragmentArticleBinding
    private val vmSource : SourceViewModel by viewModels()
    private var name: String = ""

    @Inject
    lateinit var adapterTopHeadlinePagination : TopHeadlineAdapterPaging
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = arguments?.getString("name")!!

        if (name != null) {
            vmSource.category.value = name
        }

        binding.rvArticle.adapter = adapterTopHeadlinePagination
        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvArticle.adapter = adapterTopHeadlinePagination.withLoadStateFooter(
            LoadMoreAdapter{
                adapterTopHeadlinePagination.retry()
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            vmSource.topheadline.collectLatest {
                if(it != null){
                    adapterTopHeadlinePagination.submitData(PagingData.empty())
                    adapterTopHeadlinePagination.submitData(it)
                    Log.d(TAG, "onViewCreated: produkByCategory fetch $it")
                }
            }
        }



        adapterTopHeadlinePagination.setOnItemClickListener(object : TopHeadlineAdapterPaging.OnItemClickListener{
            override fun onItemClick(item: ArticlesItemPaging) {
                val sourc = item.url
                val bund = Bundle()
                bund.putString("url", sourc)
                Navigation.findNavController(requireView()).navigate(R.id.action_articleFragment_to_detailArticleFragment,bund)
            }
        })
        
        binding.btnSearch.setOnClickListener {
            getSearchSource()
        }

    }

    fun getSearchSource(){
        if (binding.searchHere.text.toString().isNotEmpty()){
            vmSource.search.value = binding.searchHere.text.toString()
            if(vmSource.category.value == ""){
                vmSource.category.value = name
            } else {
                vmSource.category.value = ""
            }
        } else {
            vmSource.search.value = ""
            vmSource.category.value = name
        }
    }

}