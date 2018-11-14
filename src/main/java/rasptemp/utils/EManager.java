package rasptemp.utils;

import rasptemp.login.LoginAccessor;
import rasptemp.login.UsuarioAccessor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EManager implements java.io.Serializable {

    private static final Object emLock = new Object();
    private static EManager instance = null;

    private static EntityManager em = null;

    private static final Object operationLock = new Object();

    private final LoginAccessor loginAccessor;
    private final UsuarioAccessor usuarioAccessor;

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("RASPTEMP_PU");

    public EManager() {
        this.em = Persistence.createEntityManagerFactory("RASPTEMP_PU").createEntityManager();
        this.loginAccessor = new LoginAccessor(this.em, this.operationLock);
        this.usuarioAccessor = new UsuarioAccessor(this.em, this.operationLock);
    }

    public static EManager getInstance() {
        if (instance == null) {
            synchronized (emLock) {
                if (instance == null) {
                    instance = new EManager();
                }
            }
        }
        return instance;
    }

    public LoginAccessor getLoginAccessor() {
        return loginAccessor;
    }

    public UsuarioAccessor getUsuarioAccessor() {
        return usuarioAccessor;
    }
}
