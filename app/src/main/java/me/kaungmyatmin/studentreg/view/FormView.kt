package me.kaungmyatmin.studentreg.view

interface FormView {
    fun showInsertSuccess(id: Long)
    fun showInsertFail()
    fun showUpdateSuccess(id: Long)
    fun showUpdateFail()
    fun showLoading()
    fun hideLoading()
}