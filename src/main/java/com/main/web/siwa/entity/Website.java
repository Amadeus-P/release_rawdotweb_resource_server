package com.main.web.siwa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "website")
public class Website {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "description", length = 1000)
    private String description;

//    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_date", nullable = false, updatable = false)
    private Instant regDate;

    @PrePersist
    protected void onCreate() {
        this.regDate = Instant.now();
    }

    @Column(name = "updated_date")
    private Instant updatedDate;

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Instant.now();
    }

    // 상위 테이블(부모 테이블)
    @OneToMany(mappedBy = "website")
    @JsonManagedReference
    private List<WebsiteImage> images;

    // 하위 테이블(자식 테이블)
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes;

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dislike> dislikes;

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmark;

    @OneToMany(mappedBy = "website", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}