package com.firstapp.studentplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.studentplanner.Adapters.FormDetalesAdapter
import com.firstapp.studentplanner.Classes.Form

class FormsFragment(list: ArrayList<Form>): Fragment() {

    private var listOfForms = ArrayList<Form>(list)
    //private var myAdapter = FormDetalesAdapter(listOfForms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_of_all_forms,container,false)
        var myAdapter = FormDetalesAdapter(listOfForms)
        myAdapter.notifyDataSetChanged()
        view.findViewById<RecyclerView>(R.id.recyclerviewListOfAllForms).adapter = myAdapter
        view.findViewById<RecyclerView>(R.id.recyclerviewListOfAllForms).layoutManager = LinearLayoutManager(requireContext())
        return view
    }



}