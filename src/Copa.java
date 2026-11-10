import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Copa implements Iterable<Programable> {
    private final ArrayList<Equipo> equipos;
    private final ArrayList<Programable> eventos;
    private final ArrayList<Puntuable> puntuables;

    public Copa() {
        equipos = new ArrayList<>();
        eventos = new ArrayList<>();
        puntuables = new ArrayList<>();
    }

    public void registrarEquipo(
            String codigo, String nombre, String empresa) {

        Equipo nuevo = new Equipo(codigo, nombre, empresa);

        for (Equipo equipo : equipos) {
            if (equipo.getCodigo().equals(nuevo.getCodigo())) {
                throw new IllegalArgumentException(
                        "Ya existe un equipo con ese código.");
            }
        }

        equipos.add(nuevo);
    }

    public Equipo buscarEquipo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El código no puede estar vacío.");
        }

        String normalizado = codigo.trim().toUpperCase(Locale.ROOT);

        for (Equipo equipo : equipos) {
            if (equipo.getCodigo().equals(normalizado)) {
                return equipo;
            }
        }

        throw new IllegalArgumentException(
                "No existe el equipo con código " + normalizado + ".");
    }

    public void agregarEvento(Programable evento) {
        if (evento == null || evento.getId() <= 0) {
            throw new IllegalArgumentException(
                    "El evento debe tener un identificador positivo.");
        }

        for (Programable existente : eventos) {
            if (existente.getId() == evento.getId()) {
                throw new IllegalArgumentException(
                        "Ya existe un evento con ese identificador.");
            }
        }

        for (Equipo equipo : evento.getEquipos()) {
            if (!equipos.contains(equipo)) {
                throw new IllegalArgumentException(
                        "Todos los participantes deben pertenecer a la copa.");
            }
        }

        eventos.add(evento);
    }

    public void agregarEncuentro(Encuentro encuentro) {
        agregarEvento(encuentro);

        // Se guarda otra referencia al mismo objeto.
        puntuables.add(encuentro);
    }

    public Programable buscarEvento(int id) {
        for (Programable evento : eventos) {
            if (evento.getId() == id) {
                return evento;
            }
        }

        throw new IllegalArgumentException(
                "No existe el evento " + id + ".");
    }

    public void programarEvento(
            int id, LocalDate fecha, int hora, String sede) {

        Programable elegido = buscarEvento(id);
        elegido.validarProgramacion(fecha, hora, sede);

        String sedeLimpia = sede.trim();

        for (Programable otro : eventos) {
            if (otro == elegido
                    || !fecha.equals(otro.getFecha())
                    || hora != otro.getHora()) {
                continue;
            }

            if (sedeLimpia.equalsIgnoreCase(otro.getSede())) {
                throw new IllegalArgumentException(
                        "La sede está ocupada por el evento "
                                + otro.getId() + ".");
            }

            for (Equipo equipo : elegido.getEquipos()) {
                if (otro.getEquipos().contains(equipo)) {
                    throw new IllegalArgumentException(
                            "El equipo " + equipo.getNombre()
                                    + " ya participa en el evento "
                                    + otro.getId() + ".");
                }
            }
        }

        // Solo se cambia la agenda después de revisar los conflictos.
        elegido.programar(fecha, hora, sedeLimpia);
    }

    public void registrarResultado(
            int id, int marcador1, int marcador2) {

        buscarEvento(id);

        for (Puntuable puntuable : puntuables) {
            if (puntuable.getId() == id) {
                puntuable.registrarResultado(marcador1, marcador2);
                return;
            }
        }

        throw new IllegalArgumentException(
                "Ese evento no admite resultados.");
    }

    public List<Programable> getEventos() {
        return List.copyOf(eventos);
    }

    public List<Equipo> getTablaPosiciones() {
        ArrayList<Equipo> tabla = new ArrayList<>(equipos);
        Collections.sort(tabla);

        return List.copyOf(tabla);
    }

    @Override
    public Iterator<Programable> iterator() {
        ArrayList<Programable> calendario = new ArrayList<>();

        for (Programable evento : eventos) {
            if (evento.getFecha() != null) {
                calendario.add(evento);
            }
        }

        Collections.sort(calendario);

        return List.copyOf(calendario).iterator();
    }
}
