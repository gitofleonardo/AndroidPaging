package cn.huangchengxi.androidpaging

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {
    private val mModel = MainModel()

    val state:LiveData<UIState>
    val perform:(UIAction)->Unit

    private val mPage:MutableLiveData<Int> = MutableLiveData()

    init {
        state = mPage
            .distinctUntilChanged()
            .switchMap {
                liveData {
                    val state = mModel.loadPage(it)
                        .map { r->
                            UIState(r)
                        }
                    val liveData = state.asLiveData(Dispatchers.Main)
                    emitSource(liveData)
                }
            }
        perform = {
            when (it.action){
                is Action.ActionRefresh->{
                    mPage.postValue(0)
                }
                is Action.ActionNextPage->{
                    mPage.postValue((mPage.value?:0)+1)
                }
            }
        }
    }
}

data class UIState(
    val result:Result
)
sealed interface Action {
    object ActionRefresh : Action
    object ActionNextPage : Action
}
data class UIAction(
    var action:Action
)