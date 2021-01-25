package br.com.giovaniguerra.masktextwatcher.util

import br.com.giovaniguerra.masktextwatcher.extension.applyMaskReversed
import br.com.giovaniguerra.masktextwatcher.extension.applyMoneyMask
import org.junit.Test


class StringExtensionTest {

    @Test
    fun test() {
        val value = "1234"
        val mask = Mask.CPF
        val maskChar = '#'
        val expectedValue = "12-34"

        val maskedValue = value.applyMaskReversed(mask, maskChar)

        assert(expectedValue == maskedValue)
    }

    @Test
    fun test2() {
        val value = "34"
        val expectedValue = "0,34"

        val maskedValue = value.applyMoneyMask()

        assert(expectedValue == maskedValue)
    }

}