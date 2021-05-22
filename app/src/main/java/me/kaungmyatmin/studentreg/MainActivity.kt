package me.kaungmyatmin.studentreg

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StudentItemListener {

    private lateinit var studentDao: StudentDao
    private lateinit var studentAdapter: StudentAdapter

    private val CREATE_REQUEST_CODE = 13 * 10
    private val UPDATE_REQUEST_CODE = 13 * 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentDao = AppDatabase.getDatabase(applicationContext).getStudentDao()
        studentAdapter = StudentAdapter(this)

        rvStudent.adapter = studentAdapter
        rvStudent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val students = studentDao.findAll()
        studentAdapter.setNewData(students)

        btnAdd.setOnClickListener {
            val intent = FormActivity.newIntent(this)
            startActivityForResult(intent, CREATE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val id = data.getLongExtra(Constants.KEY_ID, -1)
            val student = studentDao.findById(id)
            studentAdapter.appendData(student)
        } else if (requestCode == UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val id = data.getLongExtra(Constants.KEY_ID, -1)
            val student = studentDao.findById(id)
            studentAdapter.updateData(student)
        }
    }

    override fun onDeleteClicked(student: Student) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Are you sure to delete")
            .setPositiveButton("Ok") { dialog, which ->
                studentDao.delete(student)
                studentAdapter.deleteData(student)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()
        dialog.show()

    }

    override fun onEditClicked(student: Student) {
        val intent = FormActivity.newIntent(this, student)
        startActivityForResult(intent, UPDATE_REQUEST_CODE)
    }


}