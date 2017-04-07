package com.gitlab.projects;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.gitlab.api.GitlabAPI;

/**
 *
 * @author armdev
 */
@ManagedBean(eager = false)
@AllArgsConstructor
@SessionScoped
public class ApplicationManager implements Serializable {

    //  private static final String PRIVATE_TOKEN = "thwqLRVTrgLkT5EWM6nz"; //private tokens
    private static final long serialVersionUID = -1736046614659566905L;

    @ManagedProperty("#{gitlabUserContext}")
    private GitlabUserContext gitlabUserContext = null;

    @ManagedProperty("#{i18n}")
    @Setter
    private ResourceBundle bundle = null;

    private GitlabAPI gitlabAPI;
    private GitlapConnection conn = new GitlapConnection();

    public ApplicationManager() {

    }

    @PostConstruct
    public void init() {
      //  System.out.println("gitlabUserContext.getUser().getUrl() " +gitlabUserContext.getUser().getUrl());
        //System.out.println("gitlabUserContext.getUser().getPrivateToken() " +gitlabUserContext.getUser().getPrivateToken());
        gitlabAPI = conn.getApi(gitlabUserContext.getUser().getUrl(), gitlabUserContext.getUser().getPrivateToken());
    }

    public GitlabAPI getGitlabAPI() {
        return gitlabAPI;
    }

    public void setGitlabUserContext(GitlabUserContext gitlabUserContext) {
        this.gitlabUserContext = gitlabUserContext;
    }

}
