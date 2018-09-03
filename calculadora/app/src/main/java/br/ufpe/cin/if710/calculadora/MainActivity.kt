package br.ufpe.cin.if710.calculadora

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Listeners dos botoes, para que ao clicar seja adicionado a formula no campo de texto
        btn_7.setOnClickListener{
            text_calc.append("7")
        }
        btn_8.setOnClickListener{
            text_calc.append("8")
        }
        btn_9.setOnClickListener{
            text_calc.append("9")
        }
        //metodo append normal nao funciona para caracteres diferentes de numeros, criei um stringbuilder para que funcionasse
        // e usei o setSelection para corrigir o problema que ocorre ao usar setText(o cursor do text ficava perdido)
        btn_Divide.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text).append("/")
            text_calc.setText(sb.toString())
            text_calc.setSelection(text_calc.length())
        }
        btn_4.setOnClickListener{
            text_calc.append("4")
        }
        btn_5.setOnClickListener{
            text_calc.append("5")
        }
        btn_6.setOnClickListener{
            text_calc.append("6")
        }
        btn_Multiply.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text).append("*")
            text_calc.setText(sb.toString())
            text_calc.setSelection(text_calc.length())
        }
        btn_1.setOnClickListener{
            text_calc.append("1")
        }
        btn_2.setOnClickListener{
            text_calc.append("2")
        }
        btn_3.setOnClickListener{
            text_calc.append("3")
        }
        btn_Subtract.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text).append("-")
            text_calc.setText(sb.toString())
            text_calc.setSelection(text_calc.length())
        }
        btn_Dot.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text).append(".")
            text_calc.setText(sb.toString())
            text_calc.setSelection(text_calc.length())
        }
        btn_0.setOnClickListener{
            text_calc.append("0")
        }
        //Crio um string builder para utilizar o eval
        btn_Equal.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text)
            val teste = eval(sb.toString())
            text_calc.setText(teste.toString())
        }
        btn_Add.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text).append("+")
            text_calc.setText(sb.toString())
            text_calc.setSelection(text_calc.length())
        }
        btn_LParen.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text).append("(")
            text_calc.setText(sb.toString())
            text_calc.setSelection(text_calc.length())
        }
        btn_RParen.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text).append(")")
            text_calc.setText(sb.toString())
            text_calc.setSelection(text_calc.length())
        }
        btn_Power.setOnClickListener{
            val sb = StringBuilder()
            sb.append(text_calc.text).append("^")
            text_calc.setText(sb.toString())
            text_calc.setSelection(text_calc.length())
        }
        btn_Clear.setOnClickListener{
            text_calc.setText("")
        }


    }

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
