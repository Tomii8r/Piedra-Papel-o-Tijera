# Piedra, Papel o Tijera

Juego de escritorio en **Java + Swing** que enfrenta al jugador contra la máquina, con
**dificultad progresiva**, **efectos de sonido**, tema oscuro minimalista y emojis (sin imágenes).

> El diseño completo (arquitectura MVC, POO, patrones GRASP, principios SOLID, flujos,
> diagramas y detalle de los tests) está en [`docs/CONTRIBUTING.md`](docs/CONTRIBUTING.md).

---

## Cómo se juega

1. En el menú principal, pulsá **Empezar juego**.
2. Elegí **Piedra 🪨**, **Papel 📄** o **Tijera ✂️** (botones o teclas `1` / `2` / `3`).
3. La máquina elige su jugada y se comparan:
   - **Ganás** → +1 al puntaje, suena el efecto de victoria y seguís jugando al instante.
   - **Perdés** → el puntaje vuelve a 0, suena el efecto de derrota (~2 s) y volvés al menú.
   - **Empate** → no suma ni resta y seguís jugando.

En pantalla se ve el puntaje y el **nivel** actual: `12 (Nivel 1)`.

## El bot aprende tu patrón

El bot **aprende desde la primera ronda** (`PredictorJugada`, una cadena de Markov): si venís
repitiendo, predice tu próxima jugada y te la **contrarresta con una probabilidad de al menos
50%**. Si jugás variado y no hay patrón claro, juega al azar.

El **nivel** (sube cada 10 victorias **seguidas**, tope 5, y vuelve a 0 al perder) se muestra
junto al puntaje: `12 (Nivel 1)`.

## Arquitectura (resumen)

Proyecto **Maven** con arquitectura **MVC** en el paquete `com.juego.ppt`:

```
src/main/java/com/juego/ppt/
├── Main.java        # Punto de entrada / raíz de composición
├── model/           # Jugada, Jugador, Partida, Dificultad, PredictorJugada, Generador*,
                     # ResolutorReglas
├── controller/      # JuegoController, Programador (interfaces + implementaciones)
└── view/            # VentanaPrincipal, PanelMenu, PanelJuego, Tema, BotonRedondeado,
                     # ReproductorSonido (efectos de victoria/derrota)

src/main/resources/com/juego/ppt/view/sonidos/
├── win.wav          # efecto de victoria
└── gameover.wav     # efecto de derrota
```

- **`model`** no conoce la GUI; **`view` → `controller` → `model`**.
- Aplica **POO**, **GRASP** y **SOLID** (detalles en `docs/CONTRIBUTING.md`).

---

## Requisitos

- **JDK 25**.
- No hace falta instalar Maven: el repositorio incluye el **Maven Wrapper** (`mvnw`).

## Compilar

```powershell
.\mvnw.cmd compile
```

## Ejecutar

```powershell
.\mvnw.cmd compile
java -cp target\classes com.juego.ppt.Main
```

(Si usás IntelliJ IDEA, basta con ejecutar la clase `com.juego.ppt.Main`.)

## Tests

Batería de **84 tests** con **JUnit 5**, sin abrir ventanas (modo headless):

```powershell
.\mvnw.cmd test
```

## Empaquetar

```powershell
.\mvnw.cmd package
```

Genera el `target/ppt-1.0-SNAPSHOT.jar`.
