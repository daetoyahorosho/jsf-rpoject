package com.example.jsfrpoject;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class AuthorizeBean implements Serializable {

    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Метод проверки данных пользователя
     * @return
     */
    public String authorize() {
        FacesContext context = FacesContext.getCurrentInstance();
        User user = DbUtils.getUserByLogin(login);

        if (user != null && user.getPassword().equals(password)) {
            // Если логин и пароль совпадают, устанавливаем роль в сессию
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.setAttribute("user", login);
            session.setAttribute("role", user.getRole());

            if ("ADMIN".equals(user.getRole())) {
                System.out.println("Redirecting to admin.xhtml");
                return "/admin/admin.xhtml?faces-redirect=true";
            } else {
                System.out.println("Redirecting to admin.xhtml");
                return "/user/home.xhtml?faces-redirect=true";
            }
        } else {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ошибка авторизации",
                            "Неверный логин или пароль"));
            return null;
        }
    }

    /**
     * Метод закрытия сессии пользователя
     * @return
     */
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "/index.xhtml?faces-redirect=true";
    }
}
