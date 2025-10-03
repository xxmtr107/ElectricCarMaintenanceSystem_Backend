package com.group02.ev_maintenancesystem.entity;

import com.group02.ev_maintenancesystem.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "work_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkSchedule extends BaseEntity{

    // ========== SCHEDULE INFORMATION ==========

    @Column(name = "schedule_date", nullable = false)
    LocalDate scheduleDate = LocalDate.now(); // The date this schedule applies to

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    DayOfWeek dayOfWeek; // MONDAY, TUESDAY, ...

    @Column(name = "start_time", nullable = false)
    LocalTime startTime; // 08:00

    @Column(name = "end_time", nullable = false)
    LocalTime endTime; // 17:00

    @Column(name = "is_working", nullable = false)
    Boolean isWorking = true;

    // ========== RELATIONSHIPS ==========

    // User (STAFF or TECHNICIAN role)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee; // Staff or Technician

    // Admin who created this schedule
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_admin_id")
    private User createdByAdmin; // User with ADMIN role

}
