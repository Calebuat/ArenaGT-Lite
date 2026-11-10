import java.util.Locale;

public class Equipo implements Comparable<Equipo> {
    private final String codigo;
    private final String nombre;
    private final String empresa;
    private int puntosTabla;

    public Equipo(String codigo, String nombre, String empresa) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El código no puede estar vacío.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede estar vacío.");
        }

        if (empresa == null || empresa.isBlank()) {
            throw new IllegalArgumentException(
                    "La empresa no puede estar vacía.");
        }

        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
        this.empresa = empresa.trim();
        this.puntosTabla = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public int getPuntosTabla() {
        return puntosTabla;
    }

    void sumarPuntos(int puntos) {
        if (puntos < 0
                || (long) puntosTabla + puntos > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Cantidad de puntos inválida.");
        }

        puntosTabla += puntos;
    }

    @Override
    public int compareTo(Equipo otro) {
        int comparacion = Integer.compare(
                otro.puntosTabla, puntosTabla);

        if (comparacion != 0) {
            return comparacion;
        }

        return nombre.compareToIgnoreCase(otro.nombre);
    }
}
