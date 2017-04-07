package com.gitlab.projects;


import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Armen Arzumanyan
 */
@ManagedBean(name = "gitlabUserContext")
@SessionScoped
public class GitlabUserContext implements Serializable {

    private static final long serialVersionUID = 5497292482670000840L;
    
    @Setter
    @Getter
    private SystenUser user = new SystenUser();
  

    public GitlabUserContext() {
      
    }    

}
