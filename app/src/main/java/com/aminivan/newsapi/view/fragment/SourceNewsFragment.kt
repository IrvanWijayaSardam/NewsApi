package com.aminivan.newsapi.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminivan.newsapi.R
import com.aminivan.newsapi.databinding.FragmentSourceNewsBinding
import com.aminivan.newsapi.model.ArticlesItemPaging
import com.aminivan.newsapi.model.SourcesItem
import com.aminivan.newsapi.view.adapter.SourceAdapter
import com.aminivan.newsapi.view.adapter.TopHeadlineAdapterPaging
import com.aminivan.newsapi.viewmodel.SourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SourceNewsFragment"

@AndroidEntryPoint
class SourceNewsFragment : Fragment() {

    lateinit var binding : FragmentSourceNewsBinding
    private val vmSource : SourceViewModel by viewModels()
    private var name: String = ""


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
        showDataSources(name)


        binding.btnSearchSource.setOnClickListener{
            getSearchSource(name)
        }
    }


    fun showDataSources(category : String){
        vmSource.callApiSource(category,requireContext())
        vmSource.getDataSource().observe(viewLifecycleOwner){
            if (it != null){
                showSource(it)
            }
        }
    }

    fun showSource(data : List<SourcesItem>){
        val adapter = SourceAdapter(data)
        binding.rvSource.adapter = adapter
        binding.rvSource.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.onClick = {
            val sourc = it.id
            val bund = Bundle()
            bund.putString("name", sourc)
            Navigation.findNavController(requireView()).navigate(R.id.action_sourceNewsFragment_to_articleFragment,bund)
        }
    }

    fun getSearchSource(category : String){
        if (binding.searchSource.text.toString().isNotEmpty()){
            vmSource.callApiSource(category, requireContext())
            vmSource.getDataSource().observe(viewLifecycleOwner){
                if (it != null){
                    showSource(it)
                }
            }
        }
    }


}