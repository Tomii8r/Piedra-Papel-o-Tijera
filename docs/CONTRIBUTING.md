# Diseño del proyecto — Piedra, Papel o Tijera

Documento de diseño y arquitectura del juego **Piedra, Papel o Tijera**, sincronizado con la
implementación actual, el refactor de testabilidad y la batería de tests.

---

## 1. Visión general

Aplicación de escritorio que implementa el clásico juego *Piedra, Papel o Tijera* contra
la máquina, con interfaz gráfica. El objetivo es aplicar de forma didáctica:

- **Arquitectura MVC** (Modelo - Vista - Controlador).
- **Programación Orientada a Objetos** (POO).
- **Patrones GRASP** (responsabilidades y asignación de clases).
- **Principios SOLID**.
- **Testeo automatizado** con JUnit 5 y Maven.

### Reglas del juego

1. El jugador entra al menú principal y pulsa **Empezar juego**.
2. Elige entre **Piedra**, **Papel** o **Tijera**.
3. El bot elige su jugada con `GeneradorDificultad` (azar sesgado según el nivel).
4. Se comparan ambas jugadas:
   - **Gana** → se suma **+1** al puntaje actual. El juego continúa.
   - **Pierde** → el puntaje actual se **reinicia a 0**, se muestra el resultado durante una
     **pausa breve (~1,2 s)** y se vuelve al **menú principal**.
   - **Empata** → la partida cuenta como jugada pero **no suma ni resta**; se permanece
     en la pantalla de juego.
5. `puntaje` y `puntajeMaximo` se guardan en **variables en memoria** (sin persistencia).
6. La **dificultad sube con las victorias acumuladas** (contador persistente que **no se
   resetea al perder**) cada 10 victorias, hasta un tope (`NIVEL_MAX = 5`). El bot va desde
   "juega fácil" (se deja ganar más seguido que el azar) hasta "contrarresta tu jugada
   anterior", sin volverse imbatible. El nivel se muestra al lado del puntaje: **`12 (Nivel 1)`**.

### Dificultad progresiva

| Nivel | Victorias | Prob. de contrarrestar | Prob. de dejarse ganar | P(ganar) aprox. |
|---|---|---|---|---|
| 0 | 0–9   | 0,00 | 0,65 | 0,77 |
| 1 | 10–19 | 0,12 | 0,52 | 0,64 |
| 2 | 20–29 | 0,24 | 0,39 | 0,51 |
| 3 | 30–39 | 0,36 | 0,26 | 0,39 |
| 4 | 40–49 | 0,48 | 0,13 | 0,26 |
| 5 | 50+   | 0,60 | 0,00 | 0,13 |

El nivel se mide sobre las **victorias acumuladas** (no sobre la racha), así que perder no
borra el progreso. La referencia del bot es la **jugada anterior** del humano (no la de la
ronda en curso); en la primera ronda juega al azar puro.

### Tecnologías

- **Lenguaje:** Java 25.
- **GUI:** Java Swing (`JFrame`, `JPanel`, `JButton`, `JLabel`, `CardLayout`, `Timer`).
- **Aleatoriedad:** `java.util.Random` (inyectado).
- **Build:** Maven (wrapper `mvnw`) + JUnit 5 (Jupiter).
- **Tema visual:** oscuro minimalista, con emojis en lugar de imágenes.

---

## 2. Estructura del proyecto

Layout estándar de Maven:

```
ppt/
├── pom.xml
├── mvnw / mvnw.cmd / .mvn/wrapper/maven-wrapper.properties
├── docs/CONTRIBUTING.md
├── src/main/java/com/juego/ppt/
│   ├── Main.java                  # Punto de entrada / raíz de composición
│   ├── model/                     # Lógica de negocio (independiente de la GUI)
│   │   ├── Jugada.java
│   │   ├── Resultado.java
│   │   ├── Jugador.java
│   │   ├── JugadorHumano.java
│   │   ├── JugadorMaquina.java
│   │   ├── GeneradorJugada.java
│   │   ├── GeneradorRandom.java
│   │   ├── GeneradorDificultad.java
│   │   ├── Dificultad.java
│   │   ├── ResolutorReglas.java
│   │   └── Partida.java
│   ├── controller/                # Coordinación entre vista y modelo
│   │   ├── IControlador.java
│   │   ├── JuegoController.java
│   │   ├── Programador.java
│   │   └── ProgramadorSwing.java
│   └── view/                      # Interfaz gráfica (Swing)
│       ├── INavegador.java
│       ├── IPanelMenu.java
│       ├── IPanelJuego.java
│       ├── VentanaPrincipal.java
│       ├── PanelMenu.java
│       ├── PanelJuego.java
│       ├── Tema.java
│       └── BotonRedondeado.java
└── src/test/java/com/juego/ppt/
    ├── model/
    │   ├── GeneradorGuion.java    # Doble de prueba
    │   ├── RandomFijo.java        # Doble de prueba (Random controlado)
    │   ├── JugadaTest.java
    │   ├── ResolutorReglasTest.java
    │   ├── DificultadTest.java
    │   ├── GeneradorRandomTest.java
    │   ├── GeneradorDificultadTest.java
    │   ├── JugadorHumanoTest.java
    │   ├── JugadorMaquinaTest.java
    │   └── PartidaTest.java
    ├── view/
    │   └── TemaTest.java
    └── controller/
        ├── JuegoControllerTest.java
        ├── NavegadorFake.java
        ├── ProgramadorInmediato.java
        ├── PanelMenuFake.java
        └── PanelJuegoFake.java
```

**Regla de dependencias:** `view` → `controller` → `model`. El modelo **no conoce** ni a la
vista ni al controlador. El controlador depende de **abstracciones** (`INavegador`,
`IPanelMenu`, `IPanelJuego`, `Programador`), nunca de las clases Swing concretas.

---

## 3. Modelo

### 3.1 `Jugada` (enum) — *Information Expert*

Representa las tres jugadas posibles y **es quien conoce las reglas de comparación**.

```java
public enum Jugada {
    PIEDRA, PAPEL, TIJERA;

    public boolean venceA(Jugada otra) {
        return (this == PIEDRA && otra == TIJERA)
            || (this == TIJERA && otra == PAPEL)
            || (this == PAPEL && otra == PIEDRA);
    }

    public boolean empataCon(Jugada otra) {
        return this == otra;
    }

    public Jugada vencedora() {
        for (Jugada opcion : values()) {
            if (opcion.venceA(this)) return opcion;
        }
        throw new IllegalStateException("Ninguna jugada vence a " + this);
    }

    public Jugada perdedora() {
        for (Jugada opcion : values()) {
            if (this.venceA(opcion)) return opcion;
        }
        throw new IllegalStateException("Ninguna jugada pierde contra " + this);
    }
}
```

| Método | Responsabilidad |
|---|---|
| `venceA(Jugada)` | Indica si esta jugada derrota a otra. |
| `empataCon(Jugada)` | Indica si ambas jugadas son iguales. |
| `vencedora()` | Devuelve la jugada que vence a `this` (la usa el bot para contrarrestar). |
| `perdedora()` | Devuelve la jugada a la que `this` vence (la usa el bot para dejarse ganar). |

> **GRASP – Information Expert:** la clase que posee la información (las reglas) es
> responsable de resolverlas.

### 3.2 `Resultado` (enum)

```java
public enum Resultado { GANA, PIERDE, EMPATA }
```

### 3.3 `Jugador` (clase abstracta) — *Polymorphism*

```java
public abstract class Jugador {
    protected final String nombre;

    protected Jugador(String nombre) { this.nombre = nombre; }

    public abstract Jugada elegirJugada();

    public String getNombre() { return nombre; }
}
```

- `JugadorHumano`: recibe la jugada elegida desde la vista a través del controlador.
- `JugadorMaquina` (bot): delega la elección en un `GeneradorJugada`.

```java
public class JugadorHumano extends Jugador {
    private Jugada jugada;

    public JugadorHumano(String nombre) { super(nombre); }

    public void setJugada(Jugada jugada) { this.jugada = jugada; }

    @Override public Jugada elegirJugada() { return jugada; }
}

public class JugadorMaquina extends Jugador {
    private final GeneradorJugada generador;

    private Jugada rival;
    private int nivel;

    public JugadorMaquina(String nombre, GeneradorJugada generador) {
        super(nombre);
        this.generador = generador;
    }

    public void preparar(Jugada rival, int nivel) {
        this.rival = rival;
        this.nivel = nivel;
    }

    @Override public Jugada elegirJugada() { return generador.generar(rival, nivel); }
}
```

### 3.4 `GeneradorJugada` (interfaz), `GeneradorRandom`, `Dificultad` y `GeneradorDificultad` — *Protected Variations / DIP*

```java
public interface GeneradorJugada {
    Jugada generar(Jugada jugadaHumano, int nivel);
}

public class GeneradorRandom implements GeneradorJugada {
    private final Random random;

    public GeneradorRandom(Random random) { this.random = random; }

    @Override public Jugada generar(Jugada jugadaHumano, int nivel) {
        Jugada[] opciones = Jugada.values();
        return opciones[random.nextInt(opciones.length)];
    }
}
```

`Dificultad` calcula el nivel a partir del récord y las probabilidades de la curva:

```java
public class Dificultad {
    public static final int PUNTOS_POR_NIVEL = 10;
    public static final int NIVEL_MAX = 5;

    public int nivel(int progreso) {
        return Math.min(progreso / PUNTOS_POR_NIVEL, NIVEL_MAX);
    }

    public double probabilidadContra(int nivel) {          // 0,00 → 0,60
        return 0.60 * proporcion(nivel);
    }

    public double probabilidadPierde(int nivel) {          // 0,65 → 0,00
        return 0.65 * (1.0 - proporcion(nivel));
    }

    private double proporcion(int nivel) {
        return (double) Math.max(0, Math.min(nivel, NIVEL_MAX)) / NIVEL_MAX;
    }
}
```

`GeneradorDificultad` **decora** a un generador de azar: recuerda la jugada anterior del
humano y, según la curva, contrarresta, se deja ganar o juega al azar.

```java
public class GeneradorDificultad implements GeneradorJugada {
    private final GeneradorJugada azar;
    private final Dificultad dificultad;
    private final Random random;

    private Jugada jugadaAnteriorHumano;

    public GeneradorDificultad(GeneradorJugada azar, Dificultad dificultad, Random random) {
        this.azar = azar;
        this.dificultad = dificultad;
        this.random = random;
    }

    @Override public Jugada generar(Jugada jugadaHumano, int nivel) {
        Jugada respuesta;
        if (jugadaAnteriorHumano == null) {                 // primera ronda: sin historial
            respuesta = azar.generar(jugadaHumano, nivel);
        } else {
            double contra = dificultad.probabilidadContra(nivel);
            double pierde = dificultad.probabilidadPierde(nivel);
            double tirada = random.nextDouble();

            if (tirada < contra) {
                respuesta = jugadaAnteriorHumano.vencedora();
            } else if (tirada < contra + pierde) {
                respuesta = jugadaAnteriorHumano.perdedora();
            } else {
                respuesta = azar.generar(jugadaHumano, nivel);
            }
        }
        jugadaAnteriorHumano = jugadaHumano;
        return respuesta;
    }
}
```

> El `Random` se **inyecta** en ambos generadores: permite semilla fija en tests o un
> `RandomFijo` para forzar cada rama del `GeneradorDificultad`.

### 3.5 `ResolutorReglas` — *Pure Fabrication / SRP*

```java
public class ResolutorReglas {
    public Resultado resolver(Jugada humano, Jugada maquina) {
        if (humano.empataCon(maquina)) return Resultado.EMPATA;
        if (humano.venceA(maquina))    return Resultado.GANA;
        return Resultado.PIERDE;
    }
}
```

### 3.6 `Partida` — *Information Expert / High Cohesion*

Guarda el estado de la sesión y aplica el marcador. **Guarda la última jugada de la máquina**
para que `getJugadaMaquina()` devuelva la jugada realmente resuelta (no una nueva al azar).

```java
public class Partida {
    private final Jugador humano;
    private final Jugador maquina;
    private final ResolutorReglas reglas;
    private final Dificultad dificultad;

    private int puntaje;
    private int puntajeMaximo;
    private int victoriasTotales;
    private Jugada ultimaJugadaMaquina;

    public Partida(Jugador humano, Jugador maquina, ResolutorReglas reglas, Dificultad dificultad) {
        this.humano = humano;
        this.maquina = maquina;
        this.reglas = reglas;
        this.dificultad = dificultad;
    }

    public Resultado jugarRonda(Jugada eleccionHumano) {
        ((JugadorHumano) humano).setJugada(eleccionHumano);
        ((JugadorMaquina) maquina).preparar(eleccionHumano, getNivel());
        ultimaJugadaMaquina = maquina.elegirJugada();
        Resultado resultado = reglas.resolver(humano.elegirJugada(), ultimaJugadaMaquina);

        switch (resultado) {
            case GANA:
                puntaje++;
                victoriasTotales++;
                if (puntaje > puntajeMaximo) puntajeMaximo = puntaje;
                break;
            case PIERDE:
                puntaje = 0;
                break;
            case EMPATA:
                break;
        }
        return resultado;
    }

    public int getNivel()            { return dificultad.nivel(victoriasTotales); }
    public int getPuntaje()          { return puntaje; }
    public int getPuntajeMaximo()    { return puntajeMaximo; }
    public int getVictoriasTotales() { return victoriasTotales; }
    public Jugada getJugadaMaquina() { return ultimaJugadaMaquina; }
}
```

> El **nivel se calcula sobre `victoriasTotales`** (contador persistente), por lo que **no baja
> al perder**. La `puntaje` (racha) y `puntajeMaximo` (récord) se conservan aparte.

---

## 4. Vista (Swing) — tema oscuro minimalista

La vista usa **emojis en lugar de imágenes**: 🪨 Piedra · 📄 Papel · ✂️ Tijera.

> **Nota:** Java2D no renderiza emoji a color; se dibujan en monocromo con la fuente
> `Segoe UI Emoji`. Esto es intencional y encaja con el estilo minimalista.

### 4.1 `Tema` — identidad visual centralizada

Concentra la paleta, las fuentes y los helpers de presentación, para no dispersar constantes
por los paneles. Los emojis/nombres viven aquí (presentación), **no** en el modelo.

```java
public final class Tema {
    public static final Color FONDO = new Color(0x1E1E1E);
    public static final Color SUPERFICIE = new Color(0x2A2A2A);
    public static final Color SUPERFICIE_HOVER = new Color(0x3A3A3A);
    public static final Color TEXTO = new Color(0xF5F5F5);
    public static final Color TEXTO_SUAVE = new Color(0x9E9E9E);
    public static final Color PRIMARIO = new Color(0x5B8DEF);
    public static final Color GANA = new Color(0x4CAF50);
    public static final Color PIERDE = new Color(0xE05C5C);
    public static final Color EMPATA = new Color(0x9E9E9E);

    public static String emoji(Jugada jugada)          { /* 🪨 / 📄 / ✂️ */ }
    public static String nombre(Jugada jugada)         { /* Piedra / Papel / Tijera */ }
    public static Color colorResultado(Resultado r)    { /* GANA/PIERDE/EMPATA */ }
    public static String mensajeResultado(Resultado r) { /* ¡Ganaste! +1 / Perdiste / Empate */ }
    public static String etiquetaNivel(int nivel)      { return "(Nivel " + nivel + ")"; }
}
```

### 4.2 `BotonRedondeado` — botón plano reutilizable

`JButton` con esquinas redondeadas dibujadas en `paintComponent`, sin borde ni foco por
defecto, cambio de color en `hover` y cursor de mano.

### 4.3 Interfaces de vista — *ISP / DIP*

```java
public interface IPanelMenu {
    void setControlador(IControlador controlador);
    void setPuntajeMaximo(int puntajeMaximo);
}

public interface IPanelJuego {
    void setControlador(IControlador controlador);
    void mostrarResultado(Resultado resultado, Jugada jugadaHumano, Jugada jugadaMaquina);
    void actualizarPuntajes(int puntaje, int puntajeMaximo, int nivel);
    void setBotonesHabilitados(boolean habilitado);   // bloquea entradas durante la pausa
    void reiniciar();                                  // limpia el resultado al entrar
}

public interface INavegador {
    String CARD_MENU = "MENU";
    String CARD_JUEGO = "JUEGO";

    void mostrar(String pantalla);
}
```

`INavegador` **abstrae la navegación**: `VentanaPrincipal` la implementa, y el controlador
depende de la interfaz, lo que permite testearlo con un doble (sin abrir ventanas).

### 4.4 `VentanaPrincipal` (JFrame) — implementa `INavegador`

Contiene un `CardLayout` con `PanelMenu` y `PanelJuego`; es la única clase que conoce ambas
pantallas. Tamaño inicial 480×380 (redimensionable) y fondo oscuro.

```java
public class VentanaPrincipal extends JFrame implements INavegador {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    public VentanaPrincipal() {
        setTitle("Piedra, Papel o Tijera");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 380);
        setLocationRelativeTo(null);
        setContentPane(contenedor);
        contenedor.setBackground(Tema.FONDO);
    }

    public void agregarPantalla(JPanel panel, String nombre) { contenedor.add(panel, nombre); }

    @Override public void mostrar(String nombre) { cardLayout.show(contenedor, nombre); }
}
```

### 4.5 `PanelMenu`

Título, fila de emojis, botón primario **Empezar juego** y **Récord**. Solo notifica al
controlador.

### 4.6 `PanelJuego`

Marcador (**`Puntaje actual (Nivel n)`** + récord), zona central con `emoji humano vs emoji
máquina` y mensaje coloreado según el `Resultado`, y tres botones grandes de emoji con
etiqueta y atajos de teclado `1`/`2`/`3`. Los botones se deshabilitan al jugar y se
rehabilitan al reiniciar. El nivel se pinta con `Tema.etiquetaNivel(...)`, en fuente chica
junto al número de puntaje.

---

## 5. Controlador — *GRASP Controller / Indirection*

```java
public interface IControlador {
    void iniciarJuego();
    void elegirJugada(Jugada jugada);
}
```

`Programador` abstrae el "ejecutar algo tras N ms", y `ProgramadorSwing` lo implementa con
`javax.swing.Timer` (sin bloquear el EDT con `Thread.sleep`). Inyectarlo hace determinista el
test de la pausa por derrota.

```java
public interface Programador {
    void enMs(int milisegundos, Runnable accion);
}

public class ProgramadorSwing implements Programador {
    @Override public void enMs(int milisegundos, Runnable accion) {
        Timer timer = new Timer(milisegundos, e -> accion.run());
        timer.setRepeats(false);
        timer.start();
    }
}
```

```java
public class JuegoController implements IControlador {
    private static final int PAUSA_DERROTA_MS = 1200;

    private final Partida partida;
    private final INavegador navegador;
    private final IPanelMenu panelMenu;
    private final IPanelJuego panelJuego;
    private final Programador programador;
    private final int pausaDerrotaMs;

    // Constructor de producción: usa PAUSA_DERROTA_MS.
    public JuegoController(Partida partida, INavegador navegador,
                           IPanelMenu panelMenu, IPanelJuego panelJuego,
                           Programador programador) {
        this(partida, navegador, panelMenu, panelJuego, programador, PAUSA_DERROTA_MS);
    }

    // Constructor con pausa inyectable (usado en tests).
    public JuegoController(Partida partida, INavegador navegador,
                           IPanelMenu panelMenu, IPanelJuego panelJuego,
                           Programador programador, int pausaDerrotaMs) {
        this.partida = partida;
        this.navegador = navegador;
        this.panelMenu = panelMenu;
        this.panelJuego = panelJuego;
        this.programador = programador;
        this.pausaDerrotaMs = pausaDerrotaMs;
        panelMenu.setControlador(this);
        panelJuego.setControlador(this);
    }

    @Override public void iniciarJuego() {
        panelJuego.reiniciar();
        panelJuego.actualizarPuntajes(partida.getPuntaje(), partida.getPuntajeMaximo(), partida.getNivel());
        panelJuego.setBotonesHabilitados(true);
        navegador.mostrar(INavegador.CARD_JUEGO);
    }

    @Override public void elegirJugada(Jugada jugada) {
        Resultado resultado = partida.jugarRonda(jugada);

        panelJuego.setBotonesHabilitados(false);
        panelJuego.actualizarPuntajes(partida.getPuntaje(), partida.getPuntajeMaximo(), partida.getNivel());
        panelJuego.mostrarResultado(resultado, jugada, partida.getJugadaMaquina());

        if (resultado == Resultado.PIERDE) {
            panelMenu.setPuntajeMaximo(partida.getPuntajeMaximo());
            programador.enMs(pausaDerrotaMs, this::volverAlMenu);
        } else {
            panelJuego.setBotonesHabilitados(true);
        }
    }

    private void volverAlMenu() {
        navegador.mostrar(INavegador.CARD_MENU);
        panelJuego.setBotonesHabilitados(true);
    }
}
```

`Main.java` compone todo (raíz de composición) y fija el `LookAndFeel` antes de crear la UI:

```java
public class Main {
    public static void main(String[] args) {
        configurarLookAndFeel();                 // Nimbus

        Random random = new Random();
        Dificultad dificultad = new Dificultad();

        Jugador humano  = new JugadorHumano("Jugador");
        Jugador maquina = new JugadorMaquina("Bot",
            new GeneradorDificultad(new GeneradorRandom(random), dificultad, random));
        Partida partida = new Partida(humano, maquina, new ResolutorReglas(), dificultad);

        VentanaPrincipal ventana = new VentanaPrincipal();
        PanelMenu panelMenu   = new PanelMenu();
        PanelJuego panelJuego = new PanelJuego();

        ventana.agregarPantalla(panelMenu,  INavegador.CARD_MENU);
        ventana.agregarPantalla(panelJuego, INavegador.CARD_JUEGO);

        new JuegoController(partida, ventana, panelMenu, panelJuego, new ProgramadorSwing());

        ventana.setVisible(true);
        ventana.mostrar(INavegador.CARD_MENU);
    }
}
```

---

## 6. Flujos del programa

### Flujo A — Inicio y menú

```
Usuario                PanelMenu           JuegoController        INavegador (Ventana)
  |                       |                      |                       |
  |-- click "Empezar" --->|-- iniciarJuego() --->|                       |
  |                       |          reiniciar() + setBotonesHabilitados(true)
  |                       |                      |-- mostrar("JUEGO") -->|
```

### Flujo B — Elección y comparación

```
Usuario           PanelJuego        JuegoController       Partida        GeneradorDificultad
  |                   |                   |                  |                  |
  |-- click Tijera -->|-- elegirJugada -->|-- jugarRonda --->|                  |
  |                   |    (PIEDRA)       |                  | preparar(nivel) ->|
  |                   |                   |                  |<- contrarresta /  |
  |                   |                   |                  |   se deja ganar / azar
  |                   |                   |                  |-- resolver() --->| ResolutorReglas
  |                   |                   |<-- Resultado ----|
```

El nivel se toma de las victorias acumuladas antes de resolver la ronda, de modo que las
primeras 10 victorias se juegan en nivel 0 (fácil).

### Flujo C — Victoria (continúa jugando)

```
Resultado = GANA
  Partida: puntaje++ ; puntajeMaximo = max(puntajeMaximo, puntaje)
  JuegoController: setBotonesHabilitados(false) → actualizarPuntajes y mostrarResultado
                   luego setBotonesHabilitados(true)
  → permanece en PanelJuego
```

### Flujo D — Derrota (pausa breve y vuelve al menú)

```
Resultado = PIERDE
  Partida: puntaje = 0
  JuegoController: panelMenu.setPuntajeMaximo(puntajeMaximo)
                   programador.enMs(1200, volverAlMenu)   // dentro: mostrar("MENU")
  → regresa al menú principal (puntaje actual en 0, récord conservado)
```

### Flujo E — Empate

```
Resultado = EMPATA
  Partida: sin cambios en el marcador
  JuegoController: actualizarPuntajes + mostrarResultado(EMPATA, ...)
  → permanece en PanelJuego
```

---

## 7. Patrones GRASP aplicados

| Patrón GRASP | Clase(s) | Justificación |
|---|---|---|
| **Controller** | `JuegoController` | Recibe los eventos de la GUI y los delega al modelo; no contiene reglas. |
| **Information Expert** | `Jugada`, `Partida` | Quien posee la información (reglas / estado) resuelve la operación. |
| **Creator** | `Main` / `Partida` | `Main` crea los objetos y los inyecta; `Partida` recibe sus jugadores ya creados. |
| **Polymorphism** | `Jugador` → `JugadorHumano` / `JugadorMaquina` | La conducta `elegirJugada()` varía según el tipo. |
| **Protected Variations** | `GeneradorJugada`, `INavegador`, `Programador` | Las variaciones (origen de la jugada, navegación, temporización) quedan tras interfaces. |
| **Low Coupling** | `model` ↔ `view` | El modelo no conoce la GUI; se comunican vía controlador e interfaces. |
| **High Cohesion** | `ResolutorReglas`, `Partida`, `Dificultad`, `Tema` | Cada clase agrupa responsabilidades estrechamente relacionadas. |
| **Indirection** | `JuegoController`, `INavegador` | Median entre vista y modelo evitando acoplamiento directo. |
| **Pure Fabrication** | `ResolutorReglas`, `GeneradorRandom`, `GeneradorDificultad`, `Programador` | Clases de servicio que no existen en el dominio pero cohesionan y desacoplan. |

---

## 8. Principios SOLID aplicados

| Principio | Dónde | Cómo |
|---|---|---|
| **S — Responsabilidad Única** | Modelo y vista | `Jugada` conoce las reglas; `ResolutorReglas` compara; `Partida` lleva el marcador; `Tema` centraliza el estilo; cada panel dibuja una pantalla. |
| **O — Abierto/Cerrado** | `GeneradorJugada`, `Jugador`, `Programador` | Se añaden generadores/tipos de jugador/temporizadores sin modificar lo existente. |
| **L — Sustitución de Liskov** | `JugadorHumano` / `JugadorMaquina` | Ambos son sustituibles como `Jugador`; el contrato `elegirJugada()` se respeta. |
| **I — Segregación de Interfaces** | `IPanelMenu`, `IPanelJuego`, `INavegador`, `IControlador` | Interfaces pequeñas y específicas; los dobles de test implementan solo lo necesario. |
| **D — Inversión de Dependencias** | `INavegador`, `Programador`, `GeneradorJugada` | `JuegoController` depende de abstracciones, no de `VentanaPrincipal` ni de `Timer`. |

---

## 9. Diagrama de clases (UML textual)

```
        +------------------+          +------------------+
        |   <<abstract>>   |          | GeneradorJugada  |  <<interface>>
        |     Jugador      |          |------------------|
        |------------------|          | + generar(Jugada,|
        | + elegirJugada() |          |       int):Jugada|
        +--------+---------+          +---------+--------+
                 ^                              |
        +--------+---------+          +---------+-------------------+
        |                  |          |  GeneradorRandom          |
+-------+-------+  +-------+--------+ |  GeneradorDificultad (usa |
| JugadorHumano |  | JugadorMaquina | |    Dificultad + Random)   |
+---------------+  +-------+--------+ +---------------------------+
                           | usa
                           v
                    +--------------+
                    |    Partida   |
                    |--------------|
                    | - puntaje    |
                    | - puntajeMaximo
                    | - ultimaJugadaMaquina
                    | + jugarRonda(Jugada): Resultado
                    +------+-------+
                           | usa
                           v
                    +--------------+       +----------------+
                    | ResolutorReglas |---->|     Jugada     |  <<enum>>
                    +--------------+       | PIEDRA/PAPEL/TIJERA
                                           | + venceA()     |
                                           | + empataCon()  |
                                           +----------------+

  <<interface>>    <<interface>>          <<interface>>     <<interface>>
  IControlador     INavegador             IPanelMenu        IPanelJuego
        ^                ^                      ^                 ^
        |                |                      |                 |
  +-----+-------------+  |            +---------+------+  +-------+--------+
  | JuegoController   |--+            |  PanelMenu     |  |  PanelJuego    |
  | - Partida         |               +----------------+  +----------------+
  | - INavegador      |                      ^                   ^
  | - Programador     |                      | implementa        | implementa
  +---------+---------+                      |                   |
            |                    +-----------+-------------------+--+
            v                    |        VentanaPrincipal (JFrame) |
     +------+-------+            |        implements INavegador     |
     | Programador  |            +----------------------------------+
     +------+-------+
            ^
     +------+--------+
     | ProgramadorSwing (Timer)
     +---------------+
```

---

## 10. Tests

Batería de **78 tests** con JUnit 5, ejecutables sin abrir ventanas (surefire corre en modo
headless: `-Djava.awt.headless=true`).

### 10.1 Ejecución

```powershell
.\mvnw.cmd test          # corre la suite
.\mvnw.cmd -q test       # salida resumida
```

El wrapper usa Maven 3.9.16. El `pom.xml` fija Java 25, JUnit Jupiter 5.11.4,
`maven-surefire-plugin 3.5.6` y `maven-compiler-plugin 3.15.0`.

### 10.2 Dobles de prueba (`src/test/java`)

| Doble | Paquete | Rol |
|---|---|---|
| `GeneradorGuion` | `model` | `GeneradorJugada` que devuelve una cola fija de jugadas → resultados deterministas en `Partida`. |
| `RandomFijo` | `model` | `Random` que siempre devuelve el mismo `nextDouble()` → fuerza cada rama del `GeneradorDificultad`. |
| `NavegadorFake` | `controller` | `INavegador` que registra la última pantalla mostrada. |
| `ProgramadorInmediato` | `controller` | `Programador` que ejecuta la acción al instante y registra el retardo. |
| `PanelMenuFake` / `PanelJuegoFake` | `controller` | Registran las llamadas de la vista (puntajes, nivel, resultado, habilitación, reinicio). |

### 10.3 Casos por clase

| Test | Cubre |
|---|---|
| `JugadaTest` | Las 9 combinaciones de `venceA`/`empataCon`, `vencedora()`/`perdedora()` e invariantes (nadie se vence a sí mismo, relación antisimétrica). |
| `ResolutorReglasTest` | Las 9 combinaciones → `GANA`/`PIERDE`/`EMPATA` (parametrizado con `@CsvSource`). |
| `DificultadTest` | `nivel` (cortes cada 10 y tope en 5), probabilidades en los extremos, monotonía y acotado fuera de rango. |
| `GeneradorRandomTest` | Siempre devuelve una `Jugada`; misma semilla → misma secuencia; con suficientes tiradas usa las tres opciones. |
| `GeneradorDificultadTest` | Primera ronda sin estrategia; contrarresta la jugada anterior; se deja ganar en nivel 0; cae al azar fuera de las franjas (con `RandomFijo`). |
| `JugadorHumanoTest` | `elegirJugada()` devuelve lo seteado; `null` sin selección. |
| `JugadorMaquinaTest` | Delega en el `GeneradorJugada` inyectado, con el contexto `(rival, nivel)`. |
| `PartidaTest` | Gana +1/actualiza máximo; pierde reinicia y conserva máximo; empate sin cambios; secuencia GANA·GANA·PIERDE → `0`/`2`; **regresión**: `getJugadaMaquina()` devuelve la jugada resuelta; el **nivel** sube cada 10 **victorias acumuladas** (aunque haya derrotas en el medio) y no baja al perder. |
| `TemaTest` | `emoji`, `nombre`, `colorResultado`, `mensajeResultado` y `etiquetaNivel`. |
| `JuegoControllerTest` | `iniciarJuego` reinicia/navega/habilita y muestra el nivel; victoria y empate actualizan y **no** navegan; derrota navega al menú tras la pausa, actualiza el récord y respeta el retardo inyectado. |

---

## 11. Convenciones de contribución

- **Nombres:** clases en `PascalCase`, métodos y variables en `camelCase`, constantes en
  `UPPER_SNAKE_CASE`.
- **Idioma del dominio:** términos del juego en español (`Jugada`, `Partida`).
- **Dependencias:** respetar la dirección `view → controller → model`; el modelo no importa
  paquetes de `view` ni `controller`.
- **Inyección:** pasar dependencias por constructor (no instanciar `new Random()`,
  `new VentanaPrincipal()` ni `new Timer()` dentro del modelo/controlador; usar las interfaces).
- **Estilo visual:** colores, fuentes y emojis van en `Tema`; los componentes Swing nuevos
  reutilizan `BotonRedondeado`.
- **Tests:** todo cambio de lógica debe venir con tests. Usar `GeneradorGuion` para
  determinismo y dobles `Fake`/`Inmediato` para aislar la vista y el tiempo. Correr
  `.\mvnw.cmd test` antes de dar por terminado un cambio.
