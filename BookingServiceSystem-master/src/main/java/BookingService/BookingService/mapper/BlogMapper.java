package BookingService.BookingService.mapper;

import BookingService.BookingService.dto.response.BlogResponse;
import BookingService.BookingService.dto.response.UserResponse;
import BookingService.BookingService.entity.Blog;
import BookingService.BookingService.entity.User;

import java.time.LocalDateTime;

public class BlogMapper {
    public static BlogResponse toResponse(Blog blog) {
        if (blog == null) return null;
        BlogResponse response = new BlogResponse();
        response.setBlogId(blog.getBlogId());
        response.setTitle(blog.getTitle());
        response.setContent(blog.getContent());
        response.setCreatedAt(blog.getCreatedAt());
        response.setUpdatedAt(blog.getUpdatedAt());
        User author = blog.getAuthor();
        if (author != null) {
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(author.getUserId());
            userResponse.setEmail(author.getEmail());
            userResponse.setName(author.getName());
            userResponse.setPhone(author.getPhone());
            userResponse.setAddress(author.getAddress());
            userResponse.setRole(author.getRole());
            userResponse.setCreatedAt(author.getCreatedAt());
            userResponse.setUpdatedAt(author.getUpdatedAt());
            response.setAuthor(userResponse);
        }

        return response;
    }
}
