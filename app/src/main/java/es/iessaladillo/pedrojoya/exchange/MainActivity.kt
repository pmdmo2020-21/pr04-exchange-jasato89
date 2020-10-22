package es.iessaladillo.pedrojoya.exchange

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.exchange.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private var value = 0.0
    private var conversionValue = 0.0
    private var inCurrencyId = R.id.rdbEuro
    private var OutCurrencyId = R.id.rdbOutDollar
    private var inImgId = Currency.EURO.symbol
    private var OutImgId = Currency.DOLLAR.symbol
    private lateinit var input: EditText
    private lateinit var rdbInEuro: RadioButton
    private lateinit var rdbInPound: RadioButton
    private lateinit var rdbInDollar: RadioButton
    private lateinit var rdbOutEuro: RadioButton
    private lateinit var rdbOutPound: RadioButton
    private lateinit var rdbOutDollar: RadioButton
    private lateinit var imgIn: ImageView
    private lateinit var imgOut: ImageView
    private lateinit var rdgInCurrency: RadioGroup
    private lateinit var rdgOutCourrency: RadioGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariables()
        setupViews()


    }

    private fun initVariables() {
        input = binding.inputAmount
        rdbInDollar = binding.rdbDollar
        rdbInEuro = binding.rdbEuro
        rdbInPound = binding.rdbPound
        rdbOutDollar = binding.rdbOutDollar
        rdbOutPound = binding.rdbOutPound
        rdbOutEuro = binding.rdbOutEuro
        imgIn = binding.imgIn
        imgOut = binding.imgOut
        rdgInCurrency = binding.rdgInCurrency
        rdgOutCourrency = binding.rdgOutCurrency

    }

    private fun setupViews() {
        //Preparamos la vista InputAmount
        input.selectAll()
        input.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculate()
                false
            } else {
                true
            }

        }


        rdbInEuro.isChecked = true
        imgIn.setImageResource(Currency.EURO.drawableResId)
        rdbOutDollar.isChecked = true

        rdgInCurrency.setOnCheckedChangeListener { radioGroup: RadioGroup, checkedId: Int ->
            defineInCurrencyImg(checkedId)
        }
        rdgOutCourrency.setOnCheckedChangeListener { group, checkedId ->
            defineOutCurrencyImg(checkedId)
        }

        textChangedListener()
        binding.exchangeButton.setOnClickListener { v -> calculate() }

    }

    private fun textChangedListener() {
        input.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (input.text.isBlank()) {
                    input.text = Editable.Factory.getInstance().newEditable("0")
                    input.selectAll()
                }
                value = binding.inputAmount.text.toString().toDouble()
            }


        })

        input.setOnClickListener { input.selectAll() }
    }


    private fun defineOutCurrencyImg(checkedId: Int) {
        when (checkedId) {
            R.id.rdbOutDollar -> {
                imgOut.setImageResource(Currency.DOLLAR.drawableResId)
                OutImgId = Currency.DOLLAR.symbol
                rdbInDollar.isChecked = false
                rdbInEuro.isEnabled = true
                rdbInPound.isEnabled = true
                OutCurrencyId = R.id.rdbOutDollar
            }
            R.id.rdbOutEuro -> {
                imgOut.setImageResource(Currency.EURO.drawableResId)
                OutImgId = Currency.EURO.symbol
                rdbInDollar.isEnabled = true
                rdbInEuro.isEnabled = false
                rdbInPound.isEnabled = true
                OutCurrencyId = R.id.rdbOutEuro
            }
            R.id.rdbOutPound -> {
                imgOut.setImageResource(Currency.POUND.drawableResId)
                OutImgId = Currency.POUND.symbol
                rdbInDollar.isEnabled = true
                rdbInEuro.isEnabled = true
                rdbInPound.isEnabled = false
                OutCurrencyId = R.id.rdbOutPound
            }
        }

    }

    private fun defineInCurrencyImg(checkedId: Int) {
        when (checkedId) {
            R.id.rdbDollar -> {
                imgIn.setImageResource(Currency.DOLLAR.drawableResId)
                inImgId = Currency.DOLLAR.symbol
                rdbOutDollar.isEnabled = false
                rdbOutEuro.isEnabled = true
                rdbOutPound.isEnabled = true
                inCurrencyId = R.id.rdbDollar
            }
            R.id.rdbEuro -> {
                imgIn.setImageResource(Currency.EURO.drawableResId)
                inImgId = Currency.EURO.symbol
                rdbOutDollar.isEnabled = true
                rdbOutEuro.isEnabled = false
                rdbOutPound.isEnabled = true
                inCurrencyId = R.id.rdbEuro

            }
            R.id.rdbPound -> {
                imgIn.setImageResource(Currency.POUND.drawableResId)
                inImgId = Currency.POUND.symbol
                rdbOutDollar.isEnabled = true
                rdbOutEuro.isEnabled = true
                rdbOutPound.isEnabled = false
                inCurrencyId = R.id.rdbPound
            }
        }

    }

    private fun calculate() {
        closeKeyboard()
        var dollar = 0.0
        dollar = Currency.DOLLAR.toDollar(value)
        when (inCurrencyId) {
            R.id.rdbEuro -> dollar = Currency.EURO.toDollar(value)
            R.id.rdbPound -> dollar = Currency.POUND.toDollar(value)
            R.id.rdbDollar -> dollar = Currency.DOLLAR.toDollar(value)
        }
        when (OutCurrencyId) {
            R.id.rdbOutEuro -> conversionValue = Currency.EURO.fromDollar(dollar)
            R.id.rdbOutPound -> conversionValue = Currency.POUND.fromDollar(dollar)
            R.id.rdbOutDollar -> conversionValue = Currency.DOLLAR.fromDollar(dollar)
        }

        Toast.makeText(
            this,
            "${String.format("%.2f", value)} $inImgId = ${
                String.format(
                    "%.2f",
                    conversionValue
                )
            } $OutImgId",
            Toast.LENGTH_SHORT
        ).show()

    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

        }

    }


}




