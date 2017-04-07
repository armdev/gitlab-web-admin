package com.gitlab.projects;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import lombok.Getter;
import lombok.Setter;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabBuild;
import org.gitlab.api.models.GitlabBuildVariable;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabProjectHook;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabServiceEmailOnPush;
import org.gitlab.api.models.GitlabSystemHook;
import org.gitlab.api.models.GitlabTag;
import org.gitlab.api.models.GitlabTrigger;
import org.gitlab.api.models.GitlabUser;

@ApplicationScoped
@ManagedBean(eager = true)
@Setter
@Getter
public class CacheHandler implements Serializable {

    private static final long serialVersionUID = 8163039174281168443L;

    private CacheManager manager;
    @Setter
    @Getter
    private Cache gitlabProjectCache;
    @Setter
    @Getter
    private Cache gitlabBuildCache;
    @Setter
    @Getter
    private Cache gitlabBuildVariableCache;
    @Setter
    @Getter
    private Cache gitlabTriggersCache;
    @Setter
    @Getter
    private Cache gitlabCommitsCache;
    @Setter
    @Getter
    private Cache gitlabDeployKeysCache;
    @Setter
    @Getter
    private Cache gitlabProjectHooksCache;
    @Setter
    @Getter
    private Cache gitlabSystemHooksCache;
    @Setter
    @Getter
    private Cache projectListCache;

    @Setter
    @Getter
    private Cache emailOnPushCache;

    @Setter
    @Getter
    private Cache milestonesCache;
    @Setter
    @Getter
    private Cache branchesCache;

    @Setter
    @Getter
    private Cache userCache;

    @Setter
    @Getter
    private Cache tagsCache;

    @Setter
    @Getter
    private Cache issuesCache;

    public CacheHandler() {
    }

    @PreDestroy
    public void destroy() {

    }

    @PostConstruct
    public void init() {

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream fis = classLoader.getResourceAsStream("ehcache.xml");
            this.manager = CacheManager.create(fis);

            this.gitlabProjectCache = this.manager.getCache("gitlab.project.cache");

            this.gitlabBuildCache = this.manager.getCache("gitlab.build.cache");
            this.gitlabBuildVariableCache = this.manager.getCache("gitlab.build.variable.cache");
            this.gitlabTriggersCache = this.manager.getCache("gitlab.triggers.cache");
            this.gitlabCommitsCache = this.manager.getCache("gitlab.commits.cache");
            this.gitlabDeployKeysCache = this.manager.getCache("gitlab.deploykeys.cache");
            this.gitlabProjectHooksCache = this.manager.getCache("gitlab.projecthook.cache");
            this.gitlabSystemHooksCache = this.manager.getCache("gitlab.systemhook.cache");
            this.projectListCache = this.manager.getCache("gitlab.project.list");

            this.emailOnPushCache = this.manager.getCache("gitlab.email.push");
            this.milestonesCache = this.manager.getCache("gitlab.project.milestones");
            this.branchesCache = this.manager.getCache("gitlab.project.branches");

            this.userCache = this.manager.getCache("gitlab.project.user");
            this.tagsCache = this.manager.getCache("gitlab.project.tags");
            this.issuesCache = this.manager.getCache("gitlab.project.issues");

        } catch (ClassCastException | IllegalStateException | CacheException e) {
            manager.shutdown();
        }
    }

    public boolean putIssuesCache(Serializable key, List<GitlabIssue> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.issuesCache == null) {
            this.issuesCache = this.manager.getCache("gitlab.project.issues");
        }

        issuesCache.put(new Element(key, value));
        issuesCache.flush();
        return true;
    }

    public Object getIssuesCache(String key) {
        try {
            if (this.issuesCache == null) {
                this.issuesCache = this.manager.getCache("gitlab.project.issues");
            }
            Element elem = this.issuesCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {

                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putTagsCache(Serializable key, List<GitlabTag> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.tagsCache == null) {
            this.tagsCache = this.manager.getCache("gitlab.project.tags");
        }

        tagsCache.put(new Element(key, value));
        tagsCache.flush();
        return true;
    }

    public Object getTagsCache(String key) {
        try {
            if (this.tagsCache == null) {
                this.tagsCache = this.manager.getCache("gitlab.project.tags");
            }
            Element elem = this.tagsCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {

                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putUserCache(Serializable key, GitlabUser  value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.userCache == null) {
            this.userCache = this.manager.getCache("gitlab.project.user");
        }

        userCache.put(new Element(key, value));
        userCache.flush();
        return true;
    }

    public Object getUserCache(String key) {
        try {
            if (this.userCache == null) {
                this.userCache = this.manager.getCache("gitlab.project.user");
            }
            Element elem = this.userCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {

                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putBranchesCache(Serializable key, List<GitlabBranch> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.branchesCache == null) {
            this.branchesCache = this.manager.getCache("gitlab.project.branches");
        }

        branchesCache.put(new Element(key, value));
        branchesCache.flush();
        return true;
    }

    public Object getBranchesCache(String key) {
        try {
            if (this.branchesCache == null) {
                this.branchesCache = this.manager.getCache("gitlab.project.branches");
            }
            Element elem = this.branchesCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {

                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putMilestonesCache(Serializable key, List<GitlabMilestone> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.milestonesCache == null) {
            this.milestonesCache = this.manager.getCache("gitlab.project.milestones");
        }

        milestonesCache.put(new Element(key, value));
        milestonesCache.flush();
        return true;
    }

    public Object getMilestonesCache(String key) {
        try {
            if (this.milestonesCache == null) {
                this.milestonesCache = this.manager.getCache("gitlab.project.milestones");
            }
            Element elem = this.milestonesCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {

                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putEmailOnPushCache(Serializable key, GitlabServiceEmailOnPush value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.emailOnPushCache == null) {
            this.emailOnPushCache = this.manager.getCache("gitlab.email.push");
        }
        
        emailOnPushCache.put(new Element(key, value));
        emailOnPushCache.flush();
        return true;
    }

    public Object getEmailOnPushCache(String key) {
        try {
            if (this.emailOnPushCache == null) {
                this.emailOnPushCache = this.manager.getCache("gitlab.email.push");
            }
            Element elem = this.emailOnPushCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {

                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public String invalidateCache() {
        manager.clearAllStartingWith("gitlab");
        return "projects";
    }

    public String invalidateProjectListCache() {
        manager.clearAllStartingWith("gitlab.project.list");
        return "projects";
    }

    public boolean putGitlabProjectListCache(Serializable key, List<GitlabProject> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.projectListCache == null) {
            this.projectListCache = this.manager.getCache("gitlab.project.list");
        }
        // System.out.println("ownedProjectListput ");
        projectListCache.put(new Element(key, value));
        gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabProjectListCache(String key) {
        try {
            if (this.projectListCache == null) {
                this.projectListCache = this.manager.getCache("gitlab.project.list");
            }
            Element elem = this.projectListCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {
                //  System.out.println("ownedProjectListput get " + elem.getObjectValue());
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putGitlabProjectCache(String key, GitlabProject value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.gitlabProjectCache == null) {
            this.gitlabProjectCache = this.manager.getCache("gitlab.project.cache");
        }
        gitlabProjectCache.put(new Element(key, value));
        //  gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabProjectCache(String key) {
        try {
            if (this.gitlabProjectCache == null) {
                this.gitlabProjectCache = this.manager.getCache("gitlab.project.cache");

            }
            Element elem = this.gitlabProjectCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putGitlabBuildCache(String key, List<GitlabBuild> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.gitlabBuildCache == null) {
            this.gitlabBuildCache = this.manager.getCache("gitlab.build.cache");
        }
        gitlabBuildCache.put(new Element(key, value));
        gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabBuildCache(String key) {
        try {
            if (this.gitlabBuildCache == null) {
                this.gitlabBuildCache = this.manager.getCache("gitlab.build.cache");

            }
            Element elem = this.gitlabBuildCache.get(key);

            if ((elem != null) && (elem.getObjectValue() != null)) {
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putGitlabBuildVariableCache(String key, List<GitlabBuildVariable> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.gitlabBuildVariableCache == null) {
            this.gitlabBuildVariableCache = this.manager.getCache("gitlab.build.variable.cache");
        }
        gitlabBuildVariableCache.put(new Element(key, value));
        gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabBuildVariableCache(String key) {
        try {
            if (this.gitlabBuildVariableCache == null) {
                this.gitlabBuildVariableCache = this.manager.getCache("gitlab.build.variable.cache");
            }
            Element elem = this.gitlabBuildVariableCache.get(key);

            if ((elem != null) && (elem.getObjectValue() != null)) {
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putGitlabTriggerCache(String key, List<GitlabTrigger> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.gitlabTriggersCache == null) {
            this.gitlabTriggersCache = this.manager.getCache("gitlab.triggers.cache");
        }
        gitlabTriggersCache.put(new Element(key, value));
        gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabTriggerCache(String key) {
        try {
            if (this.gitlabTriggersCache == null) {
                this.gitlabTriggersCache = this.manager.getCache("gitlab.triggers.cache");
            }
            Element elem = this.gitlabTriggersCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putGitlabCommitCache(String key, List<GitlabCommit> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.gitlabCommitsCache == null) {
            this.gitlabCommitsCache = this.manager.getCache("gitlab.commits.cache");
        }
        gitlabCommitsCache.put(new Element(key, value));
        gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabCommitCache(String key) {
        try {
            if (this.gitlabCommitsCache == null) {
                this.gitlabCommitsCache = this.manager.getCache("gitlab.commits.cache");
            }
            Element elem = this.gitlabCommitsCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putGitlabDeployCache(String key, List<GitlabSSHKey> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.gitlabDeployKeysCache == null) {
            this.gitlabDeployKeysCache = this.manager.getCache("gitlab.deploykeys.cache");
        }
        gitlabDeployKeysCache.put(new Element(key, value));
        gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabDeployCache(String key) {
        try {
            if (this.gitlabDeployKeysCache == null) {
                this.gitlabDeployKeysCache = this.manager.getCache("gitlab.deploykeys.cache");
            }
            Element elem = this.gitlabDeployKeysCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putGitlabProjectHookCache(String key, List<GitlabProjectHook> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.gitlabProjectHooksCache == null) {
            this.gitlabProjectHooksCache = this.manager.getCache("gitlab.projecthook.cache");
        }
        gitlabProjectHooksCache.put(new Element(key, value));
        gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabProjectHookCache(String key) {
        try {
            if (this.gitlabProjectHooksCache == null) {
                this.gitlabProjectHooksCache = this.manager.getCache("gitlab.projecthook.cache");
            }
            Element elem = this.gitlabProjectHooksCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }

    public boolean putGitlabSystemHookCache(String key, List<GitlabSystemHook> value) {
        if (key == null || value == null) {
            return false;
        }
        if (this.gitlabSystemHooksCache == null) {
            this.gitlabSystemHooksCache = this.manager.getCache("gitlab.systemhook.cache");
        }
        gitlabSystemHooksCache.put(new Element(key, value));
        gitlabProjectCache.flush();
        return true;
    }

    public Object getGitlabSystemHookCache(String key) {
        try {
            if (this.gitlabSystemHooksCache == null) {
                this.gitlabSystemHooksCache = this.manager.getCache("gitlab.systemhook.cache");
            }
            Element elem = this.gitlabSystemHooksCache.get(key);
            if ((elem != null) && (elem.getObjectValue() != null)) {
                return elem.getObjectValue();
            }
        } catch (ClassCastException | IllegalStateException | CacheException e) {
        }
        return null;
    }
}
