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
    private String selectedRole;

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

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public String authorize() {
        FacesContext context = FacesContext.getCurrentInstance();

        if ("admin".equals(login) && "123123".equals(password) && "ADMIN".equals(selectedRole)) {
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.setAttribute("user", login);
            session.setAttribute("role", selectedRole);
            System.out.println("Redirecting to adminHome.xhtml");
            return "admin/adminHome.xhtml?faces-redirect=true";
        } else if ("user".equals(login) && "123123".equals(password) && "USER".equals(selectedRole)) {
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.setAttribute("user", login);
            session.setAttribute("role", selectedRole);
            System.out.println("Redirecting to Home.xhtml");
            return "user/home.xhtml?faces-redirect=true";
        } else {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ошибка авторизации",
                            "Неверный логин, пароль или роль"));
            return null;
        }
    }


    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "/index.xhtml?faces-redirect=true";
    }

}
