import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Controlador {
    private final Copa copa;
    private final Vista vista;

    public Controlador(Copa copa, Vista vista) {
        this.copa = Objects.requireNonNull(copa);
        this.vista = Objects.requireNonNull(vista);
    }

    public void iniciar() {
        boolean continuar = true;

        while (continuar) {
            try {
                int opcion = vista.mostrarMenu();

                switch (opcion) {
                    case 1 -> {
                        String codigo = vista.leerTexto("Código: ");
                        String nombre = vista.leerTexto("Nombre: ");
                        String empresa = vista.leerTexto("Empresa: ");

                        copa.registrarEquipo(codigo, nombre, empresa);

                        vista.mostrarMensaje("Equipo registrado.");
                    }

                    case 2 -> crearEncuentro();

                    case 3 -> {
                        int id = vista.leerEntero(
                                "Identificador positivo y único: ");

                        String titulo = vista.leerTexto("Título: ");

                        copa.agregarEvento(new Exhibicion(id, titulo));

                        vista.mostrarMensaje(
                                "Exhibición creada sin programar.");
                    }

                    case 4 -> {
                        vista.mostrarEventos(copa.getEventos());

                        int id = vista.leerEntero("ID del evento: ");
                        copa.buscarEvento(id);

                        LocalDate fecha = vista.leerFecha(
                                "Fecha (AAAA-MM-DD): ");

                        int hora = vista.leerEntero(
                                "Hora entera (0-23): ");

                        String sede = vista.leerTexto("Sede: ");

                        copa.programarEvento(id, fecha, hora, sede);

                        vista.mostrarMensaje("Programación guardada.");
                    }

                    case 5 -> {
                        vista.mostrarEventos(copa.getEventos());

                        int id = vista.leerEntero("ID del encuentro: ");
                        Programable evento = copa.buscarEvento(id);

                        if (evento.getEquipos().size() == 2) {
                            vista.mostrarMensaje(
                                    "Orden del marcador: "
                                            + evento.getEquipos()
                                                    .get(0).getNombre()
                                            + " / "
                                            + evento.getEquipos()
                                                    .get(1).getNombre());
                        }

                        int marcador1 = vista.leerEntero(
                                "Marcador del primer equipo: ");

                        int marcador2 = vista.leerEntero(
                                "Marcador del segundo equipo: ");

                        copa.registrarResultado(
                                id, marcador1, marcador2);

                        vista.mostrarMensaje(
                                "Resultado registrado y puntos actualizados.");
                    }

                    case 6 -> vista.mostrarTabla(
                            copa.getTablaPosiciones());

                    case 7 -> vista.mostrarEventos(copa);

                    case 8 -> vista.mostrarEventos(copa.getEventos());

                    case 0 -> {
                        continuar = false;
                        vista.mostrarMensaje("Programa finalizado.");
                    }

                    default -> vista.mostrarMensaje("Opción inválida.");
                }
            } catch (NoSuchElementException e) {
                vista.mostrarMensaje(
                        "\nEntrada cerrada. Programa finalizado.");
                continuar = false;

            } catch (IllegalArgumentException | IllegalStateException e) {
                vista.mostrarMensaje("Error: " + e.getMessage());
            }
        }
    }

    private void crearEncuentro() {
        vista.mostrarTabla(copa.getTablaPosiciones());

        int deporte = vista.leerEntero(
                "Deporte: 1. Fútbol sala / 2. Baloncesto: ");

        if (deporte != 1 && deporte != 2) {
            throw new IllegalArgumentException("Deporte inválido.");
        }

        int id = vista.leerEntero(
                "Identificador positivo y único: ");

        Equipo equipo1 = copa.buscarEquipo(
                vista.leerTexto("Código del primer equipo: "));

        Equipo equipo2 = copa.buscarEquipo(
                vista.leerTexto("Código del segundo equipo: "));

        Encuentro encuentro;

        if (deporte == 1) {
            encuentro = new FutbolSala(id, equipo1, equipo2);
        } else {
            encuentro = new Baloncesto(id, equipo1, equipo2);
        }

        copa.agregarEncuentro(encuentro);

        vista.mostrarMensaje("Encuentro creado sin programar.");
    }
}
