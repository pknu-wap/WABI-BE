package com.wap.wabi.qr.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class QrQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public QrQueryDslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
