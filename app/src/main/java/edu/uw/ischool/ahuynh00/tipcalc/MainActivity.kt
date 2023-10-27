package edu.uw.ischool.ahuynh00.tipcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var etTotal : EditText? = null;
    var bTip: Button? = null;

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            etTotal!!.text.toString()?.let {
                etTotal!!.removeTextChangedListener(this);

                val totalInput = dollarsToCents(s.toString());
                Log.d("cleanInput", totalInput.toString());
                bTip!!.isEnabled = (totalInput !== 0);

                etTotal!!.setText(formatToDollars(totalInput))
                etTotal!!.setSelection(etTotal!!.length())
                etTotal!!.addTextChangedListener(this);
            }
        }
        override fun afterTextChanged(s: Editable) {}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTotal = findViewById(R.id.total);
        bTip = findViewById(R.id.tipButton);
        bTip!!.isEnabled = false;
        etTotal!!.addTextChangedListener(textWatcher);

        bTip!!.setOnClickListener() {
            Log.i("button", "button clicked");
            val total = dollarsToCents(etTotal!!.text.toString())
            total ?.let {
                val tip = calculateTip(total);
                Log.i("tipcalc", tip.toString())
                Log.i("formatting", formatToDollars(tip))
                Toast.makeText(applicationContext, formatToDollars(tip), Toast.LENGTH_LONG).show();
            }
        }
    }
    private fun calculateTip(cents: Int, percent: Int = 15) : Int {
        return cents * percent / 100;
    }
    private fun formatToDollars(cents: Int): String {
        val centString = cents.toString();
        val formattedDollars = when (centString.length) {
            0 -> "$00.00"
            1 -> "$00.0$centString";
            2 -> "$00.$centString";
            else -> {
                val dollars = centString.substring(0, centString.length - 2);
                val remainder = centString.substring(centString.length - 2);
                "$$dollars.$remainder";
            }
        }
        return formattedDollars;
    }

    private fun dollarsToCents(formattedDollars: String): Int {
        var cents = formattedDollars.replace("[$,.]".toRegex(), "");
        cents = cents.replace("/^0+/".toRegex(), "")
        val centsInt = cents.toInt();
        return centsInt;
    }

    // spinner
    // screenshots




}