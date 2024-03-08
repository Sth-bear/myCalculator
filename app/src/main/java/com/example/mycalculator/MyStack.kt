package com.example.mycalculator

import java.util.Stack

class MyStack {
    fun getPostFixExpressionOperation(originalExpression: String): Int {
        val stack = Stack<String>()
        val arr = strToArr(originalExpression)
        var result = ""

        for (e in arr) {
            when(e) {
                "+", "-", "*", "/" -> {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(e)) {
                        result += stack.pop() + " "
                    }
                    stack.push(e)
                }
                "(" -> {
                    stack.push(e)
                }
                ")" -> {
                    while (stack.peek() != "(") {
                        result += stack.pop() + " "
                    }
                    stack.pop() //"(" 제거
                }
                else -> {
                    result += "$e "
                }
            }
        }

        while (!stack.isEmpty()) {
            result += stack.pop() + " "
        }

        val realResult = finalCalc(result)
        return realResult
    }

    fun finalCalc(result: String): Int {
        val stack = Stack<String>()
        val calResult = result.split(" ") //공백제거
        var result = 0

        for (e in calResult) {
            when(e) {
                "+" -> {
                    result = stack.pop().toInt() + stack.pop().toInt()
                    stack.push(result.toString())
                }
                "-" -> {
                    result = -stack.pop().toInt() + stack.pop().toInt()
                    stack.push(result.toString())
                }
                "*" -> {
                    result = stack.pop().toInt() * stack.pop().toInt()
                    stack.push(result.toString())
                }
                "/" -> {
                    val num2 = stack.pop().toInt()
                    val num1 = stack.pop().toInt()
                    result = num1 / num2
                    stack.push(result.toString())
                }
                else -> {
                    stack.push(e)
                }
            }
        }
        return result
    }

    fun strToArr(str:String): Array<String> {
        var tempStr = str.replace("(", "(")
        tempStr = tempStr.replace(")", ")")
        return tempStr.split(" ").toTypedArray()
    }

    fun precedence(operator : String): Int {
        return when (operator) {
            "+", "-" ->1
            "*", "/" ->2
            else -> 3
        }
    }
}
