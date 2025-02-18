package BookingService.BookingService.mapper;

import BookingService.BookingService.dto.request.ServiceEntityRequest;
import BookingService.BookingService.dto.response.ServiceEntityResponse;
import BookingService.BookingService.entity.ServiceEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceEntityMapper {

    public ServiceEntity toEntity(ServiceEntityRequest request) {
        if (request == null) return null;
        return ServiceEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .duration(request.getDuration())
                .build();
    }

    public ServiceEntityResponse toResponse(ServiceEntity entity) {
        if (entity == null) return null;
        ServiceEntityResponse response = new ServiceEntityResponse();
        response.setServiceId(entity.getServiceId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setDuration(entity.getDuration());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
