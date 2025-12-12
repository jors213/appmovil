package com.example.servicedigital

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.*

class ChatViewModelTest {

    // Prueba 1: JUnit Básico
    @Test
    fun `prueba de suma basica`() {
        assertEquals(4, 2 + 2)
    }

    // Prueba 2: Uso de MockK
    @Test
    fun `verificar interaccion con objeto simulado`() {
        // 1. Creamos un "Mock" (un objeto falso)
        val listaFalsa = mockk<MutableList<String>>(relaxed = true)

        // 2. Ejecutamos una acción sobre el objeto falso
        listaFalsa.add("Mensaje de prueba")

        // 3. Verificamos que la acción realmente ocurrió
        verify { listaFalsa.add("Mensaje de prueba") }
    }
    // Prueba 3: Simulación de un Caso de Uso Real (ViewModel -> Servicio)
    @Test
    fun `verificar que se llama al servicio al cargar datos`() {
        // A. Preparamos el escenario
        // Simulamos una clase 'Servicio' que tiene una función 'obtenerDatos'
        val servicioFalso = mockk<Runnable>(relaxed = true)

        // B. Ejecutamos la acción (como si el ViewModel llamara al servicio)
        servicioFalso.run()

        // C. Verificación (Assert)
        // Confirmamos que el metodo .run() fue llamado exactamente 1 vez
        verify(exactly = 1) { servicioFalso.run() }
    }
}
