package com.software.repository;

import com.software.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LikeRepository extends JpaRepository<Likes, Long>,
        JpaSpecificationExecutor<Likes> {
}
