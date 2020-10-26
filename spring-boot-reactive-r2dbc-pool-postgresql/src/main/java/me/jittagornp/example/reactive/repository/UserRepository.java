package me.jittagornp.example.reactive.repository;

import me.jittagornp.example.reactive.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {

}
