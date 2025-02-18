package BookingService.BookingService.mapper;

import BookingService.BookingService.dto.response.ImageResponse;
import BookingService.BookingService.dto.request.ImageRequest;
import BookingService.BookingService.entity.Image;
import BookingService.BookingService.entity.ServiceEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ImageMapper {

    public ImageResponse toResponse(Image image) {
        return ImageResponse.builder()
                .imageId(image.getImageId())
                .url(image.getUrl())
                .createdAt(image.getCreatedAt())
                .serviceId(image.getService().getServiceId())
                .build();
    }

    public Image toEntity(ImageRequest request, ServiceEntity service) {
        return Image.builder()
                .url(request.getUrl())
                .createdAt(LocalDateTime.now())
                .service(service)
                .build();
    }
}
