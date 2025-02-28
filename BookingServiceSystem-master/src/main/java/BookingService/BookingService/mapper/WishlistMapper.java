package BookingService.BookingService.mapper;

import BookingService.BookingService.dto.response.WishlistResponse;
import BookingService.BookingService.entity.Wishlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    WishlistResponse toResponse(Wishlist wishlist);
}