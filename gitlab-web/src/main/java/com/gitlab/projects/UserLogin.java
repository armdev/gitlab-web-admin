package com.gitlab.projects;

import java.util.Optional;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author armdev
 */
@ManagedBean(name = "userLogin")
@RequestScoped
public class UserLogin {

    @ManagedProperty("#{mongoConnection}")
    @Setter
    private MongoConnection mongoConnection = null;
    @ManagedProperty("#{i18n}")
    @Setter
    private ResourceBundle bundle = null;
    @ManagedProperty("#{gitlabUserContext}")
    @Setter
    private GitlabUserContext gitlabUserContext = null;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String privateToken;

    @Setter
    @Getter
    private String gitlabUrl;

    public UserLogin() {
    }

    public String loginUser() {
        Optional<SystenUser> user = mongoConnection.login(username, privateToken);
        if (user.isPresent() && user.get().getUsername() != null) {
            gitlabUserContext.setUser(user.get());          
            return "dashboard";
        }
        FacesMessage msg = new FacesMessage(bundle.getString("nouser"), bundle.getString("nouser"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return null;
    }

    public String register() {
        SystenUser user = mongoConnection.findByUsername(username);
        if (user != null && user.getUsername() != null) {
            FacesMessage msg = new FacesMessage(bundle.getString("usernameused"), bundle.getString("usernameused"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        } else {
            SystenUser newUser = new SystenUser();
            newUser.setPrivateToken(privateToken);
            newUser.setUsername(username);
            newUser.setUrl(gitlabUrl);
            newUser = mongoConnection.save(newUser);
            gitlabUserContext.setUser(newUser);
            return "dashboard";
        }

    }

}
