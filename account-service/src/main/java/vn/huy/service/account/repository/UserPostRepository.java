package vn.huy.service.account.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import vn.huy.service.account.entity.UserPost;

import java.util.Optional;

public interface UserPostRepository extends Neo4jRepository<UserPost, String> {

    @Query("MATCH(p:UserPost) WHERE p.id = $id AND p.daXoa = false RETURN p")
    Optional<UserPost> findById(String id);
}
