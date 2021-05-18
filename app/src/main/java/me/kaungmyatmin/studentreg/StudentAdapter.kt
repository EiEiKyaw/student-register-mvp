package me.kaungmyatmin.studentreg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_student.view.*

class StudentAdapter(
    private val listener: StudentItemListener
) : RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {

    private var studentList: MutableList<Student> = mutableListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateUi(data: Student) {

            itemView.tvName.text = data.name
            itemView.tvFatherName.text  = data.fatherName
            itemView.tvPhone.text = data.phone
            itemView.tvAge.text = data.age.toString()

            itemView.btnEdit.setOnClickListener {
                listener.onEditClicked(data)
            }

            itemView.btnDelete.setOnClickListener {
                listener.onDeleteClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = studentList.get(position)
        holder.populateUi(data)
    }

    fun setNewData(newList: List<Student>) {
        studentList.clear()
        studentList.addAll(newList)
        notifyDataSetChanged()
    }

    fun deleteData(data: Student) {
        val index = studentList.indexOf(data)
        studentList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun appendData(data: Student) {
        studentList.add(data)
        notifyItemInserted(studentList.size - 1)
    }

    fun updateData(data: Student) {
        val index = studentList.indexOfFirst { it.id == data.id }
        studentList[index] = data
        notifyItemChanged(index)
    }

}