package com.lcwt.electronic.store.dtos;

import com.lcwt.electronic.store.entities.OrderItem;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private String orderId;
    private String orderStatus="Pending";
    private String paymentStatus= "NotPaid";

    private int orderAmount;
    @NotBlank(message = "Address is required.....")
    private String billingAddress;
    @NotBlank(message = "Phone is required....")
    private String billingPhone;
    @NotBlank(message = "NAme is required.....")
    private String billingName;

    private Date orderDate=new Date();

    private Date deliveryDate;
    private UserDto user;

    private List<OrderItemDto> item=new ArrayList<>();

}
