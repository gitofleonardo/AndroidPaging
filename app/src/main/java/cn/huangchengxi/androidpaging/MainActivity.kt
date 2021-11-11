package cn.huangchengxi.androidpaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.androidpaging.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val mViewModel = MainViewModel()
    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mItems by lazy { ArrayList<String>() }
    private val mAdapter by lazy { Adapter(mItems) }
    private val mPagingAdapter by lazy { MainPagingAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mPagingAdapter
/*            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE){
                        recyclerView.layoutManager?.let {
                            val lastVisible = (it as LinearLayoutManager).findLastVisibleItemPosition()
                            if (lastVisible == mItems.size-1){
                                mViewModel.perform.invoke(UIAction(Action.ActionNextPage))
                            }
                        }
                    }
                }
            })*/
        }
        lifecycleScope.launch {
            mViewModel.flow.collectLatest {
                mPagingAdapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            mPagingAdapter.loadStateFlow.collect {
                mBinding.srl.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }
        mBinding.srl.setOnRefreshListener {
            mPagingAdapter.refresh()
        }
        mViewModel.state.observe(this){
            when (it.result){
                is Result.SuccessResult->{
                    mAdapter.submitList(it.result.items)
                    mBinding.srl.isRefreshing = false
                }
                is Result.FailResult->{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}