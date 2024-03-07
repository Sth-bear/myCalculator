package com.example.mycalculator

class DivideOperation : MainActivity.AbstractOperation {
    override fun excute(left: Int, right: Int): Int {
        if (right == 0) {
            return -1
        } else {
            return (left / right)
        }
    }
}