package com.example.mycalculator

class MultiplyOperation : MainActivity.AbstractOperation {
    override fun excute(left: Int, right: Int): Int = left * right
}