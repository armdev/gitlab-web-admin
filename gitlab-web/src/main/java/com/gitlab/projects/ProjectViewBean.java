package com.gitlab.projects;

import com.gitlab.projects.utils.ParamUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javaslang.control.Try;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.gitlab.api.models.GitlabBuild;
import org.gitlab.api.models.GitlabBuildVariable;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabProjectHook;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabSystemHook;
import org.gitlab.api.models.GitlabTrigger;

/**
 *
 * @author armdev
 */
@ManagedBean(name = "projectViewBean")
@ViewScoped
@AllArgsConstructor
public class ProjectViewBean implements Serializable {

    private static final long serialVersionUID = 8743676996552577874L;

    @ManagedProperty("#{applicationManager}")
    @Setter
    private ApplicationManager applicationManager;

    @ManagedProperty("#{cacheHandler}")
    @Setter
    private CacheHandler cacheHandler;
    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    @Setter
    @Getter
    private Integer projectId;
    @Setter
    @Getter
    private GitlabProject gitlabProject = new GitlabProject();
    @Setter
    @Getter
    private List<GitlabBuild> gitlabBuild = new ArrayList<>();
    @Setter
    @Getter
    private List<GitlabBuildVariable> gitlabBuildVariables = new ArrayList<>();
    @Setter
    @Getter
    private List<GitlabTrigger> triggersList = new ArrayList<>();
    @Setter
    @Getter
    private List<GitlabCommit> allCommits = new ArrayList<>();
    @Setter
    @Getter
    private List<GitlabSSHKey> deployKeys = new ArrayList<>();
    @Setter
    @Getter
    private List<GitlabProjectHook> projectHooks = new ArrayList<>();
    @Setter
    @Getter
    private List<GitlabSystemHook> systemHooks = new ArrayList<>();

    public ProjectViewBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        projectId = ParamUtil.integerValue((this.getRequestParameter("projectId")));
        if (projectId != null) {
            gitlabProject = (GitlabProject) cacheHandler.getGitlabProjectCache(projectId.toString());            
            if (gitlabProject == null) {
                applicationManager.init();
                
                Try<GitlabProject> fetchGitlabProject = this.fetchProject();
                
                if (fetchGitlabProject.isSuccess()) {
                    gitlabProject = fetchGitlabProject.get();                    
                    cacheHandler.putGitlabProjectCache(projectId.toString(), gitlabProject);
                }
                if (fetchGitlabProject.isFailure()) {
                    this.redirectTo();
                }
            }
            gitlabBuild = (List<GitlabBuild>) cacheHandler.getGitlabBuildCache(projectId.toString());            
            if (gitlabBuild == null) {
                Try<List> fetchGithubBuilds = this.fetchGithubBuilds();
                if (fetchGithubBuilds.isSuccess()) {
                    gitlabBuild = fetchGithubBuilds.get();
                    cacheHandler.putGitlabBuildCache(projectId.toString(), gitlabBuild);
                }
            }

            gitlabBuildVariables = (List<GitlabBuildVariable>) cacheHandler.getGitlabBuildVariableCache(projectId.toString());
            if (gitlabBuildVariables == null) {
                Try<List> gitlabBuildVariablesList = this.fetchGitlabBuildVariables();
                if (gitlabBuildVariablesList.isSuccess()) {
                    gitlabBuildVariables = gitlabBuildVariablesList.get();
                    cacheHandler.putGitlabBuildVariableCache(projectId.toString(), gitlabBuildVariables);
                }
            }

            triggersList = (List<GitlabTrigger>) cacheHandler.getGitlabTriggerCache(projectId.toString());
            if (triggersList == null) {
                Try<List> triggers = this.fetchBuildTriggers();
                if (triggers.isSuccess()) {
                    triggersList = triggers.get();
                    cacheHandler.putGitlabTriggerCache(projectId.toString(), triggersList);
                }
            }

            allCommits = (List<GitlabCommit>) cacheHandler.getGitlabCommitCache(projectId.toString());
            if (allCommits == null) {
                Try<List> allCommitList = this.fetchAllCommits();
                if (allCommitList.isSuccess()) {
                    allCommits = allCommitList.get();
                    cacheHandler.putGitlabCommitCache(projectId.toString(), allCommits);
                }
            }
            deployKeys = (List<GitlabSSHKey>) cacheHandler.getGitlabDeployCache(projectId.toString());
            if (deployKeys == null) {
                Try<List> deployKeyList = this.fetchDeployKeys();
                if (deployKeyList.isSuccess()) {
                    deployKeys = deployKeyList.get();
                    cacheHandler.putGitlabDeployCache(projectId.toString(), deployKeys);
                }
            }
            projectHooks = (List<GitlabProjectHook>) cacheHandler.getGitlabProjectHookCache(projectId.toString());
            if (projectHooks == null) {
                Try<List> projectHooksList = this.fetchProjectHooks();
                if (projectHooksList.isSuccess()) {
                    projectHooks = projectHooksList.get();
                    cacheHandler.putGitlabProjectHookCache(projectId.toString(), projectHooks);
                }
            }
            systemHooks = (List<GitlabSystemHook>) cacheHandler.getGitlabSystemHookCache(projectId.toString());
            if (systemHooks == null) {
                Try<List> fetchSystemHooks = this.fetchSystemHooks();
                if (fetchSystemHooks.isSuccess()) {
                    systemHooks = fetchSystemHooks.get();
                    cacheHandler.putGitlabSystemHookCache(projectId.toString(), systemHooks);
                }
            }
        } else {
            this.redirectTo();
        }
    }

    public Try<Void> redirectTo() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "/projects.jsf?project_id is missing");
            return Try.success(null);
        } catch (IOException ex) {

        }
        return null;
    }

    public Try<GitlabProject> fetchProject() {
        return Try.of(() -> applicationManager.getGitlabAPI().getProject(projectId));
    }

    public Try<List> fetchSystemHooks() {
        return Try.of(() -> applicationManager.getGitlabAPI().getSystemHooks());
    }

    public Try<List> fetchGithubBuilds() {
        return Try.of(() -> applicationManager.getGitlabAPI().getProjectBuilds(projectId));
    }

    public Try<List> fetchGitlabBuildVariables() {
        return Try.of(() -> applicationManager.getGitlabAPI().getBuildVariables(projectId));
    }

    public Try<List> fetchBuildTriggers() {
        return Try.of(() -> applicationManager.getGitlabAPI().getBuildTriggers(gitlabProject));
    }

    public Try<List> fetchAllCommits() {
        return Try.of(() -> applicationManager.getGitlabAPI().getAllCommits(projectId));
    }

    public Try<List> fetchDeployKeys() {
        return Try.of(() -> applicationManager.getGitlabAPI().getDeployKeys(projectId));
    }

    public Try<List> fetchProjectHooks() {
        return Try.of(() -> applicationManager.getGitlabAPI().getProjectHooks(projectId));
    }

    private String getRequestParameter(String paramName) {
        String returnValue = null;
        if (externalContext.getRequestParameterMap().containsKey(paramName)) {
            returnValue = (externalContext.getRequestParameterMap().get(paramName));
        }
        return returnValue;
    }
}
