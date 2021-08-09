package com.fever.weather.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel<VIEW_STATE, VIEW_EVENT> : BaseViewModelStateless<VIEW_EVENT>() {

    private val _viewState = MutableLiveData<VIEW_STATE>()
    val viewState: LiveData<VIEW_STATE>
        get() = _viewState

    open fun init() {
        viewModelScope.launch {
            val viewState = getInitialViewState()
            _viewState.value = viewState
        }
    }

    protected abstract suspend fun getInitialViewState(): VIEW_STATE

    protected fun updateViewState(update: VIEW_STATE.() -> VIEW_STATE) {
        val newState = update(getViewState())
        _viewState.value = newState
    }

    private fun getViewState(): VIEW_STATE = _viewState.value!!


}