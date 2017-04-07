package com.gitlab.projects.client;

import com.gitlab.projects.GitlabUserContext;
import com.gitlab.projects.models.Pipeline;
import com.gitlab.projects.utils.ParamUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

@ManagedBean(eager = true, name = "newPipeLineBean")
@SessionScoped
public class NewPipeLineBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{gitlabUserContext}")
    @Setter
    private GitlabUserContext gitlabUserContext = null;

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;

    @Setter
    @Getter
    private Integer projectId;
    @Setter
    @Getter
    private String branch = "master";
    @Setter
    @Getter
    private Pipeline pipeline = new Pipeline();

    public NewPipeLineBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        projectId = ParamUtil.integerValue((this.getRequestParameter("projectId")));

    }

    public void createNewPipeline() {
        CloseableHttpClient CLIENT = HttpClients.createDefault();
        try {
            HttpPost request = new HttpPost(gitlabUserContext.getUser().getUrl() + "/api/v3/projects/" + projectId + "/pipeline?ref=" + branch);
            StringEntity params = new StringEntity("New pipeline", "UTF-8");
            // request.addHeader("content-type", "application/json;charset=UTF-8");
            request.addHeader("PRIVATE-TOKEN", gitlabUserContext.getUser().getPrivateToken());
            request.setEntity(params);
            HttpResponse response = (HttpResponse) CLIENT.execute(request);
            // System.out.println("Status code###### " + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            ObjectMapper mapper = new ObjectMapper();
            pipeline = mapper.readValue(EntityUtils.toString(entity), Pipeline.class);
        } catch (IOException | ParseException ex) {
            try {
                CLIENT.close();
            } catch (IOException ex1) {

            }
        } finally {
            try {
                CLIENT.close();
            } catch (IOException ex1) {

            }
        }
    }
    
     public void retry(int id) {
        CloseableHttpClient CLIENT = HttpClients.createDefault();
        try {
            HttpPost request = new HttpPost(gitlabUserContext.getUser().getUrl() + "/api/v3/projects/" + projectId + "/pipelines/" + id + "/retry");
            StringEntity params = new StringEntity("Retry pipeline", "UTF-8");          
            request.addHeader("PRIVATE-TOKEN", gitlabUserContext.getUser().getPrivateToken());
            request.setEntity(params);
            HttpResponse response = (HttpResponse) CLIENT.execute(request);            
            HttpEntity entity = response.getEntity();
            ObjectMapper mapper = new ObjectMapper();
            pipeline = mapper.readValue(EntityUtils.toString(entity), Pipeline.class);
        } catch (IOException | ParseException ex) {
            try {
                CLIENT.close();
            } catch (IOException ex1) {

            }
        } finally {
            try {
                CLIENT.close();
            } catch (IOException ex1) {

            }
        }
    }

    public void cancel(int id) {
        CloseableHttpClient CLIENT = HttpClients.createDefault();
        try {
            HttpPost request = new HttpPost(gitlabUserContext.getUser().getUrl() + "/api/v3/projects/" + projectId + "/pipelines/" + id + "/cancel");
            StringEntity params = new StringEntity("Cancel pipeline", "UTF-8");
            // request.addHeader("content-type", "application/json;charset=UTF-8");
            request.addHeader("PRIVATE-TOKEN", gitlabUserContext.getUser().getPrivateToken());
            request.setEntity(params);
            HttpResponse response = (HttpResponse) CLIENT.execute(request);
            // System.out.println("Status code###### " + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            ObjectMapper mapper = new ObjectMapper();
            pipeline = mapper.readValue(EntityUtils.toString(entity), Pipeline.class);
        } catch (IOException | ParseException ex) {
            try {
                CLIENT.close();
            } catch (IOException ex1) {

            }
        } finally {
            try {
                CLIENT.close();
            } catch (IOException ex1) {

            }
        }
    }

    public List<Pipeline> getPipelinesList() {
        CloseableHttpClient CLIENT = HttpClients.createDefault();

        List<Pipeline> list = new ArrayList<>();
        try {
            HttpGet request = new HttpGet(gitlabUserContext.getUser().getUrl() + "/api/v3/projects/" + projectId + "/pipelines");
            //request.addHeader("charset", "UTF-8");
            request.addHeader("PRIVATE-TOKEN", gitlabUserContext.getUser().getPrivateToken());
            HttpResponse response = CLIENT.execute(request);
            //  response.addHeader("content-type", "application/json;charset=UTF-8");
            HttpEntity entity = response.getEntity();
            // System.out.println("Status code " + response.getStatusLine().getStatusCode());
            ObjectMapper mapper = new ObjectMapper();
            list = mapper.readValue(EntityUtils.toString(entity), List.class);
        } catch (IOException | ParseException ex) {
            try {
                CLIENT.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        } finally {
            try {
                CLIENT.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        }
        // System.out.println("List size " + list.size());
        return list;
    }

    private String getRequestParameter(String paramName) {
        String returnValue = null;
        if (externalContext.getRequestParameterMap().containsKey(paramName)) {
            returnValue = (externalContext.getRequestParameterMap().get(paramName));
        }
        return returnValue;
    }
}
