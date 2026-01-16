package com.arnor4eck.medicinenotes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MedicineTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private LocalDate until;
    @Column(nullable = false)
    private long quantityPerDay;
}
