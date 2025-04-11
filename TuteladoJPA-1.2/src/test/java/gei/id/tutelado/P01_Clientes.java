package gei.id.tutelado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.ClienteDao;
import gei.id.tutelado.dao.ClienteDaoJPA;
import gei.id.tutelado.model.Cliente;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P01_Clientes {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();
    
    private static Configuracion cfg;
    private static ClienteDao cliDao;
    
    @Rule
    public TestRule watcher = new TestWatcher() {
       protected void starting(Description description) {
    	   log.info("");
    	   log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    	   log.info("Iniciando test: " + description.getMethodName());
    	   log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
       }
       protected void finished(Description description) {
    	   log.info("");
    	   log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
    	   log.info("Finalizado test: " + description.getMethodName());
    	   log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
       }
    };
    
    @BeforeClass
    public static void init() throws Exception {
    	cfg = new ConfiguracionJPA();
    	cfg.start();

    	cliDao = new ClienteDaoJPA();
    	cliDao.setup(cfg);
    	
    	produtorDatos = new ProdutorDatosProba();
    	produtorDatos.Setup(cfg);
    }
    
    @AfterClass
    public static void endclose() throws Exception {
    	cfg.endUp();    	
    }
    
	@Before
	public void setUp() throws Exception {		
		log.info("");	
		log.info("Limpiando BD --------------------------------------------------------------------------------------------");
		produtorDatos.limpaBD();
	}

	@After
	public void tearDown() throws Exception {
	}
	
    @Test
    public void test01_Recuperacion() {
    	
    	Cliente c;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaClientesSueltos();
    	produtorDatos.gravaClientes();
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objectivo: Prueba de recuperación desde la BD de cliente (sin ventas asociadas) por dni\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperación por dni existente\n"
    			+ "\t\t\t\t b) Recuperacion por dni inexistente\n");

    	// Situación de partida:
    	// u0 desligado    	

    	log.info("Probando recuperacion por dni EXISTENTE --------------------------------------------------");

    	c = cliDao.recuperaPorDni(produtorDatos.c1.getdni());
    	Assert.assertEquals(produtorDatos.c1.getdni(),      c.getdni());
    	Assert.assertEquals(produtorDatos.c1.getNombre(),     c.getNombre());
    	Assert.assertEquals(produtorDatos.c1.getDireccion(), c.getDireccion());
		Assert.assertEquals(produtorDatos.c1.getTelefono(),  c.getTelefono());

    	log.info("");	
		log.info("Probando recuperacion por dni INEXISTENTE -----------------------------------------------");
    	
    	c = cliDao.recuperaPorDni("iwbvyhuebvuwebvi");
    	Assert.assertNull (c);
    } 	

    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situación de partida ddel test -----------------------------------------------------------------------");
  
		produtorDatos.creaClientesSueltos();
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Obetivo: Prueba de gravación en la BD de nuevo cliente (sin ventas asociadas)\n");
    	
    	Assert.assertNull(produtorDatos.c1.getId());
    	cliDao.almacena(produtorDatos.c1);   	
    	Assert.assertNotNull(produtorDatos.c1.getId());
    }
    
    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaClientesSueltos();
    	produtorDatos.gravaClientes();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de eliminación de la BD de cliente sin ventas asociadas\n");   
 
    	Assert.assertNotNull(cliDao.recuperaPorDni(produtorDatos.c1.getdni()));
    	cliDao.elimina(produtorDatos.c1);    	
    	Assert.assertNull(cliDao.recuperaPorDni(produtorDatos.c1.getdni()));
    } 	

    @Test 
    public void test04_Modificacion() {
    	
    	Cliente c1, c2;
    	String nuevoNombre;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaClientesSueltos();
    	produtorDatos.gravaClientes();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de modificación de la información básica de un cliente sin ventas\n");

		nuevoNombre = new String ("Nombre nuevo");

		c1 = cliDao.recuperaPorDni(produtorDatos.c1.getdni());
		Assert.assertNotEquals(nuevoNombre, c1.getNombre());
    	c1.setNombre(nuevoNombre);

    	cliDao.modifica(c1);    	
    	
		c2 = cliDao.recuperaPorDni(produtorDatos.c1.getdni());
		Assert.assertEquals (nuevoNombre, c2.getNombre());

    } 	
    /*
    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosSoltos();
    	usuDao.almacena(produtorDatos.u0);
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violación de restricións not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravación de usuario con nif duplicado\n"
    			+ "\t\t\t\t b) Gravación de usuario con nif nulo\n");

    	// Situación de partida:
    	// u0 desligado, u1 transitorio
    	
		log.info("Probando gravacion de usuario con Nif duplicado -----------------------------------------------");
    	produtorDatos.u1.setNif(produtorDatos.u0.getNif());
    	try {
        	usuDao.almacena(produtorDatos.u1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	// Nif nulo
    	log.info("");	
		log.info("Probando gravacion de usuario con Nif nulo ----------------------------------------------------");
    	produtorDatos.u1.setNif(null);
    	try {
        	usuDao.almacena(produtorDatos.u1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    } 	
 */
}