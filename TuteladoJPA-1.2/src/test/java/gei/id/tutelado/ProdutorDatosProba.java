package gei.id.tutelado;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.*;

public class ProdutorDatosProba {


	// Crea un conxunto de obxectos para utilizar nos casos de proba
	
	private EntityManagerFactory emf=null;
	
	public Cliente c1, c2;
	public List<Cliente> listaClientes;
	
	public Venta vc1A, vc1B;
	public List<Venta> listaVentas;

	public Vehiculo ve1, ve2, ve3, ve4;
	public List<Vehiculo> listaVehiculos;
	
	public void Setup (Configuracion config) {
		this.emf=(EntityManagerFactory) config.get("EMF");
	}
	
	public void creaClientesSueltos() {
		this.c1 = new Cliente();
        this.c1.setDni("000A");
        this.c1.setNombre("Usuario uno");
		this.c1.setDireccion("Calle de ejemplo 1");
		this.c1.setTelefono(123456789);

        this.c2 = new Cliente();
        this.c2.setDni("111B");
        this.c2.setNombre("Usuario dos");
		this.c2.setDireccion("Calle de ejemplo 2");
		this.c2.setTelefono(987654321);

        this.listaClientes = new ArrayList<Cliente> ();
        this.listaClientes.add(0,c1);
        this.listaClientes.add(1,c2);        
	}
	
	public void creaVentasSueltas() {
        this.vc1A = new Venta();
        this.vc1A.setCodigo("028408AG83V97");;
        this.vc1A.setFecha(LocalDateTime.now());

        this.vc1B = new Venta();
        this.vc1B.setCodigo("028408AG83V98");;
        this.vc1B.setFecha(LocalDateTime.now());

        this.listaVentas = new ArrayList<Venta> ();
        this.listaVentas.add(0,vc1A);
        this.listaVentas.add(1,vc1B);
    }

	public void creaVehiculosSueltos() {
        Coche tmp1 = new Coche();
        tmp1.setMatricula("1234ABC");
        tmp1.setMarca("Toyota");
        tmp1.setModelo("Corolla");
        tmp1.setColor("Negro");
        tmp1.setPrecio(18000);
        tmp1.setNum_puertas(5);
        tmp1.setCombustible("Gasolina");
        tmp1.setCapacidad_maletero(400);
		ve1 = tmp1;

        Coche tmp3 = new Coche();
        tmp3.setMatricula("9999ABV");
        tmp3.setMarca("Honda");
        tmp3.setModelo("Civic");
        tmp3.setColor("Azul");
        tmp3.setPrecio(20000);
        tmp3.setNum_puertas(3);
        tmp3.setCombustible("Diesel");
        tmp3.setCapacidad_maletero(350);
		ve3 = tmp3;

        Moto tmp2 = new Moto();
        tmp2.setMatricula("2345BCD");
        tmp2.setMarca("Honda");
        tmp2.setModelo("CB1000R");
    	tmp2.setColor("Rojo");
        tmp2.setPrecio(6000);
        tmp2.setCilindrada(998);
        tmp2.setTipoMotor("Motor de dos tiempos");
        tmp2.setBaul(false);
		ve2 = tmp2;

        Moto tmp4 = new Moto();
        tmp4.setMatricula("9999BCD");
        tmp4.setMarca("Ducati");
        tmp4.setModelo("DesertX");
        tmp4.setColor("Blanco");
        tmp4.setPrecio(9000);
        tmp4.setCilindrada(937 );
        tmp4.setTipoMotor("Bicilíndrico en V a 90º");
        tmp4.setBaul(true);
		ve4 = tmp4;

        this.listaVehiculos = new ArrayList<Vehiculo> ();
        this.listaVehiculos.add(0,ve1);
        this.listaVehiculos.add(1,ve2);
        this.listaVehiculos.add(2,ve3);
        this.listaVehiculos.add(3,ve4);
    }
	
	public void creaClientesConVentas () {
        this.creaClientesSueltos();
        this.creaVentasSueltas();
        this.c1.anadirVenta(vc1A);
        this.c1.anadirVenta(vc1B);
    }

	public void creaVentasConVehiculos () {
		this.creaClientesConVentas();
		this.creaVehiculosSueltos();
        this.vc1A.añadirVehiculo(this.ve1);
        this.vc1A.añadirVehiculo(this.ve2);
		this.vc1A.añadirVehiculo(this.ve3);
		this.vc1B.añadirVehiculo(this.ve3);
		this.vc1B.añadirVehiculo(this.ve4);
	}
	
	public void gravaClientes() {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Iterator<Cliente> itC = this.listaClientes.iterator();
			while (itC.hasNext()) {
				Cliente c = itC.next();
				em.persist(c);
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}	
	}
	
	public void limpaBD () {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			
			Iterator <Cliente> itC = em.createQuery("SELECT c from Cliente c", Cliente.class).getResultList().iterator();
			while (itC.hasNext()) em.remove(itC.next());

			Iterator <Vehiculo> itVe = em.createQuery("SELECT ve from Vehiculo ve", Vehiculo.class).getResultList().iterator();
			while (itVe.hasNext()) em.remove(itVe.next());		
			
			em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idCliente'" ).executeUpdate();
			em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idVehiculo'" ).executeUpdate();

			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}
	}
	
}