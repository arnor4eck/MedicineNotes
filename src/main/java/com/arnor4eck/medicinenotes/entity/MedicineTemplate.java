package com.arnor4eck.medicinenotes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;
}
