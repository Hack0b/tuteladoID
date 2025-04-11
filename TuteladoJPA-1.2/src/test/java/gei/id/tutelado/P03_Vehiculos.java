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
import gei.id.tutelado.dao.VehiculoDao;
import gei.id.tutelado.dao.VehiculoDaoJPA;
import gei.id.tutelado.dao.VentaDao;
import gei.id.tutelado.dao.VentaDaoJPA;
import gei.id.tutelado.model.Vehiculo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P03_Vehiculos {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();
    
    private static Configuracion cfg;
    private static ClienteDao clienteDao;
    private static VentaDao ventaDao;
    private static VehiculoDao vehiculoDao;

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

    	clienteDao = new ClienteDaoJPA();
    	ventaDao = new VentaDaoJPA();
		vehiculoDao = new VehiculoDaoJPA();
    	clienteDao.setup(cfg);
    	ventaDao.setup(cfg);
    	vehiculoDao.setup(cfg);

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
		log.info("Limpiando BD -----------------------------------------------------------------------------------------------------");
		produtorDatos.limpaBD();
	}

	@After
	public void tearDown() throws Exception {
	}	
	
    @Test 
    public void test01_Recuperacion() {
   	
    	Vehiculo ve;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaVentasConVehiculos();
    	produtorDatos.gravaClientes();


		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de la recuperación (por matricula) de vehiculos sueltos\n"   
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación por matricula existente\n"
		+ "\t\t\t\t b) Recuperacion por matricula inexistente\n");     	

		log.info("Probando recuperacion por matricula EXISTENTE --------------------------------------------------");

    	ve = vehiculoDao.recuperaPorMatricula(produtorDatos.ve1.getMatricula());

    	Assert.assertEquals (produtorDatos.ve1.getMatricula(),				ve.getMatricula());
    	Assert.assertEquals (produtorDatos.ve1.getModelo(),					ve.getModelo());
    	Assert.assertEquals (produtorDatos.ve1.getMarca(),					ve.getMarca());
    	Assert.assertEquals (produtorDatos.ve1.getColor(),					ve.getColor());
    	Assert.assertTrue(produtorDatos.ve1.getPrecio() == ve.getPrecio());

    	log.info("");	
		log.info("Probando recuperacion por codigo INEXISTENTE --------------------------------------------------");
    	
    	ve = vehiculoDao.recuperaPorMatricula("iwbvyhuebvuwebvi");
    	Assert.assertNull (ve);

    } 	

    @Test
    public void test02_Alta() {


    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaClientesSueltos();
		produtorDatos.creaVentasSueltas();
    	produtorDatos.gravaClientes();
    	produtorDatos.creaVehiculosSueltos();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de la gravación de vehiculos sueltos\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Primer vehiculo vindulado a una venta\n"
    			+ "\t\t\t\t b) Nuevo vehiculo para una venta con vehiculos previos\n");     	

    	produtorDatos.vc1A.añadirVehiculo(produtorDatos.ve1);
		
    	log.info("");
		log.info("Gravando primer vehiculo de un cliente --------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.ve1.getId());
    	vehiculoDao.almacena(produtorDatos.ve1);
    	Assert.assertNotNull(produtorDatos.ve1.getId());

    	produtorDatos.vc1A.añadirVehiculo(produtorDatos.ve2);

    	log.info("");	
		log.info("Gravando segundo vehiculo de un usuario ---------------------------------------------------------------------");
    	Assert.assertNull(produtorDatos.ve2.getId());
    	vehiculoDao.almacena(produtorDatos.ve2);
    	Assert.assertNotNull(produtorDatos.ve2.getId());

    }

    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

    	produtorDatos.creaVentasConVehiculos();
    	produtorDatos.gravaClientes();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de eliminación de vehiculo suelto (asignado a cliente)\n");

		Assert.assertNotNull(vehiculoDao.recuperaPorMatricula(produtorDatos.ve1.getMatricula()));
    	vehiculoDao.elimina(produtorDatos.ve1);    	
		Assert.assertNull(vehiculoDao.recuperaPorMatricula(produtorDatos.ve1.getMatricula()));
    } 	
    
    @Test 
    public void test04_Modificacion() {

    	Vehiculo ve1, ve2;
    	float nuevoPrecio;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");
  
		produtorDatos.creaVentasConVehiculos();
    	produtorDatos.gravaClientes();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de modificación de la información de un vehiculo suelto\n");
 
		nuevoPrecio = 17000;

		ve1 = vehiculoDao.recuperaPorMatricula(produtorDatos.ve1.getMatricula());

		Assert.assertNotEquals(nuevoPrecio, ve1.getPrecio());
    	ve1.setPrecio(nuevoPrecio);

    	vehiculoDao.modifica(ve1);    	
    	
		ve2 = vehiculoDao.recuperaPorMatricula(produtorDatos.ve1.getMatricula());
		Assert.assertTrue(nuevoPrecio == ve2.getPrecio());
    }

/*
	@Test
	public void test07_Propagacion_Persist() {


		log.info("");
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosSoltos();
		produtorDatos.creaEntradasLogSoltas();
		produtorDatos.u1.engadirEntradaLog(produtorDatos.e1A);
		produtorDatos.u1.engadirEntradaLog(produtorDatos.e1B);


		log.info("");
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
		log.info("Obxectivo: Proba da gravación de novo usuario con entradas (novas) de log asociadas\n");

		// Situación de partida:
		// u1, e1A, e1B transitorios

		Assert.assertNull(produtorDatos.u1.getId());
		Assert.assertNull(produtorDatos.e1A.getId());
		Assert.assertNull(produtorDatos.e1B.getId());

		log.info("Gravando na BD usuario con entradas de log ----------------------------------------------------------------------");

		// Aqui o persist sobre u1 debe propagarse a e1A e e1B
		usuDao.almacena(produtorDatos.u1);

		Assert.assertNotNull(produtorDatos.u1.getId());
		Assert.assertNotNull(produtorDatos.e1A.getId());
		Assert.assertNotNull(produtorDatos.e1B.getId());
	}
*/

/*
	@Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		produtorDatos.creaUsuariosSoltos();
		produtorDatos.gravaUsuarios();
		produtorDatos.creaEntradasLogSoltas();		
		produtorDatos.u1.engadirEntradaLog(produtorDatos.e1A);		
		logDao.almacena(produtorDatos.e1A);
		
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violacion de restricions not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravación de entrada con usuario nulo\n"
    			+ "\t\t\t\t b) Gravación de entrada con codigo nulo\n"
    			+ "\t\t\t\t c) Gravación de entrada con codigo duplicado\n");

    	// Situación de partida:
    	// u0, u1 desligados
    	// e1A desligado, e1B transitorio (e sen usuario asociado)
    	
		log.info("Probando gravacion de entrada con usuario nulo ------------------------------------------------------------------");
    	try {
    		logDao.almacena(produtorDatos.e1B);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    	// Ligar entrada a usuario para poder probar outros erros
		produtorDatos.u1.engadirEntradaLog(produtorDatos.e1B);
    	    	
    	log.info("");	
		log.info("Probando gravacion de entrada con codigo nulo -------------------------------------------------------------------");
		produtorDatos.e1B.setCodigo(null);
    	try {
        	logDao.almacena(produtorDatos.e1B);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    	log.info("");	
		log.info("Probando gravacion de entrada con codigo duplicado --------------------------------------------------------------");
		produtorDatos.e1B.setCodigo(produtorDatos.e1A.getCodigo());
    	try {
        	logDao.almacena(produtorDatos.e1B);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    } 	
*/
}