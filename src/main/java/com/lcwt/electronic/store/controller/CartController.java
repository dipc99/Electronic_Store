package com.lcwt.electronic.store.controller;

import com.lcwt.electronic.store.dtos.AddItemToCartRequest;
import com.lcwt.electronic.store.dtos.CartDto;
import com.lcwt.electronic.store.exceptions.ApiResponseMessage;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.servicesI.CartService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * @author DipaliC
     * @apiNote Methods For Cart Controller
     * @param userId
     * @param request
     * @return
     */

    @PostMapping("/cart/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable Long userId, @RequestBody AddItemToCartRequest request){
        log.info("Request initiate for addItemToCart with userId :{}",userId);
        CartDto cartDto = cartService.addItemToCart(userId, request);
        log.info("Request completed for addItemToCart with userId :{}",userId);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/cart/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart( @PathVariable Long userId ,@PathVariable Long itemId){
        log.info("Request initiate for removeItemFromCart with userId :{}",userId);
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(AppConstants.API_RESPONSE1)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        log.info("Request completed for removeItemFromCart with userId :{}",userId);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @DeleteMapping("/cart/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart( @PathVariable Long userId ){
        log.info("Request initiate for clearCart with userId :{}",userId);
        cartService.clearCart(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(AppConstants.API_RESPONSE)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        log.info("Request completed for clearCart with userId :{}",userId);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long userId){
        log.info("Request initiate for getCart with userId :{}",userId);
        CartDto cartDto = cartService.getCartByUser(userId);
        log.info("Request completed for getCart with userId :{}",userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}
