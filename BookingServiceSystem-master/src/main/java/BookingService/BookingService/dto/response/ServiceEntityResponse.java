package BookingService.BookingService.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ServiceEntityResponse {
    private Long serviceId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
