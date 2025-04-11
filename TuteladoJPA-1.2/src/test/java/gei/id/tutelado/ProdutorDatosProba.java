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

	public Coche ve1, ve3;
    public Moto ve2, ve4;
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
        this.ve1 = new Coche();
        this.ve1.setMatricula("1234ABC");
        this.ve1.setMarca("Toyota");
        this.ve1.setModelo("Corolla");
        this.ve1.setColor("Negro");
        this.ve1.setPrecio(18000);
        this.ve1.setNum_puertas(5);
        this.ve1.setCombustible("Gasolina");
        this.ve1.setCapacidad_maletero(400);

        this.ve3 = new Coche();
        this.ve3.setMatricula("9999ABV");
        this.ve3.setMarca("Honda");
        this.ve3.setModelo("Civic");
        this.ve3.setColor("Azul");
        this.ve3.setPrecio(20000);
        this.ve3.setNum_puertas(3);
        this.ve3.setCombustible("Diesel");
        this.ve3.setCapacidad_maletero(350);

        this.ve2 = new Moto();
        this.ve2.setMatricula("2345BCD");
        this.ve2.setMarca("Honda");
        this.ve2.setModelo("CB1000R");
        this.ve2.setColor("Rojo");
        this.ve2.setPrecio(6000);
        this.ve2.setCilindrada(998);
        this.ve2.setTipoMotor("Motor de dos tiempos");
        this.ve2.setBaul(false);

        this.ve4 = new Moto();
        this.ve4.setMatricula("9999BCD");
        this.ve4.setMarca("Ducati");
        this.ve4.setModelo("DesertX");
        this.ve4.setColor("Blanco");
        this.ve4.setPrecio(9000);
        this.ve4.setCilindrada(937 );
        this.ve4.setTipoMotor("Bicilíndrico en V a 90º");
        this.ve4.setBaul(true);

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
			
			em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idCliente'" ).executeUpdate();
			em.createNativeQuery("UPDATE taboa_ids SET ultimo_valor_id=0 WHERE nome_id='idVehiculo'" ).executeUpdate();

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