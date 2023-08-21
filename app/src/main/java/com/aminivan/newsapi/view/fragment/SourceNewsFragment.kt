package com.aminivan.newsapi.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.aminivan.newsapi.databinding.FragmentSourceNewsBinding
import com.aminivan.newsapi.view.adapter.TopHeadlineAdapterPaging
import com.aminivan.newsapi.viewmodel.SourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SourceNewsFragment"

@AndroidEntryPoint
class SourceNewsFragment : Fragment() {

    lateinit var binding : FragmentSourceNewsBinding
    private val vmSource : SourceViewModel by viewModels()
    private var name: String = ""
    
    @Inject
    lateinit var adapterTopHeadlinePagination : TopHeadlineAdapterPaging

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSourceNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = arguments?.getString("name")!!
        Toast.makeText(context, "News Category : $name", Toast.LENGTH_SHORT).show()

        if (name != null) {
            vmSource.category.value = name
        }

        binding.rvSource.adapter = adapterTopHeadlinePagination
        binding.rvSource.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            vmSource.topheadline.collectLatest {
                if(it != null){
                    adapterTopHeadlinePagination.submitData(PagingData.empty())
                    adapterTopHeadlinePagination.submitData(it)
                    Log.d(TAG, "onViewCreated: produkByCategory fetch $it")
                }
            }
        }

        binding.btnSearchSource.setOnClickListener{
            getSearchSource()
        }
    }

    fun getSearchSource(){
        if (binding.searchSource.text.toString().isNotEmpty()){
            vmSource.search.value = binding.searchSource.text.toString()
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