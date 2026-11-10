public class Baloncesto extends Encuentro {
    public Baloncesto(int id, Equipo equipo1, Equipo equipo2) {
        super(id, equipo1, equipo2);
    }

    @Override
    protected void validarReglasDeporte(
            int marcador1, int marcador2) {

        if (marcador1 == marcador2) {
            throw new IllegalArgumentException(
                    "En baloncesto no se permiten empates.");
        }
    }

    @Override
    protected int puntosPorMarcador(int propio, int rival) {
        return propio > rival ? 2 : 1;
    }

    @Override
    public String getDescripcion() {
        String texto = "Baloncesto: "
                + getEquipo1().getNombre()
                + " vs "
                + getEquipo2().getNombre();

        if ("FINALIZADO".equals(getEstado())) {
            texto += " | Marcador: "
                    + getMarcador1()
                    + " - "
                    + getMarcador2();
        }

        return texto;
    }
}
