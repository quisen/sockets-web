package rasptemp.login;

import rasptemp.utils.EManager;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import javax.faces.context.ExternalContext;

@ManagedBean
@SessionScoped
public class LoginManager implements Serializable {

    private String login;
    private String senha;
    private static Usuario usuarioAtivo = new Usuario();

    private static boolean loggedIn;

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;

    public void doLogin() throws IOException {
        List<Usuario> users = EManager.getInstance().getLoginAccessor().getUsuariosByLoginSenha(login, senha);
        // Login sucesso
        if (users.size() > 0) {
            List<Usuario> loggedOnUsers = EManager.getInstance().getLoginAccessor().getUsuariosLogin(login);
            usuarioAtivo = users.get(0);

            loggedIn = true;
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/admin/index.xhtml");
        } else {
            popupMessage_DadosIncorretos();
        }
    }

    public void doLogout() throws IOException {
        loggedIn = false;
        this.senha = "";
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/login.xhtml");
    }

    public static void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void adicionarMensagem(FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(detail, message);
    }

    public void popupMessage_DadosIncorretos() {
        adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Nome de usuário ou senha incorretos", "");
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public static Usuario getUsuarioAtivo() {
        return usuarioAtivo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
