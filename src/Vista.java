import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Vista {
    private final Scanner entrada;

    public Vista(Scanner entrada) {
        this.entrada = Objects.requireNonNull(entrada);
    }

    public int mostrarMenu() {
        mostrarMensaje("\n=== ARENAGT LITE ===");
        mostrarMensaje("1. Registrar equipo");
        mostrarMensaje("2. Crear encuentro");
        mostrarMensaje("3. Crear exhibición");
        mostrarMensaje("4. Programar o reprogramar evento");
        mostrarMensaje("5. Registrar resultado");
        mostrarMensaje("6. Consultar tabla de posiciones");
        mostrarMensaje("7. Consultar calendario ordenado");
        mostrarMensaje("8. Consultar todos los eventos");
        mostrarMensaje("0. Salir");

        return leerEntero("Opción: ");
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);

            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                mostrarMensaje(
                        "Ingrese un entero entre -2147483648 y "
                                + "2147483647, sin decimales.");
            }
        }
    }

    public LocalDate leerFecha(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);

            try {
                if (!texto.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    mostrarMensaje(
                            "Use una fecha real con formato AAAA-MM-DD.");
                    continue;
                }

                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                mostrarMensaje(
                        "La fecha no existe. Use el formato AAAA-MM-DD.");
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarTabla(List<Equipo> tabla) {
        mostrarMensaje("\n=== TABLA DE POSICIONES ===");

        if (tabla.isEmpty()) {
            mostrarMensaje("No hay equipos registrados.");
            return;
        }

        System.out.printf(
                "%-5s %-10s %-22s %-24s %s%n",
                "Pos.", "Código", "Equipo", "Empresa", "Puntos");

        int posicion = 1;

        for (Equipo equipo : tabla) {
            System.out.printf(
                    "%-5d %-10s %-22s %-24s %d%n",
                    posicion++,
                    equipo.getCodigo(),
                    equipo.getNombre(),
                    equipo.getEmpresa(),
                    equipo.getPuntosTabla());
        }
    }

    public void mostrarEventos(Iterable<Programable> eventos) {
        boolean hayEventos = false;

        for (Programable evento : eventos) {
            hayEventos = true;

            mostrarMensaje(
                    "\nID " + evento.getId()
                            + " | " + evento.getDescripcion());

            mostrarMensaje("Estado: " + evento.getEstado());

            if (evento.getFecha() == null) {
                mostrarMensaje("Sin fecha, hora ni sede asignadas.");
            } else {
                System.out.printf(
                        "Fecha: %s | Hora: %02d:00 | Sede: %s%n",
                        evento.getFecha(),
                        evento.getHora(),
                        evento.getSede());
            }
        }

        if (!hayEventos) {
            mostrarMensaje("No hay eventos para mostrar.");
        }
    }
}
