package gei.id.tutelado.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.LazyInitializationException;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cliente;


public class ClienteDaoJPA implements ClienteDao {

	private EntityManagerFactory emf; 
	private EntityManager em;

	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}


	/* MO4.1 */
	@Override
	public Cliente recuperaPorDni(String dni) {
		List <Cliente> clientes=new ArrayList<>();

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			clientes = em.createNamedQuery("Cliente.recuperaPorDni", Cliente.class).setParameter("dni", dni).getResultList();

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

		return (clientes.isEmpty()?null:clientes.get(0));
	}

	/* MO4.2 */
	@Override
	public Cliente almacena(Cliente client) {

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(client);

			em.getTransaction().commit();
			em.close();

		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return client;
	}


	/* MO4.3 */
	@Override
	public void elimina(Cliente client) {
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			Cliente clientTmp = em.find (Cliente.class, client.getId());
			em.remove (clientTmp);
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
	public Cliente modifica(Cliente client) {
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			client= em.merge (client);
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (client);
	}

	/* MO4.5 */
	@Override
	public Cliente restauraVentas(Cliente client) {
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			try {
				client.getVentas().size();
			} catch (Exception ex2) {
				if (ex2 instanceof LazyInitializationException)
				{
					client = em.merge(client);
					client.getVentas().size();
				} else {
					throw ex2;
				}
			}
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
		return (client);
	}

	/* MO4.6 b*/
	@Override
	public List<Cliente> recuperaSinVentas() {
		List <Cliente> clientes=new ArrayList<>();
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			clientes = em.createQuery("SELECT c FROM Cliente c LEFT JOIN c.ventas v WHERE v IS NULL", Cliente.class).getResultList();
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
		return clientes;
	}

	/* MO4.6 c*/
	@Override
	public List<Cliente> recuperaPrecioVehiculos(float precio) {
		List <Cliente> clientes=new ArrayList<>();
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			clientes = em.createQuery("SELECT c FROM Cliente c WHERE(SELCT AVG(ve.precio) FROM Venta v JOIN v.vehiculos ve WHERE v.cliente = c) >= :precio", Cliente.class).setParameter("precio", precio).getResultList();
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
		return clientes;
	}

	/* MO4.6 d*/
	@Override
	public List<Cliente> recuperaNumVehiculos(int numVehiculos) {
		List <Cliente> clientes=new ArrayList<>();
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			clientes = em.createQuery("SELECT c FROM Cliente c JOIN c.ventas v JOIN v.vehiculos ve GROUP BY c HAVING COUNT(ve) >= :numVehiculos", Cliente.class).setParameter("numVehiculos", numVehiculos).getResultList();
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
		return clientes;
	}
}
