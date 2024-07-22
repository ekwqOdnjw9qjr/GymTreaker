package ru.fitnes.fitnestreaker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.fitnes.fitnestreaker.dto.PaymentDto;
import ru.fitnes.fitnestreaker.dto.TrainerDto;
import ru.fitnes.fitnestreaker.entity.Payment;
import ru.fitnes.fitnestreaker.entity.Trainer;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);

    Payment toEntity(PaymentDto paymentDto);

    List<PaymentDto> toListDto(List<Payment> paymentList);

    List<Payment> toListEntity(List<PaymentDto> paymentDtoList);

    void merge(@MappingTarget Payment target, Payment source);
}
