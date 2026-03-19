package Modelo;

public class Persona {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String mail;
    private String telefono;

    public Persona() {}

    public Persona(int id, String nombre, String apellido, String dni, String mail, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.mail = mail;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getId() {
        return id; }

    public void setId(int id) {
        this.id = id; }

    public String getNombre() {
        return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getApellido() {
        return apellido; }

    public void setApellido(String apellido) {
        this.apellido = apellido; }

    public String getDni() {
        return dni; }

    public void setDni(String dni) {
        this.dni = dni; }

    public String getMail() {
        return mail; }

    public void setMail(String mail) {
        this.mail = mail; }

    public String getTelefono() {
        return telefono; }

    public void setTelefono(String telefono) {
        this.telefono = telefono; }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}