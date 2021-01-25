package br.com.giovaniguerra.masktextwatcher

import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import br.com.giovaniguerra.masktextwatcher.extension.applyMask

class MaskTextWatcher(
    private val mask: String,
    private val maskChar: Char = '#'
) : TextWatcher {

    private var start = 0
    private var before = 0
    private var count = 0
    private var isUpdating = false
    private var isAddingCharacter: Boolean = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        if (!isUpdating) {
            Log.d(TAG, "onTextChanged. s:$text; start:$start; before:$before; after:$count")

            isAddingCharacter = count > before
            this.start = start
            this.before = before
            this.count = count
        }
    }

    override fun afterTextChanged(s: Editable) {
        if (!isUpdating) {
            isUpdating = true
            setText(s)
            isUpdating = false
        }
    }

    private fun setText(s: Editable) {
        val formattedText = s.applyMask(mask, maskChar)
        s.replace(0, s.length, formattedText)
        Selection.setSelection(s, updateCursorPosition())
    }

    private fun updateCursorPosition(): Int =
        if (isAddingCharacter) moveCursorForward()
        else moveCursorBackwards()

    private fun moveCursorForward(): Int = when {
        hasReachMaskLimit() -> mask.length
        isNotMaskChar(start) -> getNextMaskIndex()
        else -> start + 1
    }

    private fun getNextMaskIndex(): Int = mask.indexOf(maskChar, start) + 1

    private fun hasReachMaskLimit(): Boolean = start + count >= mask.length

    private fun isNotMaskChar(index: Int): Boolean = mask[index] != maskChar

    private fun moveCursorBackwards(): Int = when {
        start == 0 -> 0
        isNotMaskChar(start - 1) -> getPreviousMaskIndex()
        else -> start
    }

    private fun getPreviousMaskIndex(): Int = mask.lastIndexOf(maskChar, start - 1) + 1

    companion object {
        private const val TAG = "MASK_WATCHER"
    }

}