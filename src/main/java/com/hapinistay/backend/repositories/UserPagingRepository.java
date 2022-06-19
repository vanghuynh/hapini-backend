package com.hapinistay.backend.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.User;

@Repository
public interface UserPagingRepository extends PagingAndSortingRepository<User,Integer> {

}