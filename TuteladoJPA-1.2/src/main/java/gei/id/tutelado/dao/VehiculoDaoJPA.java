package gei.id.tutelado.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Vehiculo;
import gei.id.tutelado.model.Venta;


public class VehiculoDaoJPA implements VehiculoDao {

	private EntityManagerFactory emf; 
	private EntityManager em;

	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}


	/* MO4.1 */
	@Override
	public Vehiculo recuperaPorMatricula(String matricula) {
		List <Vehiculo> vehiculos=new ArrayList<>();

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			vehiculos = em.createNamedQuery("Vehiculo.recuperaPorMatricula", Vehiculo.class).setParameter("matricula", matricula).getResultList();

			em.getTransaction().commit();
			em.close();

		}
		catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}

		return (vehiculos.isEmpty()?null:vehiculos.get(0));
	}

	/* MO4.2 */
	@Override
	public Vehiculo almacena(Vehiculo vehiculo) {

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(vehiculo);

			em.getTransaction().commit();
			em.close();

		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return vehiculo;
	}


	/* MO4.3 */
	@Override
	public void elimina(Vehiculo vehiculo) {
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Vehiculo vehiculoTmp = em.find (Vehiculo.class, vehiculo.getId());
			List<Venta> ventas = em.createQuery("SELECT v FROM Venta v JOIN v.vehiculos ve WHERE ve = :vehiculo", Venta.class).setParameter("vehiculo", vehiculoTmp).getResultList();
			for (Venta venta : ventas) {
				venta.getVehiculos().remove(vehiculoTmp);
				em.merge(venta); // Actualizar la relación en la base de datos
			}
			em.remove (vehiculoTmp);

			em.getTransaction().commit();
			em.close();
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
	}

	/* MO4.4 */
	@Override
	public Vehiculo modifica(Vehiculo vehiculo) {

		try {

			em = emf.createEntityManager();
			em.getTransaction().begin();

			vehiculo= em.merge (vehiculo);

			em.getTransaction().commit();
			em.close();

		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (vehiculo);
	}
}
