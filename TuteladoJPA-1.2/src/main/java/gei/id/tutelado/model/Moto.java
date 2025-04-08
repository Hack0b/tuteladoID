package gei.id.tutelado.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@DiscriminatorValue("BIKE")
@PrimaryKeyJoinColumn (name = "id_Vehiculo")
public class Moto extends Vehiculo{
    
    @Column(nullable = false, unique = true)
    private int cilindrada;

    @Column(nullable = false, unique=false)
    private String tipoMotor;

    @Column(nullable = false, unique = false)
    private boolean baul;

    public int getCilindrada() {
        return cilindrada;
    }

    public String getTipoMotor() {
        return tipoMotor;
    }

    public boolean isBaul() {
        return baul;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    public void setTipoMotor(String tipoMotor) {
        this.tipoMotor = tipoMotor;
    }

    public void setBaul(boolean baul) {
        this.baul = baul;
    }

}
