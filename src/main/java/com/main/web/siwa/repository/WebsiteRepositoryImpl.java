package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Website;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WebsiteRepositoryImpl implements  WebsiteCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // 여기서 일어날 수 있는 에러
    // Could not locate TableGroup
    // > root나 query를 다른 sql문을 만드는 코드에서 같은 변수명으로 중복해서 사용 했을떄

    @Override
    public Page<Website> findAll(String title,Long categoryId, Integer page, Integer size) {

        // SQL 불러오는 객체 만들기
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // SQL문 구조 작성하기
        CriteriaQuery<Website> query = cb.createQuery(Website.class);

        // from website
        Root<Website> root = query.from(Website.class);

        List<Predicate> predicates = new ArrayList<>();

        // like = title
        if(title != null && !title.isEmpty()){
            predicates.add(cb.like(root.get("title"),"%"+title+"%"));
        }

        if(categoryId != null) {
            predicates.add(cb.equal(root.get("category").get("id"), categoryId));
        }

        // AND OR 연산
//        Predicate orPredicate = cb.or(predicates.toArray(new Predicate[0]));
//        Predicate andPredicate = cb.and(predicates.toArray(new Predicate[0]));

        // 정렬 where (Predicate) and (Predicate) and (Predicate) and...
        if(!predicates.isEmpty())
            query.where(predicates.toArray(new Predicate[0])/* , Predicate, Predicate */);

        query.orderBy(cb.desc(root.get("regDate")));

        // SQL 만들어줘
        List<Website> websites = entityManager
                .createQuery(query)
                .setFirstResult((page-1) * size) // offset
                .setMaxResults(size) // limit
                .getResultList();

        // SQL문 구조 작성하기
        CriteriaQuery<Long> countWebsiteQuery = cb.createQuery(Long.class);

        // count() from website
        Root<Website> countWebsiteRoot = countWebsiteQuery.from(Website.class);

        List<Predicate> countPredicates = new ArrayList<>();
        // where 다음에 들어갈 조건문
        if(title != null && !title.isEmpty())
            countPredicates.add(cb.like(countWebsiteRoot.get("title"),"%"+title+"%"));

        if(categoryId != null) {
            countPredicates.add(cb.equal(countWebsiteRoot.get("category").get("id"), categoryId));
        }
        countWebsiteQuery.select(cb.count(countWebsiteRoot));
        countWebsiteQuery.where(countPredicates.toArray(new Predicate[0])/* , Predicate, Predicate */);

        Long countWebsite =  entityManager
                .createQuery(countWebsiteQuery)
                .getSingleResult();

        System.out.println("================WebsiteRepositoryImpl===============");
        System.out.println("title: " + title);
        System.out.println("categoryId: " + categoryId);
        System.out.println("Predicates: " + predicates);

        System.out.println("페이지 검색 결과:" + new PageImpl<>(websites, PageRequest.of(page-1, size), countWebsite));
        return new PageImpl<>(websites, PageRequest.of(page-1, size), countWebsite);
    }
}
