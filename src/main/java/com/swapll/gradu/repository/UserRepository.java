package com.swapll.gradu.repository;

import com.swapll.gradu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserNameIgnoreCaseOrEmailIgnoreCase(String username, String email);
    boolean existsByUserNameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByMyReferralCodeIgnoreCase(String referralCode);
    Optional<User> findByMyReferralCode(String myReferralCode);
    Optional<User> findByUserNameOrEmailIgnoreCase(String username, String email);

}
