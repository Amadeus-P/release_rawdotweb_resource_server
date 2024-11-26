package com.main.web.siwa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "website_image")
public class WebsiteImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "src")
    private String src;

    @Column(name = "is_default")
    private Boolean isDefault;

    // 상위 테이블(부모 테이블)
    @ManyToOne
    @JoinColumn(name = "website_id")
    @JsonBackReference
    private Website website;

}