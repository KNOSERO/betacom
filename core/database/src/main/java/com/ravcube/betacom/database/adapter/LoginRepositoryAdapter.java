package com.ravcube.betacom.database.adapter;

import com.ravcube.betacom.database.repository.UserEntityRepository;
import com.ravcube.betacom.domain.port.LoginRepository;
import com.ravcube.betacom.domain.spec.LoginCredential;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepositoryAdapter implements LoginRepository {

    private final UserEntityRepository userRepository;

    public LoginRepositoryAdapter(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<LoginCredential> findCredentialsByLogin(String login) {
        return userRepository.findByLogin(login)
                .map(user -> new LoginCredential(user.getId(), user.getLogin(), user.getPasswordHash()));
    }
}
