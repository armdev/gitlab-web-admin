package com.gitlab.projects;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Armen Arzumanyan
 */
@ManagedBean(name = "userLogout")
@RequestScoped
public class UserLogout implements Serializable {   

    private static final long serialVersionUID = 6747076553894990200L;

    private FacesContext context = null;
    private ExternalContext externalContext = null;

    public UserLogout() {
    }

    public String doLogout() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();      
        externalContext.getSessionMap().remove("applicationManager");       
        externalContext.getSessionMap().remove("gitlabUserContext");    
        externalContext.getSessionMap().remove("gitlabUserContext");    
        externalContext.getSessionMap().clear();
        HttpSession session = (HttpSession) externalContext.getSession(true);
        session.invalidate();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "logout";
    }
}
