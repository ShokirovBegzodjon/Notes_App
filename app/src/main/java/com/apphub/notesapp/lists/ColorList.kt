package com.apphub.notesapp.lists

import com.apphub.notesapp.R
import com.apphub.notesapp.data.ColorData

object ColorList {

    private val colorList: MutableList<ColorData> = ArrayList()

    init {
        if (colorList.isEmpty()){
            initList()
        }
        else {

        }
    }
    fun getColors(color: Int): MutableList<ColorData> {
        colorList.forEach { colorData ->
            colorData.isCheck = false
        }
        colorList.remove(ColorData(color, true))
        colorList.remove(ColorData(color, false))
        colorList.add(ColorData(color, true))

        return colorList
    }


    private fun initList(){
        colorList.add(ColorData(R.color.user_icon_1,false))
        colorList.add(ColorData(R.color.user_icon_2,false))
        colorList.add(ColorData(R.color.user_icon_3,false))
        colorList.add(ColorData(R.color.user_icon_4,false))
        colorList.add(ColorData(R.color.user_icon_5,false))
        colorList.add(ColorData(R.color.user_icon_6,false))
        colorList.add(ColorData(R.color.user_icon_7,false))
    }

    fun getColor(index: Int): ColorData {
        return colorList[index]
    }
}