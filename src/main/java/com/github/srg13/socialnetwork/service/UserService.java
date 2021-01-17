package com.github.srg13.socialnetwork.service;

import com.github.srg13.socialnetwork.AuthorizedUser;
import com.github.srg13.socialnetwork.domain.Role;
import com.github.srg13.socialnetwork.domain.User;
import com.github.srg13.socialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.srg13.socialnetwork.util.UserUtil.prepareToSave;

@Service
public class UserService implements UserDetailsService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthorizedUser(userRepo.findByUsername(username));
    }

    public User get(int id) {
        return userRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User with id=" + id + " not found")
        );
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public void createOrUpdate(User user) {
        if (user.isNew()) {
            create(user);
        } else {
            update(user);
        }
    }

    public void create(User user) {
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(prepareToSave(user, passwordEncoder));

        mailSender.sendActivationMail(user.getUsername(), user.getEmail(), user.getActivationCode());
    }

    public void update(User user) {
        userRepo.save(prepareToSave(user, passwordEncoder));
    }

    public void saveProfileImage(int id, MultipartFile img) throws IOException {
        User user = get(id);

        if (img != null && !img.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + img.getOriginalFilename();

            img.transferTo(new File(uploadPath + "/" + resultFilename));

            user.setProfileImage(resultFilename);
            userRepo.save(user);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setEnabled(true);
        user.setActivationCode(null);

        userRepo.save(user);

        return true;
    }
}
