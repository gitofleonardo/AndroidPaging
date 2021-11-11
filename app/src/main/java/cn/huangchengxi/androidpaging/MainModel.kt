package cn.huangchengxi.androidpaging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.random.Random

class MainModel {
    private val mCache = mutableListOf<String>()
    private val mItems = MutableSharedFlow<Result>(replay = 1)

    suspend fun loadPage(page:Int):Flow<Result>{
        if (page == 0) {
            mCache.clear()
        }
        fetch()
        return mItems
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