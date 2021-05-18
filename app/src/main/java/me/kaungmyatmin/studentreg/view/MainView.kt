package me.kaungmyatmin.studentreg.view

import me.kaungmyatmin.studentreg.Student

interface MainView {
    fun showEmptyMessage()
    fun showLoading()
    fun hideLoading()
    fun showData(studentList:List<Student>)
}