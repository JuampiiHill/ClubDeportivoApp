package com.example.clubdeportivo

import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(private var memberList: List<Member>) :
    RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memberName: TextView = itemView.findViewById(R.id.memberName)
        val statusQuote : TextView = itemView.findViewById(R.id.memberStatusQuote)
    }

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.activity_socio_item, parent, false)
    return MemberViewHolder(view)
}

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = memberList[position]
        val context = holder.itemView.context

        holder.memberName.text = context.getString(
            R.string.member_fullname,
            member.surname,
            member.name
        )

        val estado = if (member.pay) "Paga" else "Vencida"
        val colorStatus = if (member.pay) "#4CAF50" else "#C3241A"

        val completeText = context.getString(R.string.quote_status, estado)

        val spannable = SpannableString(completeText)
        val start = completeText.indexOf(estado)
        val end = start + estado.length

        spannable.setSpan(
            ForegroundColorSpan(colorStatus.toColorInt()),
            start, end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        holder.statusQuote.text = spannable

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CarnetActivity::class.java)
            intent.putExtra("document", member.document)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = memberList.size

    fun updateList(filteredList: List<Member>) {
        memberList = filteredList
        notifyDataSetChanged()
    }
}