package cn.huangchengxi.androidpaging

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {
    private val mModel = MainModel()

    val state:LiveData<UIState>
    val action:LiveData<UIAction>

    init {
        action = MutableLiveData(UIAction(0))
        state = action
            .distinctUntilChanged()
            .switchMap {
                liveData {
                    val result = mModel.refreshItems()
                        .map { r->
                            UIState(r)
                        }
                        .asLiveData(Dispatchers.Main)
                    emitSource(result)
                }
            }
    }

    fun loadNextPage(){
        GlobalScope.launch {
            mModel.loadNextPage()
        }
    }
    fun refresh(){
        GlobalScope.launch {
            mModel.refreshItems()
        }
    }
}

data class UIState(
    val result:Result
)
data class UIAction(
    var page:Int
)