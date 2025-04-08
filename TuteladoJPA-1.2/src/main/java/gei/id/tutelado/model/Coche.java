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
    @CollectionTable(name = "coche_extras", joinColumns = @JoinColumn(name = "id_vehiculo")) //El name en el JoinCoulmn no se si tiene que ser igual a la clave primaria de la tabla Vehiculo
    @Column(name = "extra")
    private Set<String> extras = new HashSet<>();

    public int getNum_puertas() {
        return num_puertas;
    }

    public String getCombustible() {
        return combustible;
    }

    public float getCapacidad_maletero() {
        return capacidad_maletero;
    }

    public Set<String> getExtras() {
        return extras;
    }

    public void setNum_puertas(int num_puertas) {
        this.num_puertas = num_puertas;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public void setCapacidad_maletero(float capacidad_maletero) {
        this.capacidad_maletero = capacidad_maletero;
    }

    public void setExtras(Set<String> extras) {
        this.extras = extras;
    }



}
