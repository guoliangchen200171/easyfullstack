package net.fernandosalas.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "announcement_history")
public class AnnouncementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announcement_id", nullable = false)
    private Long announcementId;

    @Column(name = "title", nullable = false,
            columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String title;

    @Column(name = "operation_type", nullable = false,
            columnDefinition = "VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String operationType;

    @Column(name = "operated_at", nullable = false)
    private LocalDateTime operatedAt;
}
