package app.myanime.auth.service;

import app.myanime.auth.model.Verification;
import app.myanime.auth.repository.VerificationRepository;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
@Getter
public class VerificationService {

    @Inject
    VerificationRepository repository;

    @Inject
    Mailer mailer;

    @ConfigProperty(name = "verification.mail.template")
    String mailLinkTemplate;

    @ConfigProperty(name = "verification.mail.subject")
    String mailSubject;

    @ConfigProperty(name = "verification.mail.content")
    String mailContent;

    public Verification send(String user, String mailToSend) {
        Verification verification = create(user);
        final String link = mailLinkTemplate.replace("$code", verification.getId()).replace("$user", user);
        Mail mail = Mail.withText(mailToSend, mailSubject, mailContent.replace("$link", link));
        mailer.send(mail);
        return verification;
    }

    public Verification create(String user) {
        Verification verification = new Verification();
        verification.setId(UUID.randomUUID().toString().replace("-", ""));
        verification.setUser(user);
        verification.setTimestamp(System.currentTimeMillis());
        repository.persist(verification);
        return verification;
    }



}
