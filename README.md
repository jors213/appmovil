# ServiceDigital 📱🛠️

> **Plataforma móvil nativa para la conexión de servicios profesionales en tiempo real.**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-4285F4.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-green.svg?style=flat)]()
[![Status](https://img.shields.io/badge/Status-MVP%20Ready-orange.svg)]()

## 📖 Descripción del Proyecto

**ServiceDigital** es una aplicación Android nativa diseñada para modernizar la forma en que los usuarios encuentran servicios técnicos (gasfitería, electricidad, reparaciones). A diferencia de un directorio estático, esta app permite la interacción en tiempo real mediante un sistema de chat integrado y validación de identidad a través de códigos QR.

El proyecto fue construido siguiendo las mejores prácticas de desarrollo moderno en Android (**Modern Android Development**), priorizando una UI declarativa, gestión de estados eficiente y una arquitectura escalable.

---

## 📱 Demo en Funcionamiento

![Demo App](assets/gifAppMovil)

---

## 🛠️ Stack Tecnológico

El proyecto utiliza un stack tecnológico de vanguardia, enfocado en rendimiento y mantenibilidad:

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%)
* **Interface de Usuario:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
    * Uso de *LazyColumns* para listas eficientes.
    * Animaciones de estado (`animateDpAsState`).
    * Estilos personalizados con gradientes y temas dinámicos (Dark/Light Mode).
* **Arquitectura:** MVVM (Model-View-ViewModel).
    * Separación clara de lógica de negocio y UI.
    * Uso de `ViewModel` y `StateFlow` para gestión reactiva de datos.
* **Networking:**
    * **Retrofit:** Para consumo de API REST.
    * **WebSockets:** Para funcionalidad de chat en tiempo real.
    * **Coroutines:** Para manejo asíncrono y concurrencia.
* **Hardware & Sensores:**
    * **CameraX:** Implementación de escáner de códigos QR para validación de servicios.

---

## ✨ Funcionalidades Clave

### 1. Catálogo de Servicios Interactivo
Listado dinámico de profesionales con tarjetas de alto impacto visual. Incluye detalles, calificación y acceso directo a contacto.

### 2. Chat en Tiempo Real
Sistema de mensajería instantánea entre cliente y proveedor.
* **Tech Highlight:** Implementación de WebSockets para comunicación bidireccional sin *polling*.
* **UI:** Burbujas de chat con diseño adaptativo y timestamps.

### 3. Validación QR (Security)
Sistema de seguridad donde el usuario puede escanear el código QR del técnico al llegar al domicilio para verificar su identidad y certificación (ej. certificación SEC).

### 4. Modo Oscuro / Claro
Gestión de temas centralizada (`ThemeManager`) que adapta toda la paleta de colores (neones y gradientes) según la preferencia del usuario.

---

## 📂 Estructura del Proyecto

El código sigue una estructura de paquetes limpia para facilitar la navegación:

```text
com.example.servicedigital
├── api/             # Interfaces de Retrofit
├── controller/      # Lógica de controladores auxiliares
├── model/           # Data Classes (User, Service, Message)
├── network/         # Configuración de clientes HTTP/Socket
├── ui/theme/        # Sistemas de diseño (Color, Type, Theme)
├── view/            # Pantallas (Compose) y ViewModels
│   ├── CatalogoScreen.kt
│   ├── ChatScreen.kt
│   ├── QrScannerScreen.kt
│   └── ...
└── MainActivity.kt  # Punto de entrada y navegación

**Desarrollado por Jorge Soto** - Ingeniero en Informática & Fullstack Developer.
