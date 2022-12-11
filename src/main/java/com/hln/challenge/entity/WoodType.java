package com.hln.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="wood_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WoodType {
    @Id
    @Column(name="type")
    private String type;

    @Override
    public String toString(){
        return this.type;
    }
}
