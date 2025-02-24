package com.example.testProject.Entity;

import com.example.testProject.Common.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String partnerId;

    @Column
    private String partnerPassword;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

}
