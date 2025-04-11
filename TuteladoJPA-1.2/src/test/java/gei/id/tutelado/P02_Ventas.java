package gei.id.tutelado;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
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
import gei.id.tutelado.dao.VentaDao;
import gei.id.tutelado.dao.VentaDaoJPA;
import gei.id.tutelado.model.Cliente;
import gei.id.tutelado.model.Venta;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P02_Ventas {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();
    
    private static Configuracion cfg;
    private static ClienteDao clientDao;
    private static VentaDao ventaDao;
    
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

    	clientDao = new ClienteDaoJPA();
    	ventaDao = new VentaDaoJPA();
    	clientDao.setup(cfg);
    	ventaDao.setup(cfg);
    	
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
		log.info("Limpando BD -----------------------------------------------------------------------------------------------------");
		produtorDatos.limpaBD();
	}

	@After
	public void tearDown() throws Exception {
	}	
	
    @Test 
    public void test01_Recuperacion() {
   	
    	Venta v;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaClientesConVentas();
    	produtorDatos.gravaClientes();


		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de la recuperación (por codigo) de ventas\n"
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación por codigo existente\n"
		+ "\t\t\t\t b) Recuperacion por codigo inexistente\n");     	

    	// Situación de partida:
    	// u1, e1A, e1B desligados
    	
		log.info("Probando recuperacion por codigo EXISTENTE --------------------------------------------------");

    	v = ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo());

    	Assert.assertEquals (produtorDatos.vc1A.getCodigo(),     v.getCodigo());
		Assert.assertEquals (produtorDatos.vc1A.getFecha().truncatedTo(ChronoUnit.SECONDS),   v.getFecha().truncatedTo(ChronoUnit.SECONDS));

    	log.info("");	
		log.info("Probando recuperacion por codigo INEXISTENTE --------------------------------------------------");
    	
    	v = ventaDao.recuperaPorCodigo("iwbvyhuebvuwebvi");
    	Assert.assertNull (v);

    } 	

    @Test
    public void test02_Alta() {


    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaClientesSueltos();
    	produtorDatos.gravaClientes();
    	produtorDatos.creaVentasSueltas();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de la gravación de ventas sueltas\n"
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Primera venta vinculada a un cliente\n"
    			+ "\t\t\t\t b) Nueva venta para un usuario con ventas\n");

    	// Situación de partida:
    	// u1 desligado    	
    	// e1A, e1B transitorios

    	produtorDatos.c1.anadirVenta(produtorDatos.vc1A);
		
    	log.info("");	
		log.info("Gravando primera venta de un cliente --------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.vc1A.getId());
    	ventaDao.almacena(produtorDatos.vc1A);
    	Assert.assertNotNull(produtorDatos.vc1A.getId());

    	produtorDatos.c1.anadirVenta(produtorDatos.vc1B);

    	log.info("");	
		log.info("Gravando segunda venta de un usuario ---------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.vc1B.getId());
    	ventaDao.almacena(produtorDatos.vc1B);
    	Assert.assertNotNull(produtorDatos.vc1B.getId());

    }

    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

    	produtorDatos.creaClientesConVentas();
    	produtorDatos.gravaClientes();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de eliminación de venta suelta (asignada a cliente)\n");
    	
    	// Situación de partida:
    	// e1A desligado

		Assert.assertNotNull(ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo()));
    	ventaDao.elimina(produtorDatos.vc1A);
		Assert.assertNull(ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo()));

    } 	
    
    @Test 
    public void test04_Modificacion() {

    	Venta v1, v2;
    	LocalDateTime nuevaFecha;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");
  
		produtorDatos.creaClientesConVentas();
    	produtorDatos.gravaClientes();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Prueba de modificación de la información de una entrada de log suelta\n");
 
    	
    	// Situación de partida:
    	// e1A desligado
    	
		nuevaFecha = LocalDateTime.of(2025, 4, 11, 19, 0);

		v1 = ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo());

		Assert.assertNotEquals(nuevaFecha, v1.getFecha());
    	v1.setFecha(nuevaFecha);

    	ventaDao.modifica(v1);
    	
		v2 = ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo());
		Assert.assertEquals (v1.getFecha().truncatedTo(ChronoUnit.SECONDS), v2.getFecha().truncatedTo(ChronoUnit.SECONDS));


    }

	
	@Test
	public void test05_LAZY() {

		Cliente c;
		Venta v;
		Boolean excepcion;

		log.info("");
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaClientesConVentas();
		produtorDatos.gravaClientes();

		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
		log.info("Objetivo: Pueoba da recuperación de propiedades LAZY\n"
				+ "\t\t\t\t Casos contemplados:\n"
				+ "\t\t\t\t a) Recuperación de cliente con colección (LAZY) de ventas \n"
				+ "\t\t\t\t b) Carga forzada de colección LAZY de dicha coleccion\n"
				+ "\t\t\t\t c) Recuperacion de venta suelta con referencia (EAGER) a usuario\n");

		// Situación de partida:
		// u1, e1A, e1B desligados

		log.info("Probando (excepcion tras) recuperacion LAZY ---------------------------------------------------------------------");

		c = clientDao.recuperaPorDni(produtorDatos.c1.getdni());
		log.info("Acceso a ventas de cliente");
		try	{
			Assert.assertEquals(2, c.getVentas().size());
			Assert.assertEquals(produtorDatos.vc1A, c.getVentas().first());
			Assert.assertEquals(produtorDatos.vc1B, c.getVentas().last());
			excepcion=false;
		} catch (LazyInitializationException ex) {
			excepcion=true;
			log.info(ex.getClass().getName());
		};
		Assert.assertTrue(excepcion);

		log.info("");
		log.info("Probando carga forzada de coleccion LAZY ------------------------------------------------------------------------");

		c = clientDao.recuperaPorDni(produtorDatos.c1.getdni());
		c = clientDao.restauraVentas(c);

		Assert.assertEquals(2, c.getVentas().size());
		Assert.assertEquals(produtorDatos.vc1A, c.getVentas().first());
		Assert.assertEquals(produtorDatos.vc1B, c.getVentas().last());

		log.info("");
		log.info("Probando acceso a referencia EAGER ------------------------------------------------------------------------------");

		v = ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo());
		Assert.assertEquals(produtorDatos.c1, v.getCliente());
}

	@Test
    public void test06_EAGER() {
    	
    	Venta v;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaClientesConVentas();
    	produtorDatos.gravaClientes();

		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de la recuperación de propiedades EAGER\n");   

		log.info("Probando (que no hay excepcion tras) acceso inicial a propiedade EAGER fuera de sesion ----------------------------------------");
    	
    	v = ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo());  
		log.info("Acceso a cliente de venta");
    	try	{
        	Assert.assertEquals(produtorDatos.c1, v.getCliente());
        	excepcion=false;
    	} catch (LazyInitializationException ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertFalse(excepcion);    
    }

	@Test
	public void test07_Propagacion_Remove() {

		log.info("");
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaClientesConVentas();
		produtorDatos.gravaClientes();

		log.info("");
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
		log.info("Objetivo: Prueba de eliminación de cliente con ventas asociadas\n");

		Assert.assertNotNull(clientDao.recuperaPorDni(produtorDatos.c1.getdni()));
		Assert.assertNotNull(ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo()));
		Assert.assertNotNull(ventaDao.recuperaPorCodigo(produtorDatos.vc1B.getCodigo()));
		
		clientDao.elimina(produtorDatos.c1);

		Assert.assertNull(clientDao.recuperaPorDni(produtorDatos.c1.getdni()));
		Assert.assertNull(ventaDao.recuperaPorCodigo(produtorDatos.vc1A.getCodigo()));
		Assert.assertNull(ventaDao.recuperaPorCodigo(produtorDatos.vc1B.getCodigo()));

	}

}