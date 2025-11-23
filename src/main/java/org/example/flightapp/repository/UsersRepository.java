package org.example.flightapp.repository;

import org.example.flightapp.model.entity.Users;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UsersRepository extends ReactiveMongoRepository<Users, Long> {

}
