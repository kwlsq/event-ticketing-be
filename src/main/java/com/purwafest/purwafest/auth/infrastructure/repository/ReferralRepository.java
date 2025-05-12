package com.purwafest.purwafest.auth.infrastructure.repository;

import com.purwafest.purwafest.auth.domain.entities.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral,Integer>, JpaSpecificationExecutor<Referral> {

}
