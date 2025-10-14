package org.example.entidades;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum EspecialidadMedica {
    CARDIOLOGIA("Cardiología"),
    NEUROLOGIA("Neurología"),
    PEDIATRIA("Pediatría"),
    TRAUMATOLOGIA("Traumatología"),
    GINECOLOGIA("Ginecología"),
    UROLOGIA("Urología"),
    OFTALMOLOGIA("Oftalmología"),
    DERMATOLOGIA("Dermatología"),
    PSIQUIATRIA("Psiquiatría"),
    MEDICINA_GENERAL("Medicina General"),
    CIRUGIA_GENERAL("Cirugía General"),
    ANESTESIOLOGIA("Anestesiología");

    private String descripcion;

    EspecialidadMedica(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
