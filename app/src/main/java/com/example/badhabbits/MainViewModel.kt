package com.example.badhabbits

import androidx.activity.result.ActivityResultRegistryOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class MainViewModel(
    private val repository: MainRepository,
    private val communication: MainCommunication.Mutable,
) : MainCommunication.Observe {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            val days = repository.days()
            val value: UiState =
                if (days == 0)
                    UiState.ZeroDays
                else
                    UiState.NDays(days)
            communication.put(value)
        }

    }

    override fun observe(owner: LifecycleOwner, observer: Observer<UiState>) {
        communication.observe(owner, observer)
    }
}

sealed class UiState {
    object ZeroDays : UiState() {

    }

    data class NDays(private val days: Int) : UiState() {

    }
}

interface MainCommunication {
    interface Put {
        fun put(value: UiState)
    }

    interface Observe {
        fun observe(owner: LifecycleOwner, observer: Observer<UiState>)
    }

    interface Mutable : Put, Observe {

    }

    class Base(
        private val liveData: MutableLiveData<UiState>
    ) : Mutable {
        override fun put(value: UiState) {
            liveData.value = value
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<UiState>) {
            liveData.observe(owner, observer)
        }
    }

}

interface MainRepository {
    fun days(): Int
}