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
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabServiceEmailOnPush;
import org.gitlab.api.models.GitlabTag;
import org.gitlab.api.models.GitlabUser;

/**
 *
 * @author armdev
 */
@ManagedBean(name = "projectDetailsBean")
@ViewScoped
@AllArgsConstructor
public class ProjectDetailsBean implements Serializable {

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
    private List<GitlabIssue> gitlabIssue = new ArrayList<>();
    @Setter
    @Getter
    private List<GitlabTag> gitlabTag = new ArrayList<>();
    @Setter
    @Getter
    private GitlabUser gitlabUser = new GitlabUser();
    @Setter
    @Getter
    private List<GitlabBranch> gitlabBranch = new ArrayList<>();
    @Setter
    @Getter
    private List<GitlabMilestone> gitlabMilestone = new ArrayList<>();
    @Setter
    @Getter
    private GitlabServiceEmailOnPush gitlabServiceEmailOnPush = new GitlabServiceEmailOnPush();

    public ProjectDetailsBean() {
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

            gitlabIssue = (List<GitlabIssue>) cacheHandler.getIssuesCache(projectId.toString());
            if (gitlabIssue == null) {
                Try<List> fetch = this.fetchIssues();
                if (fetch.isSuccess()) {
                    gitlabIssue = fetch.get();
                    cacheHandler.putIssuesCache(projectId.toString(), gitlabIssue);
                }
            }

            gitlabTag = (List<GitlabTag>) cacheHandler.getTagsCache(projectId.toString());
            if (gitlabTag == null) {
                Try<List> fetch = this.fetchTags();
                if (fetch.isSuccess()) {
                    gitlabTag = fetch.get();
                    cacheHandler.putTagsCache(projectId.toString(), gitlabTag);
                }
            }

            gitlabUser = (GitlabUser) cacheHandler.getUserCache(projectId.toString());
            if (gitlabUser == null) {
                Try<GitlabUser> fetch = this.fetchUser();
                if (fetch.isSuccess()) {
                    gitlabUser = fetch.get();
                    cacheHandler.putUserCache(projectId.toString(), gitlabUser);
                }
            }

            gitlabBranch = (List<GitlabBranch>) cacheHandler.getBranchesCache(projectId.toString());
            if (gitlabBranch == null) {
                Try<List> fetch = this.fetchBranch();
                if (fetch.isSuccess()) {
                    gitlabBranch = fetch.get();
                    cacheHandler.putBranchesCache(projectId.toString(), gitlabBranch);
                }
            }

            gitlabMilestone = (List<GitlabMilestone>) cacheHandler.getMilestonesCache(projectId.toString());
            if (gitlabMilestone == null) {
                Try<List> fetch = this.fetchMilestones();
                if (fetch.isSuccess()) {
                    gitlabMilestone = fetch.get();
                    cacheHandler.putMilestonesCache(projectId.toString(), gitlabMilestone);
                }
            }

            gitlabServiceEmailOnPush = (GitlabServiceEmailOnPush) cacheHandler.getEmailOnPushCache(projectId.toString());
            if (gitlabServiceEmailOnPush == null) {
                Try<GitlabServiceEmailOnPush> fetch = this.fetchGitlabServiceEmailOnPush();
                if (fetch.isSuccess()) {
                    gitlabServiceEmailOnPush = fetch.get();
                    cacheHandler.putEmailOnPushCache(projectId.toString(), gitlabServiceEmailOnPush);
                }
            }

        } else {
            this.redirectTo();
        }
    }

    public Try<GitlabServiceEmailOnPush> fetchGitlabServiceEmailOnPush() {
        return Try.of(() -> applicationManager.getGitlabAPI().getEmailsOnPush(projectId));
    }

    public Try<List> fetchMilestones() {
        return Try.of(() -> applicationManager.getGitlabAPI().getMilestones(projectId));
    }

    public Try<List> fetchBranch() {
        return Try.of(() -> applicationManager.getGitlabAPI().getBranches(projectId));
    }

    public Try<GitlabUser> fetchUser() {
        return Try.of(() -> applicationManager.getGitlabAPI().getUser());
    }

    public Try<List> fetchTags() {
        return Try.of(() -> applicationManager.getGitlabAPI().getTags(projectId));
    }

    public Try<GitlabProject> fetchProject() {
        return Try.of(() -> applicationManager.getGitlabAPI().getProject(projectId));
    }

    public Try<List> fetchIssues() {
        return Try.of(() -> applicationManager.getGitlabAPI().getIssues(gitlabProject));
    }

    private String getRequestParameter(String paramName) {
        String returnValue = null;
        if (externalContext.getRequestParameterMap().containsKey(paramName)) {
            returnValue = (externalContext.getRequestParameterMap().get(paramName));
        }
        return returnValue;
    }

    public Try<Void> redirectTo() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "/projects.jsf?project_id is missing");
            return Try.success(null);
        } catch (IOException ex) {

        }
        return null;
    }
}
