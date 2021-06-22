package com.motyldrogi.bot.util;

import java.util.Map;

public class ErrorJson {
    private String timeStamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public ErrorJson(int status, Map<String, Object> errorAttributes) {
      this.timeStamp = String.valueOf(errorAttributes.get("timestamp"));
      this.status = status;
      this.error = String.valueOf(errorAttributes.get("error"));
      this.message = String.valueOf(errorAttributes.get("message"));
      this.path = String.valueOf(errorAttributes.get("path"));
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
