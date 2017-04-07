/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gitlab.projects;

import javaslang.control.Try;
import org.gitlab.api.GitlabAPI;

/**
 *
 * @author Admin
 */
public class GitlapConnection {

    private  GitlabAPI api;

    public GitlapConnection() {

    }

    public GitlabAPI getApi(String url, String token) {
        api = GitlabAPI.connect(url, token);
      
        return api;
    }

    public Try<GitlabAPI> tryToConnect(String url, String token) {
        return Try.of(() -> GitlabAPI.connect(url, token));
    }

}
