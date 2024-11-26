package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Website;
import com.main.web.siwa.entity.WebsiteImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebsiteImageRepository extends JpaRepository<WebsiteImage, Long> {
    List<WebsiteImage> findAllByWebsite(Website website);

}
