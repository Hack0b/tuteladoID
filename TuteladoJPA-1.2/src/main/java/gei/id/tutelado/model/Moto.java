package gei.id.tutelado.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@DiscriminatorValue("BIKE")
@PrimaryKeyJoinColumn (name = "id_Vehiculo")
public class Moto extends Vehiculo{
    
}
