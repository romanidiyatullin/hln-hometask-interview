package com.hln.challenge.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
@Data
@Embeddable
public class WoodKey implements Serializable {

    @Column(name="id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="type")
    private WoodType woodType;
}
