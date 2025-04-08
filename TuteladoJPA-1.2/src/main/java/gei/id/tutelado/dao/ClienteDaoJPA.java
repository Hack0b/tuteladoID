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



}
