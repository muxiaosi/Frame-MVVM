package com.example.frame.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.frame.App
import com.example.frame.BR
import com.example.frame.R
import com.example.frame.utils.ClassUtils
import com.example.frame.widge.CommonLoadingDialog

/**
 * @author mxs
 * @date 2019-12-20 14:24
 * @description 基础fragment
 */
abstract class BaseFragment<VB : ViewDataBinding, VM : BaseViewModel> : Fragment(), Presenter,
    BaseView {

    protected lateinit var mBinding: VB

    protected var mViewModel: VM? = null

    protected lateinit var mContext: Context

    /**
     * 是否懒加载
     */
    protected var lazyLoad = false

    /**
     * 标志位，标志已经初始化完成
     */
    private var isPrepared: Boolean = false

    private var visible = false

    private val loadingViewRoot: ConstraintLayout by lazy {
        mBinding.root.findViewById<ConstraintLayout>(
            R.id.loading_view_root
        )
    }
    private val emptyViewRoot by lazy { mBinding.root.findViewById<LinearLayout>(R.id.empty_layout) }
    private val netErrorViewRoot by lazy { mBinding.root.findViewById<LinearLayout>(R.id.ll_net_error_layout) }
    private val dataViewGit: ImageView by lazy { mBinding.root.findViewById<ImageView>(R.id.loading_gif) }

    private fun isContextInit() = ::mContext.isInitialized

    private val showLoading by lazy { CommonLoadingDialog(mContext) }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = activity ?: throw  Exception("activity 为null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        isPrepared = true
        if (lazyLoad) {
            //延迟加载，需要重写lazyLoad方法
            lazyLoad()
        } else {
            //加载数据
            loadData(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false)
        mBinding.setVariable(BR.presenter, this)
        mBinding.executePendingBindings()
        mBinding.lifecycleOwner = this
        initViewModel()
        return mBinding.root
    }

    /**
     * 获取BaseFragment所带的泛型
     */
    private fun initViewModel() {
        val vm = ClassUtils.getGenericityType<BaseViewModel>(this, 1)
        vm?.let {
            mViewModel =  ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance())
                .create(it.javaClass) as VM
        }
//        vm?.let {
//            mViewModel = ViewModelProviders.of(this).get(it::class.java)
//        }
        initBaseView(mViewModel)
    }

    /**
     * 是否可见，延迟加载
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            visible = true
            onVisible()
        } else {
            visible = false
            onInvisible()
        }
    }

    /**
     * 设置布局文件
     */
    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun lazyLoad()
    /**
     * 加载数据的方法
     */
    abstract fun loadData(isRefresh: Boolean)

    /**
     * 处理状态值
     */
    open fun initArgs(savedInstanceState: Bundle?) {

    }


    private fun onInvisible() {

    }

    private fun onVisible() {
        lazyLoad()
    }

    override fun onClick(v: View?) {
    }

    override fun showLoadingLayout() {
        loadingViewRoot?.let {
            if (loadingViewRoot.visibility != View.VISIBLE) {
                loadingViewRoot.visibility = View.VISIBLE
            }
            Glide.with(mContext)
                .load(R.mipmap.loading_pic)
                .into(dataViewGit)
        }
    }

    override fun hideLoadingLayout() {
        loadingViewRoot?.let {
            if (loadingViewRoot.visibility != View.GONE) {
                loadingViewRoot.visibility = View.GONE
            }
        }
    }

    override fun showEmptyLayout(isShow: Boolean) {
        emptyViewRoot?.let {
            if (isShow) {
                if (emptyViewRoot.visibility != View.VISIBLE) {
                    emptyViewRoot.visibility = View.VISIBLE
                }
            } else {
                if (emptyViewRoot.visibility != View.GONE) {
                    emptyViewRoot.visibility = View.GONE
                }
            }
        }
    }

    override fun showLoading() {
        if (isContextInit() && !showLoading.isShowing) {
            showLoading.show()
        }
    }

    override fun hideLoading() {
        if (showLoading.isShowing) {
            showLoading.cancel()
        }
    }

    override fun showNetErrorLayout(isShow: Boolean) {
        netErrorViewRoot?.let {
            if (isShow) {
                if (netErrorViewRoot.visibility != View.VISIBLE) {
                    netErrorViewRoot.visibility = View.VISIBLE
                }
            } else {
                if (netErrorViewRoot.visibility != View.GONE) {
                    netErrorViewRoot.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 控制加载页，空页面，网络请求loading，空页面
     */
    private fun initBaseView(viewModel: BaseViewModel?) {
        viewModel?.mShowLoadingLayout?.observe(this, Observer {
            when (it) {
                true -> {
                    showLoadingLayout()
                }
                else -> {
                    hideLoadingLayout()
                }
            }
        })
        viewModel?.mShowLoading?.observe(this, Observer {
            when (it) {
                true -> {
                    showLoading()
                }
                else -> {
                    hideLoading()
                }
            }
        })
        viewModel?.mShowEmptyLayout?.observe(this, Observer {
            when (it) {
                true -> {
                    showEmptyLayout(true)
                }
                else -> {
                    showEmptyLayout(false)
                }
            }
        })
    }

    /**
     * 初始化下拉刷新状态
     */
    fun initRefresh(view: SwipeRefreshLayout) {
        mViewModel?.isRefreshing?.observe(this, Observer {
            view.isRefreshing = it!!
        })
    }

    override fun onDestroyView() {
        mViewModel?.onCleared()
        super.onDestroyView()
    }


}