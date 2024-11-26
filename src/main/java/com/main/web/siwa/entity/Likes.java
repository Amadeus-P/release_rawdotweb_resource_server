package com.main.web.siwa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "website_id"}))
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id")
    private Website website;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
