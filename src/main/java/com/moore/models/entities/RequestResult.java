package com.moore.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Request")
public class RequestResult extends BaseEntity {

    @Column(name = "path")
    private String path;

    @Column(name = "input")
    private String input;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "result", columnDefinition = "MEDIUMTEXT")
    private String result;

    @Column(name = "errors")
    private String errors;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
