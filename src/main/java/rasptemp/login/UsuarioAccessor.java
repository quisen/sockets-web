package rasptemp.login;

import java.util.List;
import javax.persistence.EntityManager;

public class UsuarioAccessor {

    private final EntityManager manager;
    private final Object operationLock;

    public UsuarioAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }

    public List<Usuario> getUsuarios() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findAll").getResultList();
        }
    }
}
