// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.support.design.widget.AppBarLayout

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private var currentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout?.let {
            if (verticalOffset == 0) {
                if (currentState != State.EXPANDED) {
                    onStateChanged(it, State.EXPANDED)
                }
                currentState = State.EXPANDED;
            } else if (Math.abs(verticalOffset) >= it.totalScrollRange) {
                if (currentState != State.COLLAPSED) {
                    onStateChanged(it, State.COLLAPSED)
                }
                currentState = State.COLLAPSED
            } else {
                if (currentState != State.IDLE) {
                    onStateChanged(it, State.IDLE)
                }

            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)
}