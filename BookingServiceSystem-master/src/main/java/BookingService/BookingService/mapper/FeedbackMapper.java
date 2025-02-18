package BookingService.BookingService.mapper;

import BookingService.BookingService.dto.response.FeedbackResponse;
import BookingService.BookingService.entity.Feedback;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    // Chuyển đổi Feedback entity sang FeedbackResponse DTO
    public FeedbackResponse toResponse(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setFeedbackId(feedback.getFeedbackId());
        response.setBookingId(feedback.getBooking().getBookingId());
        response.setCustomerId(feedback.getCustomer().getUserId());
        response.setSpecialistId(feedback.getSpecialist().getUserId());
        response.setRating(feedback.getRating());
        response.setComment(feedback.getComment());
        response.setCreatedAt(feedback.getCreatedAt());
        return response;
    }
}
