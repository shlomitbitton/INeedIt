package i.need.it.IneedIt.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailAuthenticator extends Authenticator{

    private String username;
    private String password;

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}


