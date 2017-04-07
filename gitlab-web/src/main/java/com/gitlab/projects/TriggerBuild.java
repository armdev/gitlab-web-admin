package com.gitlab.projects;

import com.gitlab.projects.utils.ParamUtil;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import javaslang.control.Try;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.net.ssl.HttpsURLConnection;
import lombok.Getter;
import lombok.Setter;
import org.gitlab.api.models.GitlabProject;

/**
 *
 * @author armdev
 */
@ManagedBean(name = "triggerBuild")
@ViewScoped
public class TriggerBuild implements Serializable {

    private static final long serialVersionUID = -5600244836602585880L;
    private static final String GITLAB_URL = "https://gitlab.com";

    private final String USER_AGENT = "Mozilla/5.0";

    @ManagedProperty("#{gitlabUserContext}")
    @Setter
    private GitlabUserContext gitlabUserContext = null;

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    @ManagedProperty("#{applicationManager}")
    @Setter
    private ApplicationManager applicationManager;
    @Setter
    @Getter
    private Integer projectId;
    @Setter
    @Getter
    private String token;

    public TriggerBuild() {
        
    }

    @PostConstruct
    public void init(){
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        projectId = ParamUtil.integerValue((this.getRequestParameter("projectId")));
        //System.out.println("projectId received " +projectId);
        token = ((this.getRequestParameter("token")));
    }
    
    public String doAction(Integer projectId) {
        try {
            applicationManager.init();            
           //  System.out.println("projectId passed " +projectId);
            Try<GitlabProject> fetchGitlabProject = this.fetchProject(projectId);            
           //  System.out.println("fetchGitlabProject " +fetchGitlabProject.toString());
            if (fetchGitlabProject.isSuccess()) {                
                this.sendPost(gitlabUserContext.getUser().getUrl(), projectId.toString(), token, fetchGitlabProject.get().getDefaultBranch());
            }
            if (fetchGitlabProject.isFailure()) {
                System.out.println("Failure ");
            }
        } catch (Exception ex) {
        }
        return "projects";
    }

    private void sendPost(String host, String projectId, String token, String ref) throws Exception {
//        System.out.println(projectId);
//        System.out.println(token);
//        System.out.println(ref);
        if (token == null && projectId == null) {
            return;
        }

        String url = host + "/api/v3/projects/" + projectId + "/trigger/builds";

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "token=" + token + "&ref=" + ref + "&variables[RUN_NIGHTLY_BUILD]=true";

        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        int responseCode = con.getResponseCode();
     //   System.out.println("\nSending 'POST' request to URL : " + url);
      //  System.out.println("Post parameters : " + urlParameters);
      //  System.out.println("Response Code : " + responseCode);

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        //print result
       // System.out.println(response.toString());

    }

    public Try<GitlabProject> fetchProject(Integer projectId) {
        //System.out.println("project id " + projectId);
        return Try.of(() -> applicationManager.getGitlabAPI().getProject(projectId.toString()));
    }

    private String getRequestParameter(String paramName) {
        String returnValue = null;
        if (externalContext.getRequestParameterMap().containsKey(paramName)) {
            returnValue = (externalContext.getRequestParameterMap().get(paramName));
        }
        return returnValue;
    }

}
