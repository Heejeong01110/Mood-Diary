package kr.ac.kpu.dailystone

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.Window

class KeyboardVisibilityUtils(
    private val window : Window,
    private val onShowKeyboard: ((keyboardHeight: Int) -> Unit)? = null,
    private val onHideKeyboard : (() -> Unit)? = null
) {
    private val MIN_KEYBOARD_HEIGHT_PX = 150

    private val windowVisibilityDisplayFrame = Rect()
    private var lastVisibilityDecorViewHeight: Int = 0

    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        window.decorView.getWindowVisibleDisplayFrame(windowVisibilityDisplayFrame)
        val visibleDecorViewHeight = windowVisibilityDisplayFrame.height()

        if (lastVisibilityDecorViewHeight != 0){
            if(lastVisibilityDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX){
                val currentKeyboardHeight = window.decorView.height - windowVisibilityDisplayFrame.bottom
                onShowKeyboard?.invoke(currentKeyboardHeight)
            }else if(lastVisibilityDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight){
                onHideKeyboard?.invoke()
            }
        }
        lastVisibilityDecorViewHeight = visibleDecorViewHeight
    }

    init{
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }
    fun detachKeyboardListeners(){
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }
}