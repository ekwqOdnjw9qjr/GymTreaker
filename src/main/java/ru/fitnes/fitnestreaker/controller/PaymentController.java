package ru.fitnes.fitnestreaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fitnes.fitnestreaker.baseresponse.BaseResponseService;
import ru.fitnes.fitnestreaker.baseresponse.ResponseWrapper;
import ru.fitnes.fitnestreaker.dto.MembershipDto;
import ru.fitnes.fitnestreaker.dto.PaymentDto;
import ru.fitnes.fitnestreaker.service.PaymentService;

import java.util.List;
@Tag(name = "Payments",description = "Operation on payments")
@Validated
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final BaseResponseService baseResponseService;

    @Operation(
            summary = "Getting a payment by ID",
            description = "Allows you to upload a payment by ID from the database"
    )
    @GetMapping("/payment/{id}")
    public ResponseWrapper<PaymentDto> getPaymentById(@PathVariable @Min(0) Long id) {
        return baseResponseService.wrapSuccessResponse(paymentService.getById(id));
    }

    @Operation(
            summary = "Getting all the payments",
            description = "Allows you to unload all payments from the database"
    )
    @GetMapping
    public ResponseWrapper<List<PaymentDto>> getAllPayment() {
        return baseResponseService.wrapSuccessResponse(paymentService.getAll());
    }

    @Operation(
            summary = "Create a payment",
            description = "Allows you to create a new payment record in the database"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<PaymentDto> createPayment(PaymentDto paymentDto) {
        return baseResponseService.wrapSuccessResponse(paymentService.create(paymentDto));
    }

    @Operation(
            summary = "Update payment information",
            description = "Allows you to update payment information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseWrapper<PaymentDto> updatePayment(@RequestBody @Valid PaymentDto paymentDto, @PathVariable Long id) {
        return baseResponseService.wrapSuccessResponse(paymentService.update(paymentDto,id));
    }

    @Operation(
            summary = "Delete a payment by ID",
            description = "Allows you to delete a payment by ID from the database"
    )
    @DeleteMapping("/delete/{id}")
    public void deletePayment(@PathVariable @Min(0) Long id) {
        paymentService.delete(id);
    }
}
