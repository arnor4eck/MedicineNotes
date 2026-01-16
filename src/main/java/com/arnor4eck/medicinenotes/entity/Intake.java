package com.arnor4eck.medicinenotes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="intakes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Intake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id", nullable = false)
    private MedicineTemplate template;

    private LocalDateTime adoptedIn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntakesStatus status;
}
