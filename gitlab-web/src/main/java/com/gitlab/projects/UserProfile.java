/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gitlab.projects;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author armenar
 */
@ManagedBean
@AllArgsConstructor
@NoArgsConstructor
@ViewScoped
public class UserProfile implements Serializable{
    
    private static final long serialVersionUID = 4785602985021121407L;    
    
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
    private SystenUser user = new SystenUser();
    
    @PostConstruct
    public void init(){
        user = mongoConnection.findById(gitlabUserContext.getUser().getId());
    }
    
    public String updateProfile(){
        boolean check = mongoConnection.updateProfile(gitlabUserContext.getUser().getId(), user);
        if(check){
             user = mongoConnection.findById(gitlabUserContext.getUser().getId());
             gitlabUserContext.setUser(user);
        }        
        return "profile";
    }
    
    
}
