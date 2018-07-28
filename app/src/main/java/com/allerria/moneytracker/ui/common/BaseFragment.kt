package com.allerria.moneytracker.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import dagger.android.support.AndroidSupportInjection


abstract class BaseFragment: MvpAppCompatFragment() {
    protected abstract val layoutRes: Int
    abstract val TAG: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { restoreState(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutRes, container, false)

    protected open fun restoreState(state: Bundle) {}
}