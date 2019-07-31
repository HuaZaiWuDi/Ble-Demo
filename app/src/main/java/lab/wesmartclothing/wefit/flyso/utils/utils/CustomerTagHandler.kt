package com.wesmarclothing.kotlintools.kotlin.utils

import android.content.Context
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StrikethroughSpan
import android.util.Log
import android.util.TypedValue
import org.xml.sax.XMLReader
import java.util.*

/**
 * 自定义的一html标签解析
 *
 *
 * Created by Siy on 2016/11/19.
 */

internal class CustomerTagHandler(private val context: Context) : Html.TagHandler {

    /**
     * html 标签的开始下标
     */
    private var startIndex: Stack<Int>? = null

    /**
     * html的标签的属性值 value，如:<size value='16'></size>
     * 注：value的值不能带有单位,默认就是sp
     */
    private var propertyValue: Stack<String>? = null

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        Log.e("TAG", "handleTag:$tag")
        if (opening) {
            handlerStartTAG(tag, output, xmlReader)
        } else {
            handlerEndTAG(tag, output)
        }
    }

    /**
     * 处理开始的标签位
     *
     * @param tag
     * @param output
     * @param xmlReader
     */
    private fun handlerStartTAG(tag: String, output: Editable, xmlReader: XMLReader) {
        if (tag.equals("del", ignoreCase = true)) {
            handlerStartDEL(output)
        } else if (tag.equals("size", ignoreCase = true)) {
            handlerStartSIZE(output, xmlReader)
        }
    }

    /**
     * 处理结尾的标签位
     *
     * @param tag
     * @param output
     */
    private fun handlerEndTAG(tag: String, output: Editable) {
        if (tag.equals("del", ignoreCase = true)) {
            handlerEndDEL(output)
        } else if (tag.equals("size", ignoreCase = true)) {
            handlerEndSIZE(output)
        }
    }

    private fun handlerStartDEL(output: Editable) {
        if (startIndex == null) {
            startIndex = Stack()
        }
        startIndex!!.push(output.length)
    }

    private fun handlerEndDEL(output: Editable) {
        output.setSpan(StrikethroughSpan(), startIndex!!.pop(), output.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun handlerStartSIZE(output: Editable, xmlReader: XMLReader) {
        if (startIndex == null) {
            startIndex = Stack()
        }
        startIndex!!.push(output.length)

        if (propertyValue == null) {
            propertyValue = Stack()
        }

        propertyValue!!.push(getProperty(xmlReader, "value"))
    }

    private fun handlerEndSIZE(output: Editable) {

        if (!isEmpty(propertyValue)) {
            try {
                val value = Integer.parseInt(propertyValue!!.pop())
                output.setSpan(AbsoluteSizeSpan(sp2px(context, value.toFloat())), startIndex!!.pop(), output.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 利用反射获取html标签的属性值
     *
     * @param xmlReader
     * @param property
     * @return
     */
    private fun getProperty(xmlReader: XMLReader, property: String): String? {
        try {
            val elementField = xmlReader.javaClass.getDeclaredField("theNewElement")
            elementField.isAccessible = true
            val element = elementField.get(xmlReader)
            val attsField = element.javaClass.getDeclaredField("theAtts")
            attsField.isAccessible = true
            val atts = attsField.get(element)
            val dataField = atts.javaClass.getDeclaredField("data")
            dataField.isAccessible = true
            val data = dataField.get(atts) as Array<String>
            val lengthField = atts.javaClass.getDeclaredField("length")
            lengthField.isAccessible = true
            val len = lengthField.get(atts) as Int

            for (i in 0 until len) {
                // 这边的property换成你自己的属性名就可以了
                if (property == data[i * 5 + 1]) {
                    return data[i * 5 + 4]
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    companion object {

        /**
         * 集合是否为空
         *
         * @param collection
         * @return
         */
        fun isEmpty(collection: Collection<*>?): Boolean {
            return collection == null || collection.isEmpty()
        }

        /**
         * 缩放独立像素 转换成 像素
         * @param context
         * @param spValue
         * @return
         */
        fun sp2px(context: Context, spValue: Float): Int {
            return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.resources.displayMetrics) + 0.5f).toInt()
        }
    }
}