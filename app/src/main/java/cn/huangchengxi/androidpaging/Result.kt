package cn.huangchengxi.androidpaging

sealed interface Result{
    data class SuccessResult(val items:List<String>):Result
    data class FailResult(val reason:Exception):Result
}