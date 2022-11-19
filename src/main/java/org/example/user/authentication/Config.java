package org.example.user.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "F1")
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/F1",
        callerQuery = "select password from users where login = ?",
        groupsQuery = "select role from users_roles where user = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
public class
Config {
}
