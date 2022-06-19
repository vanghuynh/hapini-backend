
package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.OauthClientDetails;


@Repository
public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails, Long> {

}
