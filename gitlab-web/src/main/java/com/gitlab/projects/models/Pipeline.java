/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gitlab.projects.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author armdev
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Pipeline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter
    @Getter
    private int id;
    @Setter
    @Getter
    private boolean tag;
    @Setter
    @Getter
    private String sha;
    @Setter
    @Getter
    private String status;
    @Setter
    @Getter
    private String yaml_errors;
    @Setter
    @Getter
    private String started_at;
    @Setter
    @Getter
    private String finished_at;
    @Setter
    @Getter
    private String committed_at;
    @Setter
    @Getter
    private String duration;
    @Setter
    @Getter
    private String coverage;

}
