package BookingService.BookingService.mapper;

import BookingService.BookingService.dto.response.WishlistResponse;
import BookingService.BookingService.entity.Wishlist;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistResponse toResponse(Wishlist wishlist) {
        WishlistResponse response = new WishlistResponse();
        response.setWishlistId(wishlist.getWishlistId());
        response.setUserId(wishlist.getUser().getUserId());
        response.setServiceId(wishlist.getService().getServiceId());
        return response;
    }
}
