package br.com.giovaniguerra.masktextwatcher.extension

import android.icu.number.NumberFormatter
import android.icu.text.NumberingSystem
import android.text.method.DigitsKeyListener
import android.widget.EditText
import br.com.giovaniguerra.masktextwatcher.MaskTextWatcher
import br.com.giovaniguerra.masktextwatcher.MoneyTextWatcher
import br.com.giovaniguerra.masktextwatcher.util.Mask
import java.text.NumberFormat
import java.util.*

fun EditText.addMaskTextWatcher(
    mask: String,
    maskChar: Char = '#'
) {
    keyListener = DigitsKeyListener.getInstance("0123456789.- ()[]{}\\|/")
    addTextChangedListener(MaskTextWatcher(mask, maskChar))
}

fun EditText.addMoneyTextWatcher(
    mask: String = Mask.MONEY,
    locale: Locale = Locale.getDefault(),
    enableSymbol: Boolean = true
) {
    keyListener = DigitsKeyListener.getInstance("0123456789.,R$ ")

    val maskWithSymbol = if (enableSymbol) {
        val symbol = Currency.getInstance(locale).symbol
        "$symbol $mask"
    } else {
        mask
    }

    val defaultValue = "0,00"
    setText(defaultValue)

    val maskChar = '#'
    addTextChangedListener(MoneyTextWatcher(defaultValue))
}