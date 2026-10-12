# Piedra, Papel o Tijera

Juego de escritorio en **Java + Swing** que enfrenta al jugador contra la máquina, con
**dificultad progresiva**, tema oscuro minimalista y emojis (sin imágenes).

> El diseño completo (arquitectura MVC, POO, patrones GRASP, principios SOLID, flujos,
> diagramas y detalle de los tests) está en [`docs/CONTRIBUTING.md`](docs/CONTRIBUTING.md).

---

## Cómo se juega

1. En el menú principal, pulsá **Empezar juego**.
2. Elegí **Piedra 🪨**, **Papel 📄** o **Tijera ✂️** (botones o teclas `1` / `2` / `3`).
3. La máquina elige su jugada y se comparan:
   - **Ganás** → +1 al puntaje y seguís jugando.
   - **Perdés** → el puntaje vuelve a 0 y, tras una pausa breve, volvés al menú.
   - **Empate** → no suma ni resta y seguís jugando.

En pantalla se ve el puntaje y el **nivel** actual: `12 (Nivel 1)`.

## Dificultad progresiva

El nivel depende de las **victorias acumuladas** (no se resetea al perder) y sube cada
**10 victorias**, hasta un tope de **5**. El bot arranca fácil (se deja ganar más seguido
que el azar) y se vuelve más listo (contrarresta tu jugada anterior), sin volverse imbatible.

| Nivel | Victorias | P(ganar) aprox. |
|---|---|---|
| 0 | 0–9 | 0,77 |
| 1 | 10–19 | 0,64 |
| 2 | 20–29 | 0,51 |
| 3 | 30–39 | 0,39 |
| 4 | 40–49 | 0,26 |
| 5 | 50+ | 0,13 |

## Arquitectura (resumen)

Proyecto **Maven** con arquitectura **MVC** en el paquete `com.juego.ppt`:

```
src/main/java/com/juego/ppt/
├── Main.java        # Punto de entrada / raíz de composición
├── model/           # Jugada, Jugador, Partida, Dificultad, Generador*, ResolutorReglas
├── controller/      # JuegoController, Programador (interfaces + implementaciones)
└── view/            # VentanaPrincipal, PanelMenu, PanelJuego, Tema, BotonRedondeado
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

Batería de **78 tests** con **JUnit 5**, sin abrir ventanas (modo headless):

```powershell
.\mvnw.cmd test
```

## Empaquetar

```powershell
.\mvnw.cmd package
```

Genera el `target/ppt-1.0-SNAPSHOT.jar`.
