package com.gitlab.projects.handlers;

import com.gitlab.projects.GitlabUserContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class AuthenticationPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;
    private static HashMap<String, String> pagePermissionMapping = null;

    private void pagePermissionMapping() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (pagePermissionMapping == null) {
            pagePermissionMapping = new HashMap();
            try {
                ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "accessProp");
                if (bundle != null) {
                    Enumeration e = bundle.getKeys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = bundle.getString(key);                        
                        pagePermissionMapping.put(key, value);
                    }
                }
            } catch (Exception e) {                
            }
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public synchronized void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        ExternalContext ex = context.getExternalContext();
        try {
            if (event.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
                pagePermissionMapping();           
            }
            String viewId = "/index.xhtml";
            if (context.getViewRoot() != null && context.getViewRoot().getViewId() != null) {
                viewId = context.getViewRoot().getViewId();
            }

            String permissions = (pagePermissionMapping.get(viewId));
            GitlabUserContext sessionContext = context.getApplication().evaluateExpressionGet(context, "#{gitlabUserContext}", GitlabUserContext.class);

            if (permissions != null && permissions.contains("PUBLIC")) {
                return;
            }

            if (sessionContext.getUser().getId() == null && !viewId.contains("index.xhtml") || !permissions.contains("LOGGED")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(ex.getRequestContextPath() + "/index.jsf?illegalAccess");
            }

        } catch (IOException | ELException ex1) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(ex.getRequestContextPath() + "/index.jsf?error");
            } catch (IOException ex2) {
                Logger.getLogger(AuthenticationPhaseListener.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
    }

    public void calculateStateSize(FacesContext context) {
        //FacesContext context = FacesContext.getCurrentInstance();
        Object state = context.getApplication().getStateManager().saveView(context);

        if (state != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(state);
                }
                // context.getAttributes().put("stateSize",
                //      Integer.toString(baos.toByteArray().length));

              //  System.out.println("State size: " + Integer.toString(baos.toByteArray().length) + " bytes");
            } catch (IOException e) {
                //no op
                context.getAttributes().put("stateSize", "ERROR");
            }
        } else {
            context.getAttributes().put("stateSize", "0");
        }
    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
