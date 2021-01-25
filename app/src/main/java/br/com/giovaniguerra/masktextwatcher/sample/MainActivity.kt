package br.com.giovaniguerra.masktextwatcher.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.giovaniguerra.masktextwatcher.databinding.ActivityMainBinding
import br.com.giovaniguerra.masktextwatcher.extension.addMaskTextWatcher
import br.com.giovaniguerra.masktextwatcher.extension.addMoneyTextWatcher
import br.com.giovaniguerra.masktextwatcher.util.Mask

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.editText.addMaskTextWatcher(Mask.PHONE)

        binding.editText2.addMaskTextWatcher(Mask.CPF)

        binding.editText3.addMoneyTextWatcher()
    }
}