package com.example.mycalculator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    //test git name login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultView = findViewById<TextView>(R.id.result_view)
        val numberButtons = arrayOf<Button>(                    //숫자,연산자 버튼 배열
            findViewById(R.id.btn_zero),
            findViewById(R.id.btn_one),
            findViewById(R.id.btn_two),
            findViewById(R.id.btn_three),
            findViewById(R.id.btn_four),
            findViewById(R.id.btn_five),
            findViewById(R.id.btn_six),
            findViewById(R.id.btn_seven),
            findViewById(R.id.btn_eight),
            findViewById(R.id.btn_nine),
            findViewById(R.id.btn_slash),
            findViewById(R.id.btn_plus),
            findViewById(R.id.btn_minus),
            findViewById(R.id.btn_x),
            findViewById(R.id.btn_back),
            findViewById(R.id.btn_parenthesis)
        )//버튼할당
        val btnResult = findViewById<Button>(R.id.btn_result) //결과버튼


        for (button in numberButtons) {
            button.setOnClickListener {
                val number = button.text.toString()     // 누른 버튼은 어떤 값인가?
                val currentText = resultView.text.toString()        // textView에는 뭐가 들어가있는가?
                if (number == "B") {
                    if (resultView.text.isNotEmpty()) { // 적혀있는 값이 있으면
                        resultView.text = resultView.text.toString().substring(0, resultView.text.length - 1) // 맨 뒤에를 지운다.
                    }
                } else if (isOperator(number) && currentText.isNotEmpty() && isOperator(currentText.last().toString())) {
                    // 연산자가 입력되었고, 입력된 값이 있고, 마지막이 연산자였다면. 아무것도 하지 않는다.
                } else if (number == "(  )") {
                    val numLeftParentheses = currentText.count { it == '(' }
                    val numRightParentheses = currentText.count { it == ')' }

                    if (numLeftParentheses == numRightParentheses && (currentText.isEmpty()) || isOperator(currentText.last().toString())) {
                        resultView.text = resultView.text.toString() + "("
                    } else {
                        if (numLeftParentheses > numRightParentheses) {
                            // 괄호 쌍이 맞지 않을 경우 오른쪽 괄호를 추가한다.
                            resultView.text = resultView.text.toString() + ")"
                        }
                    }
                } else if (currentText.isEmpty() && isOperator(number).not()) {
                    // 입력창이 비어있고, 숫자가 입력되었다면 그대로 숫자를 추가한다.
                    resultView.text = resultView.text.toString() + number
                } else if (currentText.lastOrNull()?.toString() == ")" ) {
                    if (isOperator(number)) {
                        resultView.text = resultView.text.toString() + number //")" 이후에는 연산자만 올 수 있다.
                    }
                } else if (currentText.lastOrNull()?.toString() == "("){
                    if (isOperator(number).not()) {
                        resultView.text = resultView.text.toString() + number
                    }
                } else {
                    // 그 외의 경우에는 숫자를 추가한다.
                    resultView.text = resultView.text.toString() + number
                }
            }
        }
        btnResult.setOnClickListener {
            if (isOperator(resultView.text.last().toString()).not() && isOperator(resultView.text.toString())) { //마지막이 연산자가 아니고, 연산자가 한번이라도 있는경우)\
                val emptyList = mutableListOf<Int>()
                val emptyOperator = mutableListOf<Char>()
                sorter(emptyList,emptyOperator,resultView.text.toString()) //문자열에서 숫자와 문자를 분리.

                Sequence(emptyOperator,emptyList)   //사칙연산 곱셈, 나눗셈부터 진행

                resultView.text = emptyList[0].toString()
            }
        }
    }

    fun sorter (emptyList: MutableList<Int>, emptyOperator: MutableList<Char>, resultView: String) {
        var num = ""
        var isOpen: Boolean = false
        var extraNum = ""
        var extraList : MutableList<Int> = mutableListOf()
        var extraOperator : MutableList<Char> = mutableListOf()
        var text = resultView
        if(text.isNotEmpty() && text[0] == '-') {
            text = "0$text"
        }
        for (i in text) {
            if (i == '(') {
                isOpen = true
            }
            if (i ==')') {
                isOpen = false
                sorter(extraList,extraOperator,extraNum.substring(1))
                Sequence(extraOperator,extraList)
                var resultSequence = extraList[0]
                if(resultSequence < 0) {
                    resultSequence *= -1
                    emptyList.add(0)
                    emptyList.add(resultSequence)
                    emptyOperator.add('-')
                } else {
                    emptyList.add(resultSequence)
                }
                extraNum = ""
                extraList = mutableListOf()
                extraOperator = mutableListOf()
                continue
                //거짓으로 바꾸고, 괄호를 땐 값을 sorter에 집어넣는다. 이 안에 sequence를 집어넣어야한다. 그리고 뱉어내자.
            }
            if (isOpen.not()){
                if (isOperator(i.toString()).not()) {
                    num += i.toString()
                } else {
                    emptyOperator.add(i)
                    if(num.isNotEmpty()){
                        emptyList.add(num.toInt()) //괄호 이후 연산자가 있다면, num은 비어있다.
                    }
                    num = ""
                }
            }
            if (isOpen) {
                extraNum += i
            }
        }
        if(num.isNotEmpty()) {
            emptyList.add(num.toInt())
        }
    }
    fun isOperator(text:String): Boolean {      //연산자인가?
        for (i in text) {
            if (i.toString() in arrayOf("+", "-", "x", "/")) {
                return true
            }
        }
        return false
    }

    fun Sequence(emptyOperator : MutableList<Char>, emptyList: MutableList<Int>) {
        while ('x' in emptyOperator || '/' in emptyOperator) {
            for(i in 0 until emptyOperator.size) {
                if (emptyOperator[i] == 'x' || emptyOperator[i] == '/') {
                    val result = when (emptyOperator[i]) {
                        'x' -> Calculator().calculate(emptyList[i], emptyList[i + 1], MultiplyOperation())
                        '/' -> Calculator().calculate(emptyList[i], emptyList[i + 1], DivideOperation())
                        else -> throw IllegalArgumentException("지원되지 않는 연산자입니다.")
                    }
                    emptyList[i] = result
                    emptyList.removeAt(i+1)
                    emptyOperator.removeAt(i)
                    break
                }
            }
        }
        while ('+' in emptyOperator || '-' in emptyOperator) {
            for(i in 0 until emptyOperator.size) {
                if (emptyOperator[i] == '+' || emptyOperator[i] == '-') {
                    val result = when (emptyOperator[i]) {
                        '+' -> Calculator().calculate(emptyList[i], emptyList[i + 1], AddOperation())
                        '-' -> Calculator().calculate(emptyList[i], emptyList[i + 1], SubstractOperation())
                        else -> throw IllegalArgumentException("지원되지 않는 연산자입니다.")
                    }
                    emptyList[i] = result
                    emptyList.removeAt(i+1)
                    emptyOperator.removeAt(i)
                    break
                }
            }
        }
    } //우선도진행함수

    interface AbstractOperation {
        fun excute(left:Int, right:Int):Int
    }

    class Calculator {
        fun calculate(left : Int, right:Int, operation:AbstractOperation) = operation.excute(left, right)
    }
}