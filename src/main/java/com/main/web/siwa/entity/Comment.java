package com.main.web.siwa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "reg_date", nullable = false, updatable = false)
    private Instant regDate;

    @PrePersist
    protected void onCreate() {
        this.regDate = Instant.now();
    }

    @ManyToOne(fetch = FetchType.LAZY) // 성능 최적화를 위해 LAZY 로딩
    @JoinColumn(name = "member_id", nullable = false)
    @JsonBackReference
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id", nullable = false)
    @JsonBackReference
    private Website website;
}
