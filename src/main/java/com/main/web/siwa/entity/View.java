package com.main.web.siwa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "view")
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "website_id", nullable = false)
    private Website website;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "time", nullable = false, updatable = false)
    private Instant time = Instant.now();

    @Column(name = "user_ip", length = 45)
    private String userIp;
}
