package com.utaite.player.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable


abstract class BaseFragment : Fragment() {

    protected abstract val layoutId: Int
    protected abstract val self: Fragment

    val disposables by lazy { CompositeDisposable() }

    val activity: AppCompatActivity by lazy { (getActivity() as AppCompatActivity) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
            init()

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    abstract fun init()

}
