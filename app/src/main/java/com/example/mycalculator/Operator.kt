package com.example.mycalculator

enum class Operator (val operator : Char){
    PLLUS(operator = '+'),
    MINUS(operator = '-'),
    MULTIPLY(operator = 'x'),
    DIVIDE(operator = '/'),
    MODULO(operator = '%');

    companion object {
        fun getOperators() = entries.map { it.operator }
    }
}