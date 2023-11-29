package com.apphub.notesapp.rvadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apphub.notesapp.data.ColorData
import com.apphub.notesapp.databinding.ColorItemBinding

class RvColorAdapter(private val colorList: MutableList<ColorData>):
    RecyclerView.Adapter<RvColorAdapter.ViewHolder>(){
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: ColorItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(color: ColorData){
            binding.colorView.setBackgroundColor(binding.root.context.getColor(color.color))
            if (color.isCheck){
                binding.isCheck.visibility = View.VISIBLE
            }
            else{
                binding.isCheck.visibility = View.GONE
            }
            binding.root.setOnClickListener{
                if (adapterPosition != RecyclerView.NO_POSITION){
                    for (element in colorList){
                        element.isCheck = false
                    }
                    colorList[adapterPosition].isCheck = true
                    Log.d("ColorList","adapterPosition = $adapterPosition")
                    listener?.setonItemClickListener(colorList[adapterPosition].color)
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun setOnClickListener(listener:OnItemClickListener){
        this.listener = listener
    }
    interface OnItemClickListener{
        fun setonItemClickListener(color: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ColorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int = colorList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(colorList[position])
    }
}