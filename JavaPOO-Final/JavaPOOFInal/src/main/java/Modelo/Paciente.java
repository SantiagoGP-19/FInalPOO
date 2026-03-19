package Modelo;

public class Paciente extends Persona {
    private String obraSocial;
    private String numeroAfiliado;

    public Paciente() {
        super();
    }

    public Paciente(int id, String nombre, String apellido, String dni, String mail, String telefono, String obraSocial, String numeroAfiliado) {
        super(id, nombre, apellido, dni, mail, telefono);
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
    }

    public String getObraSocial() {
        return obraSocial; }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial; }

    public String getNumeroAfiliado() {
        return numeroAfiliado; }

    public void setNumeroAfiliado(String numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado; }

    @Override
    public String toString() {
        return super.toString();
    }
}