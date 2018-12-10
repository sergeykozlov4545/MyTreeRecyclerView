package com.example.sergey.mytreerecyclerview.widget

import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.example.sergey.mytreerecyclerview.R
import com.example.sergey.mytreerecyclerview.model.Item
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*


typealias ItemClick = (item: Item) -> Unit

class ItemAdapter : RecyclerView.Adapter<ItemHolder>() {
    var onClick: ItemClick? = null
    var data: List<Item> = emptyList()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(ItemDiffUtilCallback(data, value))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item, parent, false)
            )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onClick = onClick
        holder.data = data[position]
    }
}

class ItemHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    var onClick: ItemClick? = null
    var data: Item? = null
        set(value) {
            configIcon(value)

            textView.text = value?.text ?: ""
            itemView.setOnClickListener { onClick?.invoke(value!!) }

            with(ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)) {
                val dm = itemView.context.resources.displayMetrics
                val leftMargin = TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f * value!!.depth, dm).toInt()
                setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
                itemView.layoutParams = this
            }
        }

    private fun configIcon(value: Item?) {
        getDrawable(value).takeIf { it != null }
                ?.let {
                    iconView.setImageDrawable(it)
                    iconView.visibility = View.VISIBLE
                } ?: run { iconView.visibility = View.INVISIBLE }
    }

    private fun getDrawable(value: Item?): Drawable? {
        if (value == null) return null
        val drawableId = when {
            value.subItems.isEmpty() -> R.drawable.ic_person_black_24dp
            value.opened -> R.drawable.ic_expand_more_black_24dp
            else -> R.drawable.ic_chevron_right_black_24dp
        }
        return ContextCompat.getDrawable(itemView.context, drawableId)
    }
}

class ItemDiffUtilCallback(
        private val oldData: List<Item>,
        private val newData: List<Item>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldData.size

    override fun getNewListSize() = newData.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int) =
            oldData[oldPosition].id == newData[newPosition].id

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = false
}