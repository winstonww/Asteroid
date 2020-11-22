package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.databinding.MainItemBinding
import com.udacity.asteroidradar.detail.DetailViewModel
import com.udacity.asteroidradar.domain.AsteroidMain


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this,
            MainViewModel.Factory(requireActivity().application))
            .get(MainViewModel::class.java)

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val adapter = AsteroidAdapter(OnClick {
            id -> viewModel.navigateToDetails(id)
        })

        viewModel.asteroidMains.observe(viewLifecycleOwner, Observer {
            Log.i("MainFragment", it.toString())
            adapter.submitList(it)
        })

        viewModel.navigateDetails.observe(viewLifecycleOwner, Observer { id ->
            id?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(id))
                viewModel.onNavigateToDetailsComplete()
            }
        })


        binding.asteroidRecycler.adapter = adapter
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

class AsteroidAdapter(val clickListener: OnClick) : ListAdapter<AsteroidMain, AsteroidAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: MainItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<AsteroidMain>() {
        override fun areItemsTheSame(oldItem: AsteroidMain, newItem: AsteroidMain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: AsteroidMain, newItem: AsteroidMain): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(MainItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: AsteroidAdapter.ViewHolder, position: Int) {
        holder.binding.asteroid = getItem(position)
        holder.binding.root.setOnClickListener {
            clickListener.onClick(getItem(position).id)
        }
    }
}

class OnClick(private val action: (id: Long) -> Unit) {
    fun onClick(id: Long) {
        action(id)
    }
}

