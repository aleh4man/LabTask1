package org.aleh4min.labtask1.repositories;

import org.aleh4min.labtask1.entities.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<@NonNull User, @NonNull Long> {

}
