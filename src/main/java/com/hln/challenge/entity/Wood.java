package com.hln.challenge.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="wood")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Wood {

    @EmbeddedId
    private WoodKey woodId;

    @Column(name="price")
    private BigDecimal price;

}