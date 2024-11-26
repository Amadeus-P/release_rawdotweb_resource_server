package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Website;
import org.springframework.data.domain.Page;

public interface WebsiteCustomRepository {
    Page<Website>findAll(String title,Long categoryId, Integer page, Integer size);
}
