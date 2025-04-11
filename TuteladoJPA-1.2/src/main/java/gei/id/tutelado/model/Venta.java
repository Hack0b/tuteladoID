package gei.id.tutelado.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

@TableGenerator(name="generadorIdsVenta", table="tabla_ids",
pkColumnName="nombre_id", pkColumnValue="idVenta",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Venta.recuperaPorCodigo",
				 query="SELECT v FROM Venta v where v.codigo=:codigo")
})

@Entity
public class Venta implements Comparable<Venta> {
    @Id
    @GeneratedValue(generator="generadorIdsVenta")
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(unique=false, nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(cascade={}, fetch=FetchType.EAGER)
    @JoinColumn (nullable=false, unique=false)
    private Cliente cliente;

    @ManyToMany(cascade={CascadeType.PERSIST})
    @JoinTable(name = "t_vehiculo_venta",
        joinColumns = @JoinColumn(name = "id_venta"),
        inverseJoinColumns = @JoinColumn(name = "id_vehiculo"))
    private Set<Vehiculo> vehiculos = new HashSet<Vehiculo>();

	public Long getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public Cliente getCliente() {
		return cliente;
	}

    public Set<Vehiculo> getVehiculos() {
        return vehiculos;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

    public void setVehiculos(Set<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

	public void añadirVehiculo(Vehiculo vehiculo) {
        this.vehiculos.add(vehiculo);
    }

    public void eliminarVehiculo(Vehiculo vehiculo) {
        this.vehiculos.remove(vehiculo);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Venta other = (Venta) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Venta [id=" + id + ", codigo=" + codigo + ", fecha=" + fecha + "]";
	}

	@Override
	public int compareTo(Venta other) {
		return (this.fecha.isBefore(other.getFecha())? -1:1);
	}

    

}
