package com.example.movieticketingsystem.entity;

import com.example.movieticketingsystem.config.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Role extends Auditable<Role> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role",unique = true)
    private String name;

    @OneToMany(mappedBy = "role")
    private List<AppUser> appUsers;
    //    @NotNull
//    private Date createdAt;
    @NotNull
    private boolean isActive;

//    @LastModifiedDate
//    @Column(insertable = false)
//    private LocalDateTime modifiedDate;
//
//    @Column(
//            updatable = false,
//            nullable = false
//    )
//    @CreatedDate
//    private LocalDateTime createdDate;
//
//    @LastModifiedBy
//    @Column(insertable = false)
//    private String modifiedBy;

//    @CreatedBy
//    @Column(
//            updatable = false,
//            nullable = false
//    )
//    private String createdBy;

//    private Date modifiedAt;
}
