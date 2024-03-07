package com.example.mycalculator

class AddOperation : MainActivity.AbstractOperation {
    override fun excute(left: Int, right: Int): Int = left+right
}