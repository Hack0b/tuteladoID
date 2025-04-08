package gei.id.tutelado.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cliente;
import gei.id.tutelado.model.Venta;

public class VentaDaoJPA implements VentaDao {

	private EntityManagerFactory emf; 
	private EntityManager em;
    
	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}


	/* MO4.1 */
	@Override
	public Venta recuperaPorCodigo(String codigo) {

		List<Venta> entradas= new ArrayList<>();

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			entradas = em.createNamedQuery("Venta.recuperaPorCodigo", Venta.class)
					.setParameter("codigo", codigo).getResultList();

			em.getTransaction().commit();
			em.close();
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return (entradas.isEmpty()?null:entradas.get(0));
	}

	/* MO4.2 */
	@Override
	public Venta almacena(Venta venta) {
		try {
				
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(venta);

			em.getTransaction().commit();
			em.close();
		
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return venta;
	}

	/* MO4.3 */
	@Override
	public void elimina(Venta venta) {
		try {

			em = emf.createEntityManager();
			em.getTransaction().begin();

			Venta ventaTmp = em.find (Venta.class, venta.getId());
			em.remove (ventaTmp);

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
	public Venta modifica(Venta venta) {
		try {

			em = emf.createEntityManager();		
			em.getTransaction().begin();

			venta = em.merge (venta);

			em.getTransaction().commit();
			em.close();
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return venta;
	}

	/* MO4.6.a */
	@Override
	public List<Venta> recuperaTodasCliente(Cliente client) {
		List <Venta> ventas=null;

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			ventas = em.createQuery("SELECT e FROM Cliente c JOIN c.ventas e WHERE c=:c ORDER BY e.fecha DESC", Venta.class).setParameter("c", client).getResultList();

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

		return ventas;
	}
}
