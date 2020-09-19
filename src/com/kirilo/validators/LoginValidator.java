package com.kirilo.validators;

import com.kirilo.utils.ReservedWords;
import com.kirilo.utils.Text;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ResourceBundle;

@FacesValidator("com.kirilo.validators.LoginValidator")
public class LoginValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        final ResourceBundle bundle = Text.bundle;
        try {
            if (bundle == null) {
                throw new IllegalArgumentException(bundle.getString("not_found"));
            }
            if (o == null) {
                throw new IllegalArgumentException(bundle.getString("undefined"));
            }

            final String value = o.toString();

            if (!Character.isLetter(value.charAt(0))) {
                throw new IllegalArgumentException(bundle.getString("login.first_letter"));

            }
            if (value.length() < 5) {
                throw new IllegalArgumentException(bundle.getString("login.length.error"));
            }
            if (ReservedWords.list.contains(value)) {
                throw new IllegalArgumentException(bundle.getString("login.used"));
            }

        } catch (IllegalArgumentException e) {
            final FacesMessage message = new FacesMessage(e.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

    }
}
