package com.zhuinden.simplestackexamplescoping.features.words

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhuinden.simplestackexamplescoping.R
import com.zhuinden.simplestackexamplescoping.utils.observe
import com.zhuinden.simplestackexamplescoping.utils.onClick
import com.zhuinden.simplestackexamplescoping.utils.safe
import com.zhuinden.simplestackexamplescoping.utils.showToast
import com.zhuinden.simplestackextensions.fragments.KeyedFragment
import com.zhuinden.simplestackextensions.fragmentsktx.lookup
import kotlinx.android.synthetic.main.word_list_view.*

/**
 * Created by Zhuinden on 2018.09.17.
 */

class WordListFragment : KeyedFragment(R.layout.word_list_view) {
    interface ActionHandler {
        fun onAddNewWordClicked()
    }

    interface DataProvider {
        val wordList: LiveData<List<String>>
    }

    private val actionHandler by lazy { lookup<ActionHandler>() }
    private val dataProvider by lazy { lookup<DataProvider>() }
    private val wordList by lazy { dataProvider.wordList }

    private val controllerEvents by lazy { lookup<WordEventEmitter>() }

    val adapter = WordListAdapter()

    @Suppress("NAME_SHADOWING")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val x: RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        buttonGoToAddNewWord.onClick { view ->
            actionHandler.onAddNewWordClicked()
        }

        wordList.observe(viewLifecycleOwner) { words ->
            adapter.updateWords(words)
        }

        controllerEvents.observe(viewLifecycleOwner) { event ->
            when (event) {
                is WordController.Events.NewWordAdded -> showToast("Added ${event.word}")
            }.safe()
        }
    }
}