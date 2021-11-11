package cn.huangchengxi.androidpaging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class MainPagingSource:PagingSource<Int,String>() {
    private val mRepository = MainModel()

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let { ap->
            val anchorPage = state.closestPageToPosition(ap)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = params.key?:0
        return when (val response = mRepository.loadPage1(page)){
            is Result.SuccessResult->{
                LoadResult.Page(
                    data = response.items,
                    prevKey = null,
                    nextKey = if (response.items.isEmpty()) null else response.page
                )
            }
            is Result.FailResult->{
                LoadResult.Error(
                    throwable = response.reason
                )
            }
        }
    }
}