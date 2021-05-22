package me.kaungmyatmin.studentreg

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        studentDao = AppDatabase.getDatabase(applicationContext).getStudentDao()

        val student = getStudent()
        populateUi(student)

        btnOk.setOnClickListener {
            if (student != null) {
                updateStudent(student)
            } else {
                createStudent()
            }
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
        val student = Student(name = name, fatherName = fatherName, age = age, phone = phone)
        val id = studentDao.insert(student)
        if (id > -1) {
            val intent = Intent()
            intent.putExtra(Constants.KEY_ID, student.id)
            setResult(Activity.RESULT_OK, intent)
            notifyUser("Successfully created")
        } else {
            notifyUser("Can't create")
        }

    }

    private fun updateStudent(student: Student) {

        val name = etName.text.toString()
        val fatherName = etFatherName.text.toString()
        val age = etAge.text.toString().toInt()
        val phone = etPhone.text.toString()

        //todo: check validations
        val newStudent = Student(student.id, name, fatherName, age, phone)
        val rowCount = studentDao.update(newStudent)
        if (rowCount > 0) {
            val intent = Intent()
            intent.putExtra(Constants.KEY_ID, student.id)
            setResult(Activity.RESULT_OK, intent)
            notifyUser("Successfully updated")
        } else {
            notifyUser("Can't updated")
        }
    }


    private fun notifyUser(message: String) {
        Snackbar.make(btnOk, message, Snackbar.LENGTH_LONG).show()
    }


    private fun populateUi(student: Student?) {
        if (student != null) {
            title = "Update"

            btnOk.text = "Update"
        } else {
            title = "Create"
            btnOk.text = "Create"
        }
    }
}