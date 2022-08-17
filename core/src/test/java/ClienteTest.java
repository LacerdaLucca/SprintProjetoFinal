import com.adobe.aem.guides.wknd.core.dao.ClienteDao;
import com.adobe.aem.guides.wknd.core.models.Cliente;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ClienteTest
{
    @Test
    void AdminTeste(){

        ClienteDao mock = Mockito.mock(ClienteDao.class);
        Cliente cliente = mock.getClientes().get(0);
        Mockito.when(mock.buscaCliente(1)).thenReturn(cliente);
        Assert.assertEquals("admin", cliente.getNome());

    }
}
