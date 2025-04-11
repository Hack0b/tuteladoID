package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.ClienteDao;
import gei.id.tutelado.dao.ClienteDaoJPA;
import gei.id.tutelado.dao.VentaDao;
import gei.id.tutelado.dao.VentaDaoJPA;
import gei.id.tutelado.dao.VehiculoDao;
import gei.id.tutelado.dao.VehiculoDaoJPA;
import gei.id.tutelado.model.Cliente;
import gei.id.tutelado.model.Venta;
import gei.id.tutelado.model.Vehiculo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.Exception;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P04_Consultas {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProdutorDatosProba produtorDatos = new ProdutorDatosProba();
    
    private static Configuracion cfg;
    private static ClienteDao clienteDao;
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

    	clienteDao = new ClienteDaoJPA();
    	ventaDao = new VentaDaoJPA();
    	clienteDao.setup(cfg);
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
    public void test08_INNER_JOIN() {
        produtorDatos.creaClientesConVentas();
        produtorDatos.vc1A.setFecha(LocalDateTime.of(2024, 1, 1, 10, 0));
        produtorDatos.vc1B.setFecha(LocalDateTime.of(2024, 3, 1, 10, 0)); 
        produtorDatos.gravaClientes();

        List<Venta> ventas = ventaDao.recuperaTodasCliente(produtorDatos.c1);

        assertEquals(2, ventas.size());

        // Mirar que este en orden
        assertTrue(ventas.get(0).getFecha().isAfter(ventas.get(1).getFecha()));

        for (Venta v : ventas) {
            assertEquals(produtorDatos.c1, v.getCliente());
        }
    }

    @Test
    public void test08_OUTER_JOIN() {
        produtorDatos.creaClientesConVentas();
        produtorDatos.gravaClientes();
        List<Cliente> resultado = clienteDao.recuperaSinVentas();
        assertEquals(1, resultado.size());
    }

	@Test
    public void test08_Subconsulta() {

        produtorDatos.creaClientesConVentas();
        produtorDatos.creaVehiculosSueltos();
        produtorDatos.vc1A.añadirVehiculo(produtorDatos.ve1); // 18000
        produtorDatos.vc1A.añadirVehiculo(produtorDatos.ve3); // 20000
        produtorDatos.gravaClientes();

        List<Cliente> resultado = clienteDao.recuperaPrecioVehiculos(Double.valueOf(18500));
        assertEquals(1, resultado.size());  // Precio medio = 19000
        assertTrue(resultado.contains(produtorDatos.c1));
    }

    @Test
    public void test08_Agregacion() {
        produtorDatos.creaClientesConVentas();
        produtorDatos.creaVehiculosSueltos();
        produtorDatos.vc1A.añadirVehiculo(produtorDatos.ve1);
        produtorDatos.vc1A.añadirVehiculo(produtorDatos.ve2);
        produtorDatos.vc1B.añadirVehiculo(produtorDatos.ve3);
        produtorDatos.gravaClientes();

        List<Cliente> resultado = clienteDao.recuperaNumVehiculos(Long.valueOf(3));
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(produtorDatos.c1));
    }

}