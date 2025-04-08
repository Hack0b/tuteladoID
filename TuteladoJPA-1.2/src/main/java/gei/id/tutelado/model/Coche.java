package gei.id.tutelado.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@DiscriminatorValue("CAR")
@PrimaryKeyJoinColumn (name = "id_Vehiculo")
public class Coche extends Vehiculo{

    @Column(nullable = false, unique = true)
    private int num_puertas;

    @Column(nullable = false, unique=false)
    private String combustible;

    @Column(nullable = false, unique = false)
    private float capacidad_maletero;

    @ElementCollection
    @CollectionTable(name = "coche_extras", joinColumns = @JoinColumn(name = "id_vehiculo"))
    @Column(name = "extra")
    private Set<String> extras = new HashSet<>();





}
