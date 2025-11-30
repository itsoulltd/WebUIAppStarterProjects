package com.infoworks.domain.models;

public class ResetPassword extends Authorization {
    private String oldPassword;
    private String newPassword;

    public ResetPassword(String authorization, String oldPassword, String newPassword) {
        super(authorization);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
