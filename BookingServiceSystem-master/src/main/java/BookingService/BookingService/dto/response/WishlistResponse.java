package BookingService.BookingService.dto.response;

import lombok.Data;

@Data
public class WishlistResponse {
    private Long wishlistId;
    private Long userId;
    private Long serviceId;
}
