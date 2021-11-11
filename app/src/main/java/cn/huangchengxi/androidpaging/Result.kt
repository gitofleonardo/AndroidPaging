package cn.huangchengxi.androidpaging

sealed interface Result{
    data class SuccessResult(val items:List<String>,val page:Int = 0):Result
    data class FailResult(val reason:Exception):Result
}