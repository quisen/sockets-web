package rasptemp.login;

import java.util.List;
import javax.persistence.EntityManager;

public class LoginAccessor {

    private final EntityManager manager;
    private final Object operationLock;

    public LoginAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }

    public List<Usuario> getUsuariosByLoginSenha(String login, String senha) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findByLoginSenha", Usuario.class).setParameter("login", login).setParameter("senha", senha).getResultList();
        }
    }

    public List<Usuario> getUsuariosLogin(String username) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findByLogin", Usuario.class).setParameter("login", username).getResultList();
        }
    }
}
