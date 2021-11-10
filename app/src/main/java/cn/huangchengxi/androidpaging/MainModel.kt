package cn.huangchengxi.androidpaging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.random.Random

class MainModel {
    private val mCache = mutableListOf<String>()
    private val mItems = MutableSharedFlow<Result>()
    private var mCurrentPage = 0

    suspend fun refreshItems():Flow<Result>{
        mCache.clear()
        mCurrentPage = 0
        fetch()
        return mItems
    }

    suspend fun loadNextPage(){
        fetch()
        ++mCurrentPage
    }

    private suspend fun fetch():Result{
        val items = generateItems()
        mCache.addAll(items)
        val result = Result.SuccessResult(mCache)
        mItems.emit(result)
        return result
    }

    private fun generateItems():List<String>{
        val result = mutableListOf<String>()
        for (i in 0 until 10){
            result.add(Random.nextInt().toString())
        }
        return result
    }
}