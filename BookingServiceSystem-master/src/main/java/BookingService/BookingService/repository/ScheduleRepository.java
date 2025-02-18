package BookingService.BookingService.repository;

import BookingService.BookingService.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    boolean existsBySpecialistUserIdAndDateAndTimeSlot(Long userId, LocalDate date, String timeSlot);
    boolean existsBySpecialistUserIdAndDateAndTimeSlotAndScheduleIdNot(Long userId, LocalDate date, String timeSlot, Long scheduleId);
}
