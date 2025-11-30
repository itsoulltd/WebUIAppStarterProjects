package com.infoworks.domain.entities;

import com.infoworks.entity.PrimaryKey;
import com.infoworks.entity.TableName;
import jakarta.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "Trend")
@TableName(value = "Trend")
public class Trend extends Persistable<Integer, Long>{

    @PrimaryKey(name="id", auto=true)
    @Id
    @Column(length = 100)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer id = 0;

    @NotEmpty(message = "title must not empty or null!")
    @Size(min = 4, max = 20, message = "4 <= title <= 20")
    private String title;

    @Size(max = 56, message = "subtitle <= 56")
    private String subtitle;

    @Size(max = 256, message = "description <= 256")
    private String description;


    @Email(message = "Invalid email!")
    private String email = "";

    private String phone;
    private String pictureUrl;
    private boolean enabled = false;

    public Trend() {}

    public Trend(Integer id, String title, String subtitle, String description) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean status) {
        this.enabled = status;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
