package BookingService.BookingService.controller;

import BookingService.BookingService.dto.request.ScheduleRequest;
import BookingService.BookingService.dto.response.ScheduleResponse;
import BookingService.BookingService.entity.Schedule;
import BookingService.BookingService.entity.User;
import BookingService.BookingService.mapper.ScheduleMapper;
import BookingService.BookingService.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 1. Lấy toàn bộ Schedule
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        List<ScheduleResponse> responseList = scheduleService.getAllSchedules()
                .stream()
                .map(ScheduleMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    /**
     * 2. Lấy 1 Schedule theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(schedule -> {
                    ScheduleResponse responseDTO = ScheduleMapper.toResponse(schedule);
                    return ResponseEntity.ok(responseDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 3. Tạo mới Schedule
     *    - Gọi createSchedule trong service để kiểm tra trùng slot
     */
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest request) {
        // 1. Lấy specialist (User) từ service/repository
        User specialist = scheduleService.getSpecialistById(request.getSpecialistId());

        // 2. Map DTO -> Entity
        Schedule scheduleEntity = ScheduleMapper.toEntity(request, specialist);

        // 3. Tạo lịch (đã kèm logic check trùng slot)
        Schedule savedSchedule = scheduleService.createSchedule(scheduleEntity);

        // 4. Map Entity -> DTO
        ScheduleResponse responseDTO = ScheduleMapper.toResponse(savedSchedule);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 4. Cập nhật 1 Schedule
     *    - Nếu user thay đổi date/timeSlot/specialist, vẫn kiểm tra trùng slot
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequest request
    ) {
        // Lấy schedule cũ
        var existingScheduleOpt = scheduleService.getScheduleById(id);
        if (existingScheduleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Schedule existingSchedule = existingScheduleOpt.get();

        // Lấy specialist
        User specialist = scheduleService.getSpecialistById(request.getSpecialistId());

        // Tạo object mới chứa dữ liệu cập nhật
        Schedule newData = ScheduleMapper.toEntity(request, specialist);

        // Gọi service update (sẽ kiểm tra conflict và loại trừ bản ghi hiện tại)
        Schedule updatedSchedule = scheduleService.updateSchedule(existingSchedule, newData);

        // Trả về DTO
        ScheduleResponse responseDTO = ScheduleMapper.toResponse(updatedSchedule);
        return ResponseEntity.ok(responseDTO);
    }


    /**
     * 5. Xoá 1 Schedule
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        var existingScheduleOpt = scheduleService.getScheduleById(id);
        if (existingScheduleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
