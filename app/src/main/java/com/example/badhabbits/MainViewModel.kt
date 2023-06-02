package com.example.badhabbits

import android.view.View
import android.widget.Button
import android.widget.TextView
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
    abstract fun apply(daysTextView: TextView, resetBtn: Button)

    object ZeroDays : UiState() {
        override fun apply(daysTextView: TextView, resetBtn: Button) {
            daysTextView.text = "0"
            resetBtn.visibility = View.GONE
        }

    }

    data class NDays(private val days: Int) : UiState() {
        override fun apply(daysTextView: TextView, resetBtn: Button) {
            daysTextView.text = days.toString()
            resetBtn.visibility = View.GONE
        }

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