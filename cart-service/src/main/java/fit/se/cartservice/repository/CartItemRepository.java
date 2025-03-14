package fit.se.cartservice.repository;

import fit.se.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId); // Tìm các chi tiết giỏ hàng của giỏ hàng cụ thể
}
