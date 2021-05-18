package me.kaungmyatmin.studentreg.presenter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.kaungmyatmin.studentreg.Student
import me.kaungmyatmin.studentreg.StudentDao
import me.kaungmyatmin.studentreg.view.FormView

// not prefer and should not include android. importing - this is difficult for unit testing bcz of including context (import android.........starting android.)
//class FormPresenter(context: Context) {
//    private val studentDao:StudentDao
//
//    init {
//        studentDao = AppDatabase.getDatabase(context).getStudentDao()
//    }
//
//    fun insertStudent(student: Student){
//
//    }
//}

class FormPresenter(private val studentDao: StudentDao) {
    private var view: FormView? = null

    fun registerView(view: FormView) {
        this.view = view
    }

    fun unregisterView() {
        this.view = null
    }

    fun insertStudent(student: Student) {
        GlobalScope.launch(Dispatchers.Main) {
            view?.showLoading()

            /*val result = GlobalScope.async(Dispatchers.IO) {
                studentDao.insert(student)
            }
            val id = result.await()*/

//            val id = insertStudentInDatabase(student)
            val id = studentDao.insert(student)
            view?.hideLoading()

            if (id > -1) {
                view?.showInsertSuccess(id)
            } else {
                view?.showInsertFail()
            }
        }
    }

    fun updateStudent(student: Student) {
        GlobalScope.launch(Dispatchers.Main) {
            view?.showLoading()

            /* val result = GlobalScope.async {
                 studentDao.update(student)
             }
             val rowCount = result.await()*/

            val rowCount = updateStudentInDatabase(student)
            view?.hideLoading()
            if (rowCount > 0) {
                view?.showUpdateSuccess(student.id?:-1)
            } else {
                view?.showUpdateFail()
            }
        }
    }

    /**
     * suspend => thread will be wait at somewhere within the function
     * suspend => either calling function must be suspend or must be called from coroutine block
     **/
    /*private suspend fun insertStudentInDatabase(student: Student): Long {
        val result = GlobalScope.async {
            studentDao.insert(student)
        }
        return result.await()
    }*/

    private suspend fun updateStudentInDatabase(student: Student): Int {
        val result = GlobalScope.async {
            studentDao.update(student)
        }
        return result.await()
    }
}