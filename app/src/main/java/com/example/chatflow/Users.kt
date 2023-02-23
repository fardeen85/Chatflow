package com.example.chatflow

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatflow.databinding.FragmentUsersBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User


class Users : Fragment() {

    var usersbinding : FragmentUsersBinding? = null
    private val useradapter =  adapter()
    private var client = ChatClient.instance()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        usersbinding = FragmentUsersBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        queryall()
        setuprecycler()
        return usersbinding!!.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        val menulayout = inflater.inflate(R.menu.usersmenu,menu)
        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchview = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchview.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {

                queryusers(p0!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }


        })

        searchview.setOnCloseListener {

            queryall()
            false
        }

        }

    fun setuprecycler(){

        usersbinding!!.recyclerusers.layoutManager = LinearLayoutManager(requireContext())
        usersbinding!!.recyclerusers.adapter = useradapter
    }


    fun queryusers(query : String) : Boolean{

        if (query.isEmpty()){
            queryall()
        }
        else{ searchuser(query)}

        return  true
    }

    private fun searchuser(query: String) {

        val request = QueryUsersRequest(

                filter = Filters.and(Filters.autocomplete("id",query),
                Filters.ne("id",client.getCurrentUser()!!.id)),
                offset = 0,
                limit = 100

        )

        client.queryUsers(request).enqueue(){
            if (it.isSuccess){

                val users = it.data()
                useradapter.setdata(users)
            }
        }
    }

    private fun queryall() {

        val request = QueryUsersRequest(

                filter = Filters.ne("id",client.getCurrentUser()!!.id),
                offset = 0,
                limit = 100

        )

        client.queryUsers(request).enqueue(){
            if (it.isSuccess){

                val users : List<User> = it.data()
                useradapter.setdata(users)
                Log.d("TAg",users.toString())
            } else{
                Log.e("TAg",it.error().message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        usersbinding = null
    }
}







