package BookingService.BookingService.service;

import BookingService.BookingService.dto.request.WishlistRequest;
import BookingService.BookingService.dto.response.WishlistResponse;
import BookingService.BookingService.entity.ServiceEntity;
import BookingService.BookingService.entity.User;
import BookingService.BookingService.entity.Wishlist;
import BookingService.BookingService.exception.AppException;
import BookingService.BookingService.exception.ErrorCode;
import BookingService.BookingService.mapper.WishlistMapper;
import BookingService.BookingService.repository.WishlistRepository;
import BookingService.BookingService.repository.UserRepository;
import BookingService.BookingService.repository.ServiceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ServiceEntityRepository serviceRepository;
    private final WishlistMapper wishlistMapper;

    // Tạo mới wishlist (lấy user từ SecurityContext, kiểm tra duplicate)
    public WishlistResponse createWishlist(WishlistRequest wishlistRequest) {
        // Lấy thông tin authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String currentUserEmail = authentication.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy service từ DB
        ServiceEntity service = serviceRepository.findById(wishlistRequest.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));

        // Kiểm tra duplicate: nếu đã tồn tại wishlist cho user này và service này thì ném exception
        if (!wishlistRepository.findByUserAndService(user, service).isEmpty()) {
            throw new AppException(ErrorCode.WISHLIST_DUPLICATE);
        }

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .service(service)
                .build();

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.toResponse(savedWishlist);
    }

    // Lấy toàn bộ wishlist (cho admin/staff hoặc dùng chung)
    public List<WishlistResponse> getAllWishlist() {
        return wishlistRepository.findAll().stream()
                .map(wishlistMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Lấy wishlist theo id
    public WishlistResponse getWishlistById(Long id) {
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_NOT_FOUND));
        return wishlistMapper.toResponse(wishlist);
    }

    // Cập nhật wishlist theo id (chỉ cho phép thay đổi service; user không được thay đổi)
    public WishlistResponse updateWishlist(Long id, WishlistRequest wishlistRequest) {
        Wishlist existingWishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_NOT_FOUND));

        // Lấy thông tin authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String currentUserEmail = authentication.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra rằng wishlist này thuộc user hiện tại
        if (!existingWishlist.getUser().getUserId().equals(user.getUserId())) {
            throw new AppException(ErrorCode.WISHLIST_NOT_ALLOWED);
        }

        // Lấy service mới từ DB
        ServiceEntity service = serviceRepository.findById(wishlistRequest.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));

        // Kiểm tra duplicate: nếu tồn tại wishlist khác của user với service này thì ném exception
        if (!wishlistRepository.findByUserAndService(user, service).stream()
                .filter(w -> !w.getWishlistId().equals(id))
                .collect(Collectors.toList()).isEmpty()) {
            throw new AppException(ErrorCode.WISHLIST_DUPLICATE);
        }

        existingWishlist.setService(service);
        Wishlist updatedWishlist = wishlistRepository.save(existingWishlist);
        return wishlistMapper.toResponse(updatedWishlist);
    }

    // Xóa wishlist theo id (chỉ cho user sở hữu mới được xóa)
    public void deleteWishlist(Long id) {
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_NOT_FOUND));

        // Lấy thông tin user từ SecurityContext để kiểm tra quyền xóa
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String currentUserEmail = authentication.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!wishlist.getUser().getUserId().equals(user.getUserId())) {
            throw new AppException(ErrorCode.WISHLIST_NOT_ALLOWED);
        }

        wishlistRepository.delete(wishlist);
    }
}
