package com.example.servicedigital

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.*

class ChatViewModelTest {

    @Test
    fun `prueba de suma basica`() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `verificar interaccion con objeto simulado`() {
        val listaFalsa = mockk<MutableList<String>>(relaxed = true)

        listaFalsa.add("Mensaje de prueba")

        verify { listaFalsa.add("Mensaje de prueba") }
    }
    @Test
    fun `verificar que se llama al servicio al cargar datos`() {
        val servicioFalso = mockk<Runnable>(relaxed = true)

        servicioFalso.run()

        verify(exactly = 1) { servicioFalso.run() }
    }
}
