package cn.huangchengxi.androidpaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.paging.Pager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.androidpaging.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.flow

class MainActivity : AppCompatActivity() {
    private val mViewModel = MainViewModel()
    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mItems by lazy { ArrayList<String>() }
    private val mAdapter by lazy { Adapter(mItems) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
            })
        }
        mBinding.srl.setOnRefreshListener {
            mViewModel.perform.invoke(UIAction(Action.ActionRefresh))
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