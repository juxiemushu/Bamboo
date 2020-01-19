package com.wei.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @NotBlank
    @Length(min = 6, message = "密码长度至少6位")
    private String username;

    @NotNull
    @Min(15L)
    private Long orderId;
}
