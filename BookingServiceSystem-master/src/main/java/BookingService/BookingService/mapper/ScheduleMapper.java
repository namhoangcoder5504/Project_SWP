package BookingService.BookingService.mapper;

import BookingService.BookingService.dto.request.ScheduleRequest;
import BookingService.BookingService.dto.response.ScheduleResponse;
import BookingService.BookingService.entity.Schedule;
import BookingService.BookingService.entity.User;

public class ScheduleMapper {

    // Chuyển từ DTO (yêu cầu) -> Entity
    public static Schedule toEntity(ScheduleRequest request, User specialist) {
        return Schedule.builder()
                .date(request.getDate())
                .timeSlot(request.getTimeSlot())
                .availability(request.getAvailability())
                .specialist(specialist)
                .build();
    }

    // Chuyển từ Entity -> DTO (phản hồi)
    public static ScheduleResponse toResponse(Schedule schedule) {
        ScheduleResponse response = new ScheduleResponse();
        response.setScheduleId(schedule.getScheduleId());
        response.setDate(schedule.getDate());
        response.setTimeSlot(schedule.getTimeSlot());
        response.setAvailability(schedule.getAvailability());
        if (schedule.getSpecialist() != null) {
            response.setSpecialistId(schedule.getSpecialist().getUserId());
            // hoặc getUserId() tuỳ cách đặt tên trong entity User
        }
        return response;
    }
}
