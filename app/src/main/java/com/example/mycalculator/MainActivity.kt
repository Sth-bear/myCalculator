package com.example.mycalculator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val calculator by lazy { Calculator() } //lazy로 사용할 때 초기화
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
                when {
                    number == "B" -> if (currentText.isNotEmpty()) { //B버튼이며, 빈문자열이 아닌경우
                        resultView.text = currentText.dropLast(n = 1) // 맨뒷 글자를 자른다 , substring -> dropLast 교체
                    }

                    isOperator(text = number) && currentText.isNotEmpty() && isOperator(text = currentText.last().toString()) -> {
                        //연산자가 입력되었고      문자열이 비어있지않고           마지막이 연산자라면 -> 연산자 다음 연산자는 반응하지않는다.
                    }

                    number == "(  )" -> {
                        val numLeftParentheses = currentText.count { it == '(' }
                        val numRightParentheses = currentText.count { it == ')' }

                        if (numLeftParentheses == numRightParentheses && (currentText.isEmpty()) || isOperator(text = currentText.last().toString())) { //문자열 뒤에만 괄호는 열릴 수 있다.
                            resultView.text = currentText + "("
                        } else {
                            if (numLeftParentheses > numRightParentheses) {
                                // 괄호 쌍이 맞지 않을 경우 오른쪽 괄호를 추가한다.
                                resultView.text = currentText + ")"
                            }
                        }
                    }

                    currentText.isEmpty() && isOperator(text = number).not() -> {
                        resultView.text = currentText +  number
                    }

                    currentText.lastOrNull()?.toString() == ")" -> { //괄호가 닫힌 다음은 문자열 만 올 수 있다.
                        if (isOperator(text = number)) {
                            resultView.text = currentText + number
                        }
                    }

                    currentText.lastOrNull()?.toString() == "(" -> { // 괄호가 열린 다음은 숫자만 올 수 있다
                        if (isOperator(text = number).not()) {
                            resultView.text = currentText + number
                        }
                    }

                    else -> {
                        resultView.text = currentText + number
                    }
                }
            }
        } //버튼 입력시 조건문들
        btnResult.setOnClickListener {
            if (isOperator(text = resultView.text.last().toString()).not() && isOperator(resultView.text.toString())) { //마지막이 연산자가 아니고, 연산자가 한번이라도 있는경우)\
                val emptyList = mutableListOf<Int>()
                val emptyOperator = mutableListOf<Char>()
                sorter(emptyList,emptyOperator,resultView.text.toString()) //문자열에서 숫자와 문자를 분리.

                Sequence(emptyOperator,emptyList)   //사칙연산 곱셈, 나눗셈부터 진행

                resultView.text = emptyList[0].toString()
            }
        } // 결과 입력시 계산하기
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
                if (isOperator(text = i.toString()).not()) {
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
    private fun isOperator(text: String): Boolean {      //연산자인가?
        val operators = Operator.getOperators() // array에서 직접 찾던 것을 Operator class파일로 외부에 구현함.
        for (i in text) {
            if (i in operators) {
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
                        'x' -> calculator.calculate(emptyList[i], emptyList[i + 1], MultiplyOperation())
                        '/' -> calculator.calculate(emptyList[i], emptyList[i + 1], DivideOperation())
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
                        '+' -> calculator.calculate(emptyList[i], emptyList[i + 1], AddOperation())
                        '-' -> calculator.calculate(emptyList[i], emptyList[i + 1], SubstractOperation())
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