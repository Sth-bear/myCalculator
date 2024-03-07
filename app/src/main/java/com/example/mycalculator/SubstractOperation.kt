package com.example.mycalculator

class SubstractOperation : MainActivity.AbstractOperation {
    override fun excute(left: Int, right: Int): Int = left - right
}