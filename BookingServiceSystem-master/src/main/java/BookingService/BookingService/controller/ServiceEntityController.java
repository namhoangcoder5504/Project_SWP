package BookingService.BookingService.controller;

import BookingService.BookingService.dto.request.ServiceEntityRequest;
import BookingService.BookingService.dto.response.ServiceEntityResponse;
import BookingService.BookingService.entity.ServiceEntity;
import BookingService.BookingService.mapper.ServiceEntityMapper;
import BookingService.BookingService.service.ServiceEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceEntityController {

    private final ServiceEntityService serviceEntityService;
    private final ServiceEntityMapper serviceEntityMapper;

    // Chỉ ADMIN mới có thể tạo dịch vụ
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceEntityResponse> createService(@Valid @RequestBody ServiceEntityRequest request) {
        ServiceEntity serviceEntity = serviceEntityMapper.toEntity(request);
        ServiceEntity created = serviceEntityService.createService(serviceEntity);
        ServiceEntityResponse response = serviceEntityMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint xem dịch vụ mở cho mọi người
    @GetMapping
    public ResponseEntity<List<ServiceEntityResponse>> getAllServices() {
        List<ServiceEntity> list = serviceEntityService.getAllServices();
        List<ServiceEntityResponse> responses = list.stream()
                .map(serviceEntityMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Endpoint xem dịch vụ theo ID mở cho mọi người
    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntityResponse> getServiceById(@PathVariable Long id) {
        ServiceEntity service = serviceEntityService.getServiceById(id);
        ServiceEntityResponse response = serviceEntityMapper.toResponse(service);
        return ResponseEntity.ok(response);
    }

    // Chỉ ADMIN mới được cập nhật dịch vụ
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntityResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceEntityRequest request
    ) {
        ServiceEntity serviceEntity = serviceEntityMapper.toEntity(request);
        ServiceEntity updated = serviceEntityService.updateService(id, serviceEntity);
        ServiceEntityResponse response = serviceEntityMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    // Chỉ ADMIN mới được xóa dịch vụ
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceEntityService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint thêm ảnh (có thể mở cho mọi người hoặc giới hạn lại nếu cần)
    @PostMapping("/{serviceId}/images")
    public ResponseEntity<?> addImageToService(
            @PathVariable Long serviceId,
            @RequestParam("url") String imageUrl
    ) {
        var savedImage = serviceEntityService.addImageToService(serviceId, imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);
    }
}
