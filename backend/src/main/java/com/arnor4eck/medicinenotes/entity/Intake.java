package com.arnor4eck.medicinenotes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="intakes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Intake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id", nullable = false)
    private MedicineTemplate template;

    private LocalDateTime adoptedIn;

    @Column(nullable = false)
    private LocalDate shouldAdoptedIn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntakesStatus status;
}
