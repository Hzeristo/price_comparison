package com.haydenshui.pricecomparison.user;

import com.haydenshui.pricecomparison.shared.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository interface provides methods to interact with the User database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the User object if found, or null if not found
     */
    User findByUsername(String username);

    /**
     * Checks if a user exists by their username.
     *
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a user exists by their phone number.
     *
     * @param phone the phone number to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByPhone(String phone);

}
