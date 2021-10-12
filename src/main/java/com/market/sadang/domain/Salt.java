package com.market.sadang.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

@Builder

@Entity
public class Salt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String salt;

    public Salt(String salt) {
        this.salt = salt;
    }
}
