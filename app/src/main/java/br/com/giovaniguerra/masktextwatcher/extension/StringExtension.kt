package br.com.giovaniguerra.masktextwatcher.extension

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

internal val NOT_NUMBER_REGEX = "[\\D]".toRegex()

internal fun CharSequence?.unmaskOrNull(): CharSequence? =
    this?.let { it removeAll NOT_NUMBER_REGEX }

internal infix fun CharSequence.removeAll(regex: Regex): CharSequence = replace(regex, "")

internal fun CharSequence.unmask(): CharSequence = this removeAll NOT_NUMBER_REGEX

internal fun CharSequence.applyMask(mask: CharSequence, charValidator: Char): CharSequence {
    if (this.isBlank()) {
        return ""
    }

    val newValue = this.unmask()

    val maskedNewValue = CharArray(mask.length)

    var i = 0
    var j = 0
    while (i < maskedNewValue.size) {
        val maskChar = mask[i]

        val hasNewValue = newValue.length > j

        if (hasNewValue && maskChar == charValidator) {
            maskedNewValue[i] = newValue[j++]
        } else if (hasNewValue) {
            maskedNewValue[i] = maskChar
        } else {
            break
        }
        i++

    }

    return maskedNewValue.concatToString(0, i)
}


internal fun CharSequence.applyMaskReversed(mask: CharSequence, charValidator: Char): CharSequence {
    if (this.isBlank()) {
        return ""
    }

    val newValue = this.unmask()

    val maskedNewValue = CharArray(mask.length)

    var i = mask.indices.last
    var j = newValue.indices.last
    while (i > 0) {
        val maskChar = mask[i]

        val hasNewValue = j >= 0

        if (hasNewValue && maskChar == charValidator) {
            maskedNewValue[i] = newValue[j--]
        } else if (hasNewValue) {
            maskedNewValue[i] = maskChar
        } else {
            break
        }
        i--

    }

    return maskedNewValue.concatToString(i + 1, mask.length)
}