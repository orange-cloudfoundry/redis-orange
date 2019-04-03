package com.orange.redis.service.broker;

import org.springframework.credhub.core.CredHubOperations;
import org.springframework.credhub.support.CredentialDetails;
import org.springframework.credhub.support.SimpleCredentialName;
import org.springframework.credhub.support.password.PasswordCredential;
import org.springframework.stereotype.Component;

@Component
public
class CredHubService {
  private final CredHubOperations credHubOperations;
  private final SimpleCredentialName credentialName;

  public
  CredHubService(CredHubOperations credHubOperations, String deployment, String property) {
    this.credHubOperations = credHubOperations;

    credentialName = new SimpleCredentialName(deployment, property);
  }

  public
  String getPassword() {
    CredentialDetails<PasswordCredential> password =
            credHubOperations.credentials().getByName(credentialName, PasswordCredential.class);

    return password.getValue().getPassword();
  }
}
