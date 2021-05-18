package me.kaungmyatmin.studentreg.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.item_student.*
import me.kaungmyatmin.studentreg.*
import me.kaungmyatmin.studentreg.data.AppDatabase
import me.kaungmyatmin.studentreg.presenter.FormPresenter
import me.kaungmyatmin.studentreg.view.FormView

class FormActivity : AppCompatActivity(),
    FormView {

    companion object {
        fun newIntent(context: Context, student: Student? = null): Intent {
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra("student", student)
            return intent
        }
    }

    private fun getStudent(): Student? {
        return intent.getParcelableExtra("student")
    }

    private lateinit var studentDao: StudentDao
    private lateinit var formPresenter: FormPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        studentDao = AppDatabase.getDatabase(
            applicationContext
        ).getStudentDao()
        formPresenter = FormPresenter(studentDao)
        formPresenter.registerView(this)

        val student = getStudent()
        populateUi(student)

        btnOk.setOnClickListener {
            if (student != null) {
                updateStudent(student)
            } else {
                createStudent()
            }
            startActivity(MainActivity.newIntent(this))
        }
        btnCancel.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun createStudent() {

        val name = etName.text.toString()
        val fatherName = etFatherName.text.toString()
        val age = etAge.text.toString().toInt()
        val phone = etPhone.text.toString()

        //todo: check validations
        val student = Student(
            name = name,
            fatherName = fatherName,
            age = age,
            phone = phone
        )
        formPresenter.insertStudent(student)
//        if (id > -1) {
//            val intent = Intent()
//            intent.putExtra(Constants.KEY_ID, id)
//            setResult(Activity.RESULT_OK, intent)
//            notifyUser("Successfully created")
//        } else {
//            notifyUser("Can't create")
//        }

    }

    private fun updateStudent(student: Student) {

        val name = etName.text.toString()
        val fatherName = etFatherName.text.toString()
        val age = etAge.text.toString().toInt()
        val phone = etPhone.text.toString()

        //todo: check validations
        val newStudent = Student(
            student.id,
            name,
            fatherName,
            age,
            phone
        )
        formPresenter.updateStudent(newStudent)
//        val rowCount = studentDao.update(newStudent)
//        if (rowCount > 0) {
//            val intent = Intent()
//            intent.putExtra(Constants.KEY_ID, student.id)
//            setResult(Activity.RESULT_OK, intent)
//            notifyUser("Successfully updated")
//        } else {
//            notifyUser("Can't updated")
//        }
    }


    private fun notifyUser(message: String) {
        Snackbar.make(btnOk, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun populateUi(student: Student?) {
        if (student != null) {
            title = "Update"
            etName.setText(student.name)
            etFatherName.setText(student.fatherName)
            etAge.setText(student.age.toString())
            etPhone.setText(student.phone)
            btnOk.text = getString(R.string.lb_update)
        } else {
            title = "Create"
            btnOk.text = getString(R.string.lb_create)
        }
    }

    override fun showInsertSuccess(id: Long) {
        val intent = Intent()
        intent.putExtra(Constants.KEY_ID, id)
        setResult(Activity.RESULT_OK, intent)
        notifyUser("Successfully created")
    }

    override fun showInsertFail() {
        notifyUser("Can't create")
    }

    override fun showUpdateSuccess(id: Long) {
        val intent = Intent()
        intent.putExtra(Constants.KEY_ID, id)
        setResult(Activity.RESULT_OK, intent)
        notifyUser("Successfully updated")
    }

    override fun showUpdateFail() {
        notifyUser("Can't updated")
    }

    override fun showLoading() {
        notifyUser("Loading..........")
    }

    override fun hideLoading() {
        notifyUser("Finished.........")
    }

    override fun onDestroy() {
        super.onDestroy()
        formPresenter.unregisterView()
    }
}