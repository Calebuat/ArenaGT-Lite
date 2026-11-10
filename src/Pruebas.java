import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Scanner;

public class Pruebas {
    private static int aprobadas = 0;

    public static void main(String[] args) {
        Copa inicial = new Copa();

        inicial.registrarEquipo("F", "Faro", "Norte");
        inicial.registrarEquipo("S", "Sol", "Sur");

        Equipo f = inicial.buscarEquipo("F");
        Equipo s = inicial.buscarEquipo("S");

        inicial.agregarEncuentro(new FutbolSala(1, f, s));
        inicial.agregarEncuentro(new Baloncesto(2, s, f));
        inicial.agregarEncuentro(new FutbolSala(3, f, s));

        for (int id = 1; id <= 3; id++) {
            inicial.programarEvento(
                    id, LocalDate.of(2026, 11, id), 9, "Central");
        }

        inicial.registrarResultado(1, 3, 1);
        comprobar(
                f.getPuntosTabla() == 3 && s.getPuntosTabla() == 0,
                "Victoria fútbol sala");

        inicial.registrarResultado(2, 78, 74);
        comprobar(
                f.getPuntosTabla() == 4 && s.getPuntosTabla() == 2,
                "Victoria baloncesto");

        inicial.registrarResultado(3, 0, 0);
        comprobar(
                f.getPuntosTabla() == 5 && s.getPuntosTabla() == 3,
                "Empate fútbol sala");

        Copa copa = Main.cargarDatos();
        LocalDate dia9 = LocalDate.of(2026, 11, 9);

        comprobar(
                tabla(copa).equals("F=4;S=2;L=1;N=1;"),
                "Datos iniciales y desempate");

        comprobar(
                calendario(copa).equals("1,2,5,3,4,"),
                "Orden por fecha, hora y sede");

        copa.programarEvento(5, dia9, 23, "Auditorio");
        comprobar(
                calendario(copa).equals("5,1,2,3,4,"),
                "Reprogramar exhibición");

        copa.programarEvento(4, dia9, 0, "Central");
        comprobar(
                calendario(copa).equals("4,5,1,2,3,"),
                "Reprogramar encuentro");

        copa.agregarEvento(new Exhibicion(6, "Demostración"));
        comprobar(
                calendario(copa).equals("4,5,1,2,3,"),
                "Excluir evento sin fecha");

        copa.programarEvento(4, dia9, 0, "Central");
        comprobar(
                copa.buscarEvento(4).getEstado().equals("PROGRAMADO"),
                "Conservar horario propio");

        // P10: conflicto de sede.
        rechazar(copa, () ->
                copa.programarEvento(4, dia9, 23, "Auditorio"));

        // P11: conflicto de equipo en otra sede.
        rechazar(copa, () ->
                copa.programarEvento(
                        4, LocalDate.of(2026, 11, 10), 9, "Otra"));

        // P12: código vacío.
        rechazar(copa, () ->
                copa.registrarEquipo(" ", "Otro", "Empresa"));

        // P13: código duplicado normalizado.
        rechazar(copa, () ->
                copa.registrarEquipo(" f ", "Otro", "Empresa"));

        // P14: equipo contra sí mismo.
        rechazar(copa, () ->
                copa.agregarEncuentro(new FutbolSala(
                        7, copa.buscarEquipo("F"), copa.buscarEquipo("F"))));

        // P15: empate en baloncesto.
        rechazar(copa, () -> copa.registrarResultado(4, 20, 20));

        // P16: marcador negativo.
        rechazar(copa, () -> copa.registrarResultado(4, -1, 20));

        // P17 y P18: horas fuera de rango.
        rechazar(copa, () ->
                copa.programarEvento(6, dia9, -1, "Sur"));

        rechazar(copa, () ->
                copa.programarEvento(6, dia9, 24, "Sur"));

        copa.agregarEncuentro(new FutbolSala(
                7, copa.buscarEquipo("F"), copa.buscarEquipo("S")));

        // P19: resultado sin programación.
        rechazar(copa, () -> copa.registrarResultado(7, 1, 0));

        // P20: segundo resultado.
        rechazar(copa, () -> copa.registrarResultado(1, 1, 0));

        // P21: reprogramar finalizado.
        rechazar(copa, () ->
                copa.programarEvento(1, dia9, 8, "Otra"));

        // P22: resultado en exhibición.
        rechazar(copa, () -> copa.registrarResultado(5, 1, 0));

        // P23: sede vacía.
        rechazar(copa, () ->
                copa.programarEvento(6, dia9, 8, " "));

        // P24: fecha nula.
        rechazar(copa, () ->
                copa.programarEvento(6, null, 8, "Sur"));

        // P25: identificador duplicado.
        rechazar(copa, () ->
                copa.agregarEvento(new Exhibicion(5, "Otro")));

        // P26: evento inexistente.
        rechazar(copa, () ->
                copa.programarEvento(99, dia9, 8, "Sur"));

        // P27: identificador negativo.
        rechazar(copa, () ->
                copa.agregarEvento(new Exhibicion(-3, "Otro")));

        // P28: nombre vacío.
        rechazar(copa, () ->
                copa.registrarEquipo("X", " ", "Empresa"));

        // P29: empresa vacía.
        rechazar(copa, () ->
                copa.registrarEquipo("X", "Otro", " "));

        // P30: título vacío.
        rechazar(copa, () ->
                copa.agregarEvento(new Exhibicion(8, " ")));

        // P31: equipo ajeno a la copa.
        rechazar(copa, () ->
                copa.agregarEncuentro(new FutbolSala(
                        8,
                        copa.buscarEquipo("F"),
                        new Equipo("X", "Externo", "Empresa"))));

        // P32: exhibición en sede ocupada por un encuentro.
        rechazar(copa, () ->
                copa.programarEvento(6, dia9, 0, " central "));

        // P33: equipo inexistente.
        rechazar(copa, () -> copa.buscarEquipo("X"));

        // P34: entradas numéricas y fechas inválidas.
        String anterior = estado(copa);

        capturar(() -> {
            Vista vista = new Vista(new Scanner(
                    "abc\n2.5\n2147483648\n12\n"
                            + "2026-02-30\nmal\n2026-11-15\n"));

            if (vista.leerEntero("") != 12
                    || !vista.leerFecha("").equals(
                            LocalDate.of(2026, 11, 15))) {

                throw new AssertionError(
                        "La lectura no validó las entradas.");
            }
        });

        comprobar(
                anterior.equals(estado(copa)),
                "Enteros y fechas inválidas");

        // P35: el iterador no permite eliminar.
        Iterator<Programable> iterador = copa.iterator();
        iterador.next();

        boolean protegido = false;

        try {
            iterador.remove();
        } catch (UnsupportedOperationException e) {
            protegido = true;
        }

        comprobar(
                protegido && anterior.equals(estado(copa)),
                "Iterador protegido");

        // P36: victoria del segundo equipo.
        copa.registrarResultado(4, 10, 20);

        comprobar(
                tabla(copa).equals("F=6;L=2;S=2;N=1;"),
                "Victoria del segundo equipo");

        // P37: transición a FINALIZADO.
        comprobar(
                copa.buscarEvento(4).getEstado().equals("FINALIZADO"),
                "Estado finalizado");

        // P38: menú completo con entradas incorrectas.
        Copa menu = Main.cargarDatos();
        String antesMenu = estado(menu);

        String salida = capturar(() ->
                new Controlador(
                        menu,
                        new Vista(new Scanner(
                                "abc\n99\n4\n4\n2026-02-30\n"
                                        + "2026-11-15\n24\nCentral\n"
                                        + "6\n7\n0\n")))
                        .iniciar());

        comprobar(
                antesMenu.equals(estado(menu))
                        && salida.contains("Opción inválida")
                        && salida.contains("La fecha no existe")
                        && salida.contains("entre 0 y 23")
                        && salida.contains("TABLA DE POSICIONES")
                        && salida.contains("ID 5")
                        && salida.contains("Programa finalizado"),
                "Menú resistente a errores");

        System.out.println(
                aprobadas + " casos aprobados y 0 fallos.");
    }

    private static void comprobar(
            boolean condicion, String descripcion) {

        if (!condicion) {
            throw new AssertionError(
                    "Falló P" + (aprobadas + 1) + ": " + descripcion);
        }

        System.out.printf(
                "P%02d\tOK\t%s%n", ++aprobadas, descripcion);
    }

    private static void rechazar(Copa copa, Runnable operacion) {
        String anterior = estado(copa);
        boolean rechazado = false;

        try {
            operacion.run();
        } catch (IllegalArgumentException | IllegalStateException e) {
            rechazado = true;
        }

        comprobar(
                rechazado && anterior.equals(estado(copa)),
                "Rechazo sin alterar datos");
    }

    private static String tabla(Copa copa) {
        StringBuilder texto = new StringBuilder();

        for (Equipo equipo : copa.getTablaPosiciones()) {
            texto.append(equipo.getCodigo())
                    .append('=')
                    .append(equipo.getPuntosTabla())
                    .append(';');
        }

        return texto.toString();
    }

    private static String calendario(Copa copa) {
        StringBuilder texto = new StringBuilder();

        for (Programable evento : copa) {
            texto.append(evento.getId()).append(',');
        }

        return texto.toString();
    }

    private static String estado(Copa copa) {
        StringBuilder texto = new StringBuilder(tabla(copa));

        for (Programable evento : copa.getEventos()) {
            texto.append('|').append(evento.getId())
                    .append('|').append(evento.getDescripcion())
                    .append('|').append(evento.getFecha())
                    .append('|').append(evento.getHora())
                    .append('|').append(evento.getSede())
                    .append('|').append(evento.getEstado());
        }

        return texto.toString();
    }

    private static String capturar(Runnable operacion) {
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (PrintStream temporal = new PrintStream(
                bytes, true, StandardCharsets.UTF_8)) {

            System.setOut(temporal);
            operacion.run();

        } finally {
            System.setOut(original);
        }

        return bytes.toString(StandardCharsets.UTF_8);
    }
}
