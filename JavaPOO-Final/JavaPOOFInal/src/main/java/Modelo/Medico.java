package Modelo;

public class Medico extends Persona {
    private String matricula;
    private String especialidad;

    public Medico() {
        super();
    }

    public Medico(int id, String nombre, String apellido, String dni, String mail, String telefono, String matricula, String especialidad) {
        super(id, nombre, apellido, dni, mail, telefono);
        this.matricula = matricula;
        this.especialidad = especialidad;
    }

    public String getMatricula() {
        return matricula; }

    public void setMatricula(String matricula) {
        this.matricula = matricula; }

    public String getEspecialidad() {
        return especialidad; }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad; }

    @Override
    public String toString() {
        return super.toString() + " (" + especialidad + ")";
    }
}