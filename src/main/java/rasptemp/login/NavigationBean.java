package rasptemp.login;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {

    public String redirectToLogin() {
        return "/login.xhtml?faces-redirect=true";
    }

    public static String redirectToAdmin() {
        return "/admin/index.xhtml?faces-redirect=true";
    }

    public static String toAdmin() {
        return "/admin/index.xhtml";
    }
}
