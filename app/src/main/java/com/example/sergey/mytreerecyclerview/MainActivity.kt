package com.example.sergey.mytreerecyclerview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.sergey.mytreerecyclerview.model.Item
import com.example.sergey.mytreerecyclerview.widget.ItemAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = ItemAdapter()
    private var data = getData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(listView) {
            adapter = this@MainActivity.adapter.apply {
                onClick = ::itemClick
                data = this@MainActivity.data
            }
            layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    private fun itemClick(item: Item) {
        item.opened = !item.opened

        val clickedIndex = data.indexOf(item)

        if (item.opened) {
            item.subItems.forEach { it.depth = item.depth + 1 }
            data.addAll(clickedIndex + 1, item.subItems)
        } else {
            var nextInDepthIndex = -1
            for (i in clickedIndex + 1..data.lastIndex) {
                if (data[i].depth <= item.depth) {
                    nextInDepthIndex = i
                    break
                }
                data[i].opened = false
            }

            data = data.filterIndexed { index, _ ->
                index <= clickedIndex || index >= nextInDepthIndex && nextInDepthIndex > -1
            }.toMutableList()
        }

        adapter.data = data
    }

    private fun getData() = mutableListOf(
            Item(1, "Item 1", listOf(
                    Item(2, "Item 1, SubItem 1"),
                    Item(3, "Item 1, SubItem 2"),
                    Item(4, "Item 1, SubItem 3"),
                    Item(5, "Item 1, SubItem 4"),
                    Item(6, "Item 1, SubItem 5")
            )),
            Item(7, "Item 2", listOf(
                    Item(8, "Item 2, SubItem 1", listOf(
                            Item(19, "Item 2, SubItem 1, SubItem 1")
                    )),
                    Item(9, "Item 2, SubItem 2"),
                    Item(10, "Item 2, SubItem 3"),
                    Item(11, "Item 2, SubItem 4"),
                    Item(12, "Item 2, SubItem 5")
            )),
            Item(13, "Item 3", listOf(
                    Item(14, "Item 3, SubItem 1"),
                    Item(15, "Item 3, SubItem 2"),
                    Item(16, "Item 3, SubItem 3"),
                    Item(17, "Item 3, SubItem 4"),
                    Item(18, "Item 3, SubItem 5")
            ))
    )
}
