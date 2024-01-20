
package com.Servicios.HogarExpert.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter @Getter
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_comentario;
    
    @OneToOne
    private Proveedor proveedor;
    @OneToOne
    private Trabajo trabajo;
 
    private String mensaje;
    private Integer calificacion;

    public Comentario() {
    }

    public Comentario(Long id_comentario, String mensaje, Integer calificacion) {
        this.id_comentario = id_comentario;
        this.mensaje = mensaje;
        this.calificacion = calificacion;
    }
    
    
    
    
}