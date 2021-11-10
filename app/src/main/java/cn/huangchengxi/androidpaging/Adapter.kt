package cn.huangchengxi.androidpaging

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val mItems: MutableList<String>):RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ViewHolder.bind(mItems[position],holder)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun submitList(newList:List<String>){
        DEFAULT_DIFF_CALLBACK.setLists(mItems,newList)
        val result = DiffUtil.calculateDiff(DEFAULT_DIFF_CALLBACK,true)
        result.dispatchUpdatesTo(this)
        mItems.clear()
        mItems.addAll(newList)
    }

    companion object{
        private val DEFAULT_DIFF_CALLBACK = object : DiffUtil.Callback(){
            private lateinit var mOriginList:List<String>
            private lateinit var mNewList:List<String>

            fun setLists(origin:List<String>,new:List<String>){
                mOriginList = origin
                mNewList = new
            }

            override fun getOldListSize(): Int {
                return mOriginList.size
            }
            override fun getNewListSize(): Int {
                return mNewList.size
            }
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mOriginList[oldItemPosition] == mNewList[newItemPosition]
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mOriginList[oldItemPosition] == mNewList[newItemPosition]
            }
        }
    }
}