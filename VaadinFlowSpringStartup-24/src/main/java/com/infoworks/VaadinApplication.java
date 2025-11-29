package com.infoworks;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme("default")
public class VaadinApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(VaadinApplication.class, args);
    }

}
