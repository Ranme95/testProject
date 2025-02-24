package com.example.testProject.Entity;

import com.example.testProject.Common.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userId;

    @JoinColumn
    @OneToOne(cascade = CascadeType.ALL)
    private MemberImage memberImage;

    @Column
    private String userPassword;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String provider;

    private String providerId;

}
