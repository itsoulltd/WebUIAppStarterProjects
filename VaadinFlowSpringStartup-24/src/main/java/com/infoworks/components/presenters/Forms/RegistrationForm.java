package com.infoworks.components.presenters.Forms;

import com.infoworks.applayouts.RoutePath;
import com.infoworks.config.RequestURI;
import com.infoworks.domain.entities.Registration;
import com.infoworks.domain.entities.User;
import com.infoworks.domain.models.Gender;
import com.infoworks.domain.repositories.AuthRepository;
import com.infoworks.objects.Response;
import com.infoworks.utils.rest.client.PostTask;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

public class RegistrationForm extends FormLayout {
    private TextField firstName = new TextField("First name");
    private TextField lastName  = new TextField("Last name");
    private EmailField email    = new EmailField("Email");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm password");
    private TextField contact  = new TextField("Contact");
    private Checkbox allowTerms = new Checkbox();
    private Button submit = new Button("Register");
    private Button cancel = new Button("Cancel");

    private BeanValidationBinder<Registration> binder;

    public RegistrationForm() {
        /**/
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Setup:
        Anchor termsAnchor = new Anchor("/pages/terms.html"
                , "I accept the Terms & Conditions!", AnchorTarget.BLANK);
        HorizontalLayout termsLayout = new HorizontalLayout(allowTerms, termsAnchor);

        HorizontalLayout actionBar = new HorizontalLayout(cancel, submit);
        add(firstName, lastName, contact, email, password, confirmPassword
                , termsLayout, actionBar);

        binder = new BeanValidationBinder<>(Registration.class);

        // Bind fields to bean properties
        binder.bindInstanceFields(this);  // assuming matching names

        // Custom validator: password must match confirmation
        binder.forField(confirmPassword)
                .withValidator( pwdConfirm -> pwdConfirm.equals(password.getValue()),
                        "Passwords must match")
                .bind(reg -> reg.getConfirmPassword(), (reg, val) -> reg.setConfirmPassword(val));

        // Submit Action:
        submit.addClickListener(event -> {
            UI ui = event.getSource().getUI().orElse(null);
            if (binder.validate().isOk() && ui != null) {
                Registration registration = new Registration();
                binder.writeBeanIfValid(registration);
                System.out.println(registration.getEmail());
                // Call service to register user etc
                // Create New User with Registration:
                User user = createUserFrom(registration);
                //TODO: CHECK provided registration API:
                PostTask task = new PostTask(RequestURI.USER_BASE, RequestURI.USER_REGISTRATION_API);
                task.setBody(user, AuthRepository.parseToken(ui));
                // IF-Success:: UI.getCurrent().navigate(RoutePath.LOGIN_VIEW);
                Response response = task.execute(null);
                if (response.getStatus() == 200) {
                    Notification notification = Notification.show("User has been created! Please login!"
                            , 3000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    UI.getCurrent().navigate(RoutePath.LOGIN_VIEW);
                } else {
                    // ELSE:: Show error;
                    Notification notification = Notification.show(response.getError()
                            , 3000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                // Handle validation errors
                Notification notification = Notification.show("Validation Failed!"
                        , 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        // Cancel Action:
        cancel.addClickListener(event -> UI.getCurrent().navigate(RoutePath.LOGIN_VIEW));

        // Submit & AllowTerms:
        submit.setEnabled(false);
        //binder.addStatusChangeListener(evt -> submit.setEnabled(binder.isValid()));
        allowTerms.addValueChangeListener(event -> {
            boolean isValid = binder.isValid();
            submit.setEnabled(isValid && event.getValue());
        });

        // UI config:
        setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("300px", 2)
        );
        setColspan(actionBar, 2);
    }

    private User createUserFrom(Registration registration) {
        User user = new User("", Gender.NONE, 18);
        user.setName(registration.getFirstName() + " " + registration.getLastName());
        //user.setFname(registration.getFirstName());
        //user.setLname(registration.getLastName());
        //user.setContact(registration.getContact());
        //user.setEmail(registration.getEmail());
        //user.setTenantID(registration.getTenantID());
        //String account = registration.getAccountName();
        //user.setAccountName(account);
        //user.setAccountPrefix("CASH");
        //user.setAccountType("USER");
        return user;
    }
}
