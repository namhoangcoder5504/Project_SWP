package BookingService.BookingService.service;

import BookingService.BookingService.entity.Image;
import BookingService.BookingService.entity.ServiceEntity;
import BookingService.BookingService.enums.SkinType;
import BookingService.BookingService.exception.AppException;
import BookingService.BookingService.exception.ErrorCode;
import BookingService.BookingService.repository.ImageRepository;
import BookingService.BookingService.repository.ServiceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceEntityService {

    private final ServiceEntityRepository serviceRepository;
    private final ImageRepository imageRepository;

    public ServiceEntity getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));
    }

    public Image addImageToService(Long serviceId, String imageUrl) {
        ServiceEntity service = getServiceById(serviceId);
        Image image = Image.builder()
                .url(imageUrl)
                .createdAt(LocalDateTime.now())
                .service(service)
                .build();
        return imageRepository.save(image);
    }

    public ServiceEntity createService(ServiceEntity service) {
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(service);
    }

    public ServiceEntity updateService(Long id, ServiceEntity updated) {
        ServiceEntity existing = getServiceById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setDuration(updated.getDuration());
        existing.setRecommendedSkinTypes(updated.getRecommendedSkinTypes()); // Cập nhật trường mới
        existing.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(existing);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    // Thêm phương thức tìm dịch vụ theo loại da
    public List<ServiceEntity> getServicesBySkinType(SkinType skinType) {
        return serviceRepository.findAll().stream()
                .filter(service -> service.getRecommendedSkinTypes() != null && service.getRecommendedSkinTypes().contains(skinType))
                .collect(Collectors.toList());
    }
}