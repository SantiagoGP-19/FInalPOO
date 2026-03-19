package Modelo;

import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;

public class Turno {
    private int id;
    private Medico medico;
    private Paciente paciente;

    private LocalDate fecha;
    private String hora; // Usamos String para HH:mm

    private Estado estado;
    private String observaciones;

    public Turno() {}

    public Turno(int id, Medico medico, Paciente paciente, LocalDate fecha, String hora, Estado estado, String observaciones) {
        this.id = id;
        this.medico = medico;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getId() {
        return id; }

    public void setId(int id) {
        this.id = id; }

    public Medico getMedico() {
        return medico; }

    public void setMedico(Medico medico) {
        this.medico = medico; }
    public Paciente getPaciente() {
        return paciente; }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente; }

    public LocalDate getFecha() {
        return fecha; }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha; }

    public String getHora() {
        return hora; }

    public void setHora(String hora) {
        this.hora = hora; }

    public Estado getEstado() {
        return estado; }

    public void setEstado(Estado estado) {
        this.estado = estado; }

    public String getObservaciones() {
        return observaciones; }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones; }

    // Propiedad para JavaFX
    public SimpleObjectProperty<Estado> estadoProperty() {
        return new SimpleObjectProperty<>(estado);
    }
}