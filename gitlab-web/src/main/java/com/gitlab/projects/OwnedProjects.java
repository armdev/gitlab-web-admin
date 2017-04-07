package com.gitlab.projects;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javaslang.control.Try;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.gitlab.api.models.GitlabProject;

@ManagedBean
@AllArgsConstructor
@ViewScoped
public class OwnedProjects implements Serializable {

    private static final long serialVersionUID = 410099901232573667L;

    @ManagedProperty("#{applicationManager}")
    @Setter
    private ApplicationManager applicationManager;
    @Setter
    @Getter
    private List<GitlabProject> ownedProjectList;
    @ManagedProperty("#{cacheHandler}")
    @Setter
    private CacheHandler cacheHandler;
    @ManagedProperty("#{i18n}")
    @Setter
    private ResourceBundle bundle = null;
    @ManagedProperty("#{gitlabUserContext}")
    @Setter
    private GitlabUserContext gitlabUserContext = null;

    public OwnedProjects() {
    }

    @PostConstruct
    public void init() {
        ownedProjectList = (List<GitlabProject>) cacheHandler.getGitlabProjectListCache(gitlabUserContext.getUser().getUsername());

    }

    public void doCall() {
        ownedProjectList = (List<GitlabProject>) cacheHandler.getGitlabProjectListCache(gitlabUserContext.getUser().getUsername());
        if (ownedProjectList == null) {
            applicationManager.init();
            Try<List> projectList = this.fetchProjectList();
            if (projectList.isSuccess()) {
                ownedProjectList = projectList.get();
                cacheHandler.putGitlabProjectListCache(gitlabUserContext.getUser().getUsername(), ownedProjectList);
            } else if (projectList.isFailure()) {
               // System.out.println("1 " +projectList.toString());
                FacesMessage msg = new FacesMessage(bundle.getString("connectionError"), bundle.getString("connectionError"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else if (projectList.isEmpty()) {
                //System.out.println("2");
                FacesMessage msg = new FacesMessage(bundle.getString("connectionError"), bundle.getString("connectionError"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else if (projectList.isSingleValued()) {
                //System.out.println("3");
                FacesMessage msg = new FacesMessage(bundle.getString("connectionError"), bundle.getString("connectionError"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                //System.out.println("4");
                FacesMessage msg = new FacesMessage(bundle.getString("connectionError"), bundle.getString("connectionError"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

    }

    public Try<List> fetchProjectList() {       
        return Try.of(() -> applicationManager.getGitlabAPI().getOwnedProjects());
    }

    public String refresh() {
        Try<List> projectList = this.fetchProjectList();
        if (projectList.isSuccess()) {
            ownedProjectList = projectList.get();
            cacheHandler.invalidateProjectListCache();
            cacheHandler.putGitlabProjectListCache(gitlabUserContext.getUser().getUsername(), ownedProjectList);
        } else if (projectList.isFailure()) {
            //System.out.println("1");
            FacesMessage msg = new FacesMessage(bundle.getString("connectionError"), bundle.getString("connectionError"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (projectList.isEmpty()) {
            //System.out.println("2");
            FacesMessage msg = new FacesMessage(bundle.getString("connectionError"), bundle.getString("connectionError"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (projectList.isSingleValued()) {
            //System.out.println("3");
            FacesMessage msg = new FacesMessage(bundle.getString("connectionError"), bundle.getString("connectionError"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            //System.out.println("4");
            FacesMessage msg = new FacesMessage(bundle.getString("connectionError"), bundle.getString("connectionError"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return "projects";
    }

}
