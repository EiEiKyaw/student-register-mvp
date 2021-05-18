package me.kaungmyatmin.studentreg

interface StudentItemListener {
    fun onDeleteClicked(student: Student)
    fun onEditClicked(student: Student)
}