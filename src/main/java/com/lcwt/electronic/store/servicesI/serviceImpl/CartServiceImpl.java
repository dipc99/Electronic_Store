package com.lcwt.electronic.store.servicesI.serviceImpl;

import com.lcwt.electronic.store.dtos.AddItemToCartRequest;
import com.lcwt.electronic.store.dtos.CartDto;
import com.lcwt.electronic.store.entities.Cart;
import com.lcwt.electronic.store.entities.CartItem;
import com.lcwt.electronic.store.entities.Product;
import com.lcwt.electronic.store.entities.User;
import com.lcwt.electronic.store.exceptions.BadApiRequest;
import com.lcwt.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.repositories.CartItemRepo;
import com.lcwt.electronic.store.repositories.CartRepo;
import com.lcwt.electronic.store.repositories.ProductRepository;
import com.lcwt.electronic.store.repositories.UserRepo;
import com.lcwt.electronic.store.servicesI.CartService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartItemRepo cartItemRepo;

    /**
     * @author DipaliC.
     * @implNote Methods for Cart Module
     * @param userId
     * @param request
     * @return
     */
    @Override
    public CartDto addItemToCart(Long userId, AddItemToCartRequest request) {
        log.info("Initiate request to Dao Call for addItemToCart with userId:{}",userId);
        int quantity = request.getQuantity();
        Long productId = request.getProductId();
        if (quantity<=0){
            throw new BadApiRequest(AppConstants.QUANTITY);
        }
        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_CART, AppConstants.PRODUCT_ID, productId));
        //fetch the user from db
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER, AppConstants.USER_ID, userId));
        Cart cart=null;
        try {
            cart=cartRepo.findByUser(user).get();
        }catch (NoSuchElementException e){
                cart=new Cart();
                cart.setCartId(UUID.randomUUID().toString());
               // cart.setCartId(Long.valueOf(UUID.randomUUID().toString()));
                cart.setCreatedAt(new Date());
        }
            //perform cart Exception
        //if cart items already present then update
        AtomicReference<Boolean> updated=new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        List<CartItem> updatedItems = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //item already in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
        cart.setItems(updatedItems);
        //create items
        if (!updated.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updatedCart = cartRepo.save(cart);
        log.info("Initiate completed to Dao Call for addItemToCart with userId:{}",userId);
        return mapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(Long userId, Long cartItem) {
        log.info("Initiate request to Dao Call for removeItemFromCart with userId:{}",userId);
        CartItem cartItem1 = cartItemRepo.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CART_ITEM, AppConstants.CART_ITEM, cartItem));
        cartItemRepo.delete(cartItem1);
        log.info("Initiate complete to Dao Call for removeItemFromCart with userId:{}",userId);
    }

    @Override
    public void clearCart(Long userId) {
        log.info("Initiate request to Dao Call for clearCart with userId:{}",userId);
        //fetch the user from db
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER, AppConstants.USER_ID, userId));
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_CART, AppConstants.USER_ID, userId));
        cart.getItems().clear();
        cartRepo.save(cart);
        log.info("Initiate completed to Dao Call for clearCart with userId:{}",userId);
    }

    @Override
    public CartDto getCartByUser(Long userId) {
        log.info("Initiate request to Dao Call for getCartByUser with userId:{}",userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER, AppConstants.USER_ID, userId));
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_CART, AppConstants.USER_ID, userId));
        log.info("Initiate completed to Dao Call for getCartByUser with userId:{}",userId);
        return mapper.map(cart,CartDto.class);
    }
}
