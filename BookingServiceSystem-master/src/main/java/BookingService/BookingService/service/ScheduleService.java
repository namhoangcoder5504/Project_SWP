package BookingService.BookingService.service;

import BookingService.BookingService.entity.Schedule;
import BookingService.BookingService.entity.User;
import BookingService.BookingService.exception.AppException;
import BookingService.BookingService.exception.ErrorCode;
import BookingService.BookingService.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    // Tạm thời ta giả sử có 1 UserService hoặc UserRepository
    // Nếu không, bạn phải tự viết phương thức getSpecialistById() phù hợp.
    // Đây chỉ là ví dụ mô phỏng, bạn nên thay thế bằng code thật.
    public User getSpecialistById(Long specialistId) {
        // TODO: Lấy user từ userRepository.findById(specialistId)
        // hoặc ném ra exception nếu không tồn tại
        User user = new User();
        user.setUserId(specialistId);
        return user;
    }


    /**
     * Tạo mới Schedule
     * - Kiểm tra trùng time slot (date + timeSlot + specialistId)
     * - Nếu trùng -> ném exception
     */
    public Schedule createSchedule(Schedule schedule) {
        Long specialistId = schedule.getSpecialist().getUserId();
        LocalDate date = schedule.getDate();
        String timeSlot = schedule.getTimeSlot();

        // 1. Kiểm tra trùng time slot
        boolean isConflict = scheduleRepository.existsBySpecialistUserIdAndDateAndTimeSlot(specialistId, date, timeSlot);
        if (isConflict) {
            // 2. Nếu trùng, ném exception
            throw new AppException(ErrorCode.BOOKING_TIME_CONFLICT);
        }

        // 3. Không trùng, lưu vào DB
        return scheduleRepository.save(schedule);
    }

    /**
     * Cập nhật Schedule (cơ bản)
     * - Tuỳ nghiệp vụ, bạn có thể kiểm tra trùng time slot nếu date/timeSlot thay đổi
     */
    public Schedule updateSchedule(Schedule existingSchedule, Schedule newData) {
        // Nếu date, timeSlot hoặc specialist thay đổi, kiểm tra lại
        boolean isDateChanged = !existingSchedule.getDate().equals(newData.getDate());
        boolean isTimeSlotChanged = !existingSchedule.getTimeSlot().equals(newData.getTimeSlot());
        boolean isSpecialistChanged = !existingSchedule.getSpecialist().getUserId()
                .equals(newData.getSpecialist().getUserId());

        if (isDateChanged || isTimeSlotChanged || isSpecialistChanged) {
            // Kiểm tra trùng time slot, loại trừ bản ghi đang được update
            boolean isConflict = scheduleRepository.existsBySpecialistUserIdAndDateAndTimeSlotAndScheduleIdNot(
                    newData.getSpecialist().getUserId(),
                    newData.getDate(),
                    newData.getTimeSlot(),
                    existingSchedule.getScheduleId() // loại trừ schedule hiện tại
            );
            if (isConflict) {
                throw new AppException(ErrorCode.BOOKING_TIME_CONFLICT);
            }
        }

        // Gán các trường mới
        existingSchedule.setDate(newData.getDate());
        existingSchedule.setTimeSlot(newData.getTimeSlot());
        existingSchedule.setSpecialist(newData.getSpecialist());
        existingSchedule.setAvailability(newData.getAvailability());

        return scheduleRepository.save(existingSchedule);
    }


    /**
     * Lấy danh sách tất cả Schedule
     */
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    /**
     * Lấy một Schedule theo ID
     */
    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    /**
     * Xoá Schedule theo ID
     */
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
