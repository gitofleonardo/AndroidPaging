package cn.huangchengxi.androidpaging

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.androidpaging.databinding.LayoutItemBinding

class ViewHolder(view:View,val binding:LayoutItemBinding):RecyclerView.ViewHolder(view) {

    companion object{
        fun create(parent:ViewGroup):ViewHolder{
            val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context))
            return ViewHolder(binding.root,binding)
        }
        fun bind(str:String,holder: ViewHolder){
            holder.binding.text.text = SpannableString(str)
        }
    }
}