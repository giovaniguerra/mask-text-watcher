package br.com.giovaniguerra.masktextwatcher.util

class Mask private constructor() {
    companion object {
        const val CNPJ = "XX.XXX.XXX/XXXX-XX"
        const val CPF = "###.###.###-##"
        const val PHONE = "(##) #####-####"
        const val MONEY = "###.###.###.###.###,##"
    }
}