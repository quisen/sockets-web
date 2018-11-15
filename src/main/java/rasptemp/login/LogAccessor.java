package rasptemp.login;

import java.util.List;
import javax.persistence.EntityManager;

public class LogAccessor {

    private final EntityManager manager;
    private final Object operationLock;

    public LogAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }
}
