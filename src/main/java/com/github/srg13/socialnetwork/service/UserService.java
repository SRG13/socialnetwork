package com.github.srg13.socialnetwork.service;

import com.github.srg13.socialnetwork.AuthorizedUser;
import com.github.srg13.socialnetwork.domain.Role;
import com.github.srg13.socialnetwork.domain.User;
import com.github.srg13.socialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
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
        user.setProfileImage("picture.jpg");
        user.setRoles(Collections.singleton(Role.USER));
        user.setEnabled(true);

        userRepo.save(prepareToSave(user));
    }

    public void update(User user) {
        userRepo.save(prepareToSave(user));
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
}
