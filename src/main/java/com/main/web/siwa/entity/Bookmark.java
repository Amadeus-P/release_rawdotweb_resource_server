package com.main.web.siwa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "website_id"}))
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "website_id", nullable = false)
    private Website website;

    @Column(name = "reg_date")
    private Instant regDate;

    @PrePersist
    protected void onCreate() {
        this.regDate = Instant.now();
    }
}
