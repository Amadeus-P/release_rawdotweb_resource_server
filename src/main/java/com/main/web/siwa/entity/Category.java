package com.main.web.siwa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "kor_name")
    private String korName;

    @Column(name = "eng_name")
    private String engName;

    @Column(name = "icon_name")
    private String iconName;

    //상위 테이블(부모 테이블)
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    private Category mainCategory;

    // 하위 테이블(자식 테이블)
    @OneToMany(mappedBy = "mainCategory")
    @JsonManagedReference
    private List<Category> subCategories;

    // 카테고리에 속한 웹사이트
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
//    @JsonManagedReference
    @JsonIgnore
    private List<Website> websites;
}