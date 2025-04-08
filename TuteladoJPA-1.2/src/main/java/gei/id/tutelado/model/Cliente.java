package gei.id.tutelado.model;

import javax.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;

@TableGenerator(name="generadorIdsCliente", table="tabla_ids",
pkColumnName="nombre_id", pkColumnValue="idCliente",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

// @NamedQueries ({
// 	@NamedQuery (name="Usuario.recuperaPorNif",
// 				 query="SELECT u FROM Usuario u where u.nif=:nif")
// })

@Entity
public class Cliente {
    @Id
    @GeneratedValue (generator="generadorIdsCliente")
    private Long id;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false, unique=false)
    private String nombre;

    @Column(nullable = false, unique = false)
    private String direccion;

    @Column(nullable = false, unique=false)
    private int telefono;
     
    //No sabemos que poner en el merge, si ON delete Cascade o no
    @OneToMany (mappedBy="cliente", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE} )
    @OrderBy("fecha ASC")
    private SortedSet<Venta> ventas = new TreeSet<>();
    // NOTA: necesitamos @OrderBy, ainda que a colección estea definida como LAZY, por se nalgun momento accedemos á propiedade DENTRO de sesión.
    // Garantimos así que cando Hibernate cargue a colección, o faga na orde axeitada na consulta que lanza contra a BD

    public Long getId() {
		return id;
	}

	public String getdni() {
		return dni;
	}

	public String getNombre() {
		return nombre;
	}

    public String getDireccion() {
		return direccion;
	}

	public int getTelefono() {
		return telefono;
	}

	public SortedSet<Venta> getVentas() {
		return ventas;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public void setNome(String nombre) {
		this.nombre = nombre;
	}

    public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public void setVentas(SortedSet<Venta> ventas) {
		this.ventas = ventas;
	}
	
	// Metodo de conveniencia para asegurarnos de que actualizamos los dos extremos de la asociación al mismo tiempo
	public void anadirVenta(Venta venta) {
		if (venta.getCliente() != null) throw new RuntimeException ("");
		venta.setCliente(this);
		// Al ser sorted set, añadir por orden de datos (ascendente)
		this.ventas.add(venta);
	}

	// Metodo de conveniencia para asegurarnos de que actualizamos los dos extremos de la asociación al mismo tiempo
	public void eliminarVenta(Venta venta) {
		if (!this.ventas.contains(venta)) throw new RuntimeException ("");
		if (!venta.getCliente().equals(this)) throw new RuntimeException ("");
		venta.setCliente(null);
		this.ventas.remove(venta);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
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
		Cliente other = (Cliente) obj;
		if (dni == null) {
            return other.dni == null;
        } else if (!dni.equals(other.dni))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", dni=" + dni + ", nombre=" + nombre +  ", direccion=" + direccion + ", telefono=" + telefono + "]";
	}

    



    
}
