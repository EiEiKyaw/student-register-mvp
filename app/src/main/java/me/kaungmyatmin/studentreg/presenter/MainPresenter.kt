package me.kaungmyatmin.studentreg.presenter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.kaungmyatmin.studentreg.StudentDao
import me.kaungmyatmin.studentreg.view.MainView

class MainPresenter(
    private val studentDao: StudentDao
) {
    private var view: MainView? = null

    fun registerView(view: MainView){
        this.view = view
    }

    fun unregisterView(){
        this.view = null
    }

    fun findAll(){
        GlobalScope.launch(Dispatchers.Main) {
            view?.showLoading()
            val result  = GlobalScope.async(Dispatchers.IO) {
                studentDao.findAll()
            }
            val studentList = result.await()
            view?.hideLoading()
            if(studentList.isEmpty()){
                view?.showEmptyMessage()
            }else{
                view?.showData(studentList)
            }
        }
    }
}