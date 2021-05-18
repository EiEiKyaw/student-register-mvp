package me.kaungmyatmin.studentreg.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import me.kaungmyatmin.studentreg.*
import me.kaungmyatmin.studentreg.data.AppDatabase
import me.kaungmyatmin.studentreg.presenter.MainPresenter
import me.kaungmyatmin.studentreg.view.MainView

class MainActivity : AppCompatActivity(),
    StudentItemListener, MainView {

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            return intent
        }
    }

    private lateinit var studentDao: StudentDao
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var mainPresenter: MainPresenter

    private val CREATE_REQUEST_CODE = 13 * 10
    private val UPDATE_REQUEST_CODE = 13 * 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentDao = AppDatabase.getDatabase(
            applicationContext
        ).getStudentDao()
        studentAdapter = StudentAdapter(this)
        mainPresenter = MainPresenter(studentDao)
        mainPresenter.registerView(this)

        rvStudent.adapter = studentAdapter
        rvStudent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mainPresenter.findAll()
//        val students = studentDao.findAll()
//        studentAdapter.setNewData(students)

        btnAdd.setOnClickListener {
            val intent =
                FormActivity.newIntent(this)
            startActivityForResult(intent, CREATE_REQUEST_CODE)
        }
    }

    /**
     * Backstack
     * |A| startActivity(B)
     * |A| |B| startActivity(A)
     * |A| |B| |C|
     *
     *
     * |A| startActivity(B)
     * |A| |B| finish()
     * |A|
     *
     *
     * |A| startActivity(B)
     * |A| |B| setResult() finish()
     * |A| onActivityResult()
     **/
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
        val intent = FormActivity.newIntent(
            this,
            student
        )
        startActivityForResult(intent, UPDATE_REQUEST_CODE)
    }

    override fun showEmptyMessage() {
        notifyUser("Student list is empty")
    }

    override fun showLoading() {
        notifyUser("Loading..........")
    }

    override fun hideLoading() {
        notifyUser("Student list..........")
    }

    override fun showData(studentList: List<Student>) {
        studentAdapter.setNewData(studentList)
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.unregisterView()
    }
}