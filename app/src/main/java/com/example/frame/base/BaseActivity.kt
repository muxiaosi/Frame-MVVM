package com.example.frame.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.frame.App
import com.example.frame.BR
import com.example.frame.R
import com.example.frame.utils.AppManager
import com.example.frame.utils.ClassUtils
import com.example.frame.widge.CommonLoadingDialog
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author mxs
 * @date 2019-12-20 18:10
 * @description 页面初始化
 */
abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    Presenter, BaseView {

    protected lateinit var mContext: Context

    protected lateinit var mBinding: ViewDataBinding

    protected lateinit var mViewModel: VM

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<VB>(this, getLayoutId())
        mBinding.setVariable(BR.presenter, this)
        mBinding.lifecycleOwner = this
        mContext = this

        initView()
        initViewModel()
        loadData()
        AppManager.addActivity(this)
        toolbar_back?.setOnClickListener { finish() }
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        val vm = ClassUtils.getGenericityType<BaseViewModel>(this, 1)
        vm?.let {
            mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance())
                .create(it.javaClass) as VM
        }
        initBaseView(mViewModel)
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun loadData()

    override fun onClick(v: View?) {
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
     * 设置toolbar 右边的文字
     *
     * @param resId
     */
    protected fun setToolbarRight(resId: Int) {
        if (resId <= 0) {
            return
        }
        setToolbarRight(getString(resId))
    }

    protected fun setToolbarRightOnClick(onClick: View.OnClickListener) {
        tv_toolbar_right?.setOnClickListener(onClick)
    }

    protected fun setToolbarRightColor(resId: Int) {
        tv_toolbar_right?.setTextColor(ContextCompat.getColor(this, resId))
    }

    protected fun setToolbarRight(resId: String) {
        if (!TextUtils.isEmpty(resId)) {
            tv_toolbar_right?.text = resId
            setToolbarRightVisibility(true)
        }
    }

    /**
     * 设置toolbar的中间的标题
     *
     * @param resId
     */
    protected fun setToolbarTitle(resId: Int) {
        if (resId <= 0) {
            return
        }
        setToolbarTitle(getString(resId))
    }

    protected fun setToolbarTitle(resId: String) {
        if (!TextUtils.isEmpty(resId)) {
            toolbar_title?.text = resId
        }
    }

    /**
     * 设置右边文字是否可见
     *
     * @param visibility true 可见；false 不可见
     */
    protected fun setToolbarRightVisibility(visibility: Boolean) {
        if (visibility) {
            tv_toolbar_right?.visibility = View.VISIBLE
        } else {
            tv_toolbar_right?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.finishActivity(this)
    }

}