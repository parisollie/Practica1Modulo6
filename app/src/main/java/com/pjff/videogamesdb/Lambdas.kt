package com.pjff.videogamesdb


fun main() {
    //
    operaNumeros(52,26)
    operaNumeros(30,18)

    val miLambdaSuma: (Int, Int) -> Unit = { a, b -> println("La suma de $a + $b es: ${a+b}") }

    operaNumeros(20,10, miLambdaSuma, {})

    //recibe 2 parametros e interpolamos
    operaNumeros(20,10, { a, b ->
        println("La suma de $a + $b es: ${a + b}")
    }, { name ->
        println(name)
    })

    operaNumeros(20, 10, { a,b ->
        println("La multiplicación de $a * $b es: ${a*b}")
        //Aqui mandamos a llamar a la funcion lambda
    }, { name ->
        println("Hola $name")
    })

}
//Aquí hace una sobrecarga de Numeros como en java,por eso podemos poner 2 funciones
//con el mismo nombre
fun operaNumeros(num1: Int, num2: Int){
    //Interpolamos ${num1+num2}
    println("La suma de $num1 + $num2 es ${num1+num2}")
}

/* Operacion con Lambda , operacion: (Int, Int) -> Unit, lambda2: (String) le paso una funcion
como un parametro a eso operaNumeros, de tal manera que puedo mandar a llamar a la funcion
el unit no regresa nada
*/

//operacion: (Int, Int) -> Unit, le pasamos una funcion

fun operaNumeros(num1: Int, num2: Int, operacion: (Int, Int) -> Unit, lambda2: (String) -> Unit){
    operacion(num1, num2)
    lambda2("Paullie")
}