package com.example.calculator2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator2.ui.theme.Calculator2Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Calculator2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    Box(contentAlignment = Alignment.TopEnd){
                        Text(text = global.equation, color = Color.White, modifier = Modifier.size(1200.dp))
                    }
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Keypad()
                    }
                }
            }
        }
    }
}


@Composable
fun Keypad(){
    val operator=listOf("+", "-", "*", "/", ".", "(", ")")
    val eq= listOf("=")
    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(10) { i ->
            Button(onClick = {
                global.equation+=i.toString()
            }) {
                Text(text = i.toString(), color = Color.Black)
            }
        }
        items(operator){i->
            Button(onClick = {
                global.equation+=i
            }) {
                Text(text = i, color= Color.Black)
            }
        }
        items(eq){i->
            Button(onClick = {
                val res=bodmasCalc(global.equation).toString()
                global.equation=res
            }) {
                Text(text = i, color= Color.Black)
            }
        }
    }
}

fun bodmasCalc(equation: String): Double {

    val tokens = equation.replace(" ", "").toCharArray()
    val values = mutableListOf<Double>()
    val operator = mutableListOf<Char>()
    val operation = mutableListOf<Double>()
    val bracket = mutableListOf<Char>()
    var i = 0
    var dec = ""
    while (i < tokens.size) {
        if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
            operator.add(tokens[i])
            if (dec != "") {
                try {
                    values.add(dec.toDouble())
                    dec = ""
                } catch (e: Exception) {
                    println(e)
                    println("Enter a valid number")
                }
            }
        } else if (tokens[i] == '(') {
            var j = i
            j++
            while (tokens[j] != ')') {
                if (tokens[j] == '+' || tokens[j] == '-' || tokens[j] == '*' || tokens[j] == '/') {
                    bracket.add(tokens[j])
                    try {
                        operation.add(dec.toDouble())
                        dec = ""
                    } catch (e: Exception) {
                        println(e)
                        println("Enter a valid number")
                    }
                } else dec += tokens[j]
                j++
            }
            if (dec != "") {
                try {
                    operation.add(dec.toDouble())
                    dec = ""
                } catch (e: Exception) {
                    println(e)
                    println("Enter a valid number")
                }
            }
            //println(operation)
            while (bracket.isNotEmpty())
                compute(bracket, operation)
            values.add(operation.removeLast())
            i = j
        } else dec += tokens[i]
        i++
    }
    if (dec != "") {
        try {
            values.add(dec.toDouble())
        } catch (e: Exception) {
            println(e)
            println("Enter a valid number")
        }
    }

    while (operator.isNotEmpty())
        compute(operator, values)
    return values.last()
}

fun compute(operator: MutableList<Char>, values: MutableList<Double>) {
    var index: Int = -1
    var op = ' '
    var a: Double
    var b: Double
    if (operator.contains('/'))
        op = '/'
    else if (operator.contains('*'))
        op = '*'
    else if (operator.contains('+'))
        op = '+'
    else if (operator.contains('-'))
        op = '-'
    if (op != ' ') {
        index = operator.indexOf(op)
        a = values.removeAt(index)
        b = values.removeAt(index)
        values.add(index, operation(a, b, op))
        operator.removeAt(index)
    }
}

fun operation(a: Double, b: Double, op: Char): Double {
    if (op == '/')
        return a / b
    else if (op == '*')
        return a * b
    else if (op == '+')
        return a + b
    else if (op == '-')
        return a - b
    return 0.0
}


object global{
    var equation by mutableStateOf("")
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Calculator2Theme {
//        Greeting("Android")
//    }
//}