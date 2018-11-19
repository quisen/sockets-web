package rasptemp.login;

import java.util.List;
import javax.persistence.EntityManager;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

public class LogAccessor {

    private final EntityManager manager;
    private final Object operationLock;

    public LogAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }

    public List<Log> getLogs() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Log.findByTempRecent").setHint(QueryHints.REFRESH, HintValues.TRUE).getResultList();
        }
    }

    public void insertLog(Log l) {
        synchronized (this.operationLock) {
            try {
                this.manager.getTransaction().begin();
                this.manager.persist(l);
                this.manager.getTransaction().commit();
            } catch (Exception e) {
                this.manager.getTransaction().rollback();
            }
        }
    }
}
