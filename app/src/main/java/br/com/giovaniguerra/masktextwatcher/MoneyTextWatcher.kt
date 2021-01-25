package br.com.giovaniguerra.masktextwatcher

import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import br.com.giovaniguerra.masktextwatcher.extension.unmask
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(
    defaultText: String,
    locale: Locale = Locale("pt", "BR")
) : TextWatcher {

    private var start = 0
    private var before = 0
    private var count = 0
    private var isUpdating = false
    private var isAddingCharacter: Boolean = false
    private var oldText: CharSequence = defaultText
    private var newText: CharSequence = ""

    private val formatter = NumberFormat.getInstance(locale).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
        minimumIntegerDigits = 1
        maximumIntegerDigits = 15
    }

    private val maxDigits: Int by lazy {
        formatter.maximumIntegerDigits + formatter.maximumFractionDigits
    }

    private var changedCharIndex: Int = 0
    private var changedChar: Char = ' '

    override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
        if (!isUpdating) {
            Log.d(TAG, "beforeTextChanged. s:$text; start:$start; count:$count; after:$after")
            this.start = start

            isAddingCharacter = after > count
            if (!isAddingCharacter) {
                changedCharIndex = start
                changedChar = text[start]
            }
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        if (!isUpdating) {
            Log.d(TAG, "onTextChanged. s:$text; start:$start; before:$before; after:$count")

            if (isAddingCharacter) {
                changedCharIndex = start
                changedChar = text[changedCharIndex]
            }
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
        newText = s.applyMoneyMask()
        val newCursorPosition = updateCursorPosition()
        s.replace(0, s.length, newText)
        Selection.setSelection(s, newCursorPosition)
        oldText = newText
    }

    private fun CharSequence.applyMoneyMask(): CharSequence {
        val text = if (isAddingCharacter) formatAddedText(this)
        else formatRemovedText(this)
        val value = text.unmask().toString().toBigDecimalOrNull() ?: BigDecimal.ZERO
        return formatter.format(value.divide(BigDecimal(100)))
    }

    private fun formatAddedText(text: CharSequence): CharSequence {
        val isCursorAtEnd = start == text.length - 1
        val cleanText = text.unmask()
        val hasReachMaxDigits =
            cleanText.length >= formatter.maximumFractionDigits + formatter.maximumIntegerDigits

        return if (!isCursorAtEnd && hasReachMaxDigits) {
            cleanText.substring(0 until maxDigits)
        } else {
            text
        }
    }

    private fun formatRemovedText(text: CharSequence): CharSequence {
        return if (!changedChar.isDigit()) {
            val indexToBeRemoved = changedCharIndex - 1
            text.removeRange(indexToBeRemoved..indexToBeRemoved)
        } else {
            text
        }
    }

    private fun updateCursorPosition(): Int =
        if (isAddingCharacter) moveCursorForward()
        else moveCursorBackwards()

    private fun moveCursorForward(): Int {
        val hasReachMaxDigits = oldText.unmask().length == maxDigits
        return when {
            start == oldText.length && hasReachMaxDigits -> start
            hasReachMaxDigits -> newText.indexOf(changedChar, changedCharIndex) + 1
            else -> newText.length - (oldText.length - start)
        }
    }

    private fun moveCursorBackwards(): Int {
        val position = oldText.length - (start + 1)
        return newText.length - position
    }

    companion object {
        private const val TAG = "MONEY_MASK_WATCHER"
    }

}