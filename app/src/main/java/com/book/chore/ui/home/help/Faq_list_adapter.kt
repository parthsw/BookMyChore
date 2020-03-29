package com.book.chore.ui.home.help

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import android.widget.Toast
import com.book.chore.R

class Faq_list_adapter(var context: Context,var expandableListView: ExpandableListView,var header:MutableList<String>,var body:MutableList<MutableList<String>>): BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): String {
        return header[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
       return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
       var convertView = convertView
        if(convertView==null)
        {
            val inflator=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView= inflator.inflate(R.layout.layout_group,null)
        }

        val title=convertView?.findViewById<TextView>(R.id.tv_title)
        title?.text=getGroup(groupPosition)
        title?.setOnClickListener {
            if (expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.collapseGroup(groupPosition)
            } else
                expandableListView.expandGroup(groupPosition)
        }
        return convertView!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
       return body[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
                return body[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView
        if(convertView==null)
        {
            val inflator=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView= inflator.inflate(R.layout.layout_child,null)
        }

        val title=convertView?.findViewById<TextView>(R.id.tv_title)
        title?.text=getChild(groupPosition,childPosition)
        return convertView!!
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
       return  childPosition.toLong()
    }

    override fun getGroupCount(): Int {
    return header.size
    }
}