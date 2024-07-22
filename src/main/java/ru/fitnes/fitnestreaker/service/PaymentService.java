package ru.fitnes.fitnestreaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fitnes.fitnestreaker.dto.PaymentDto;
import ru.fitnes.fitnestreaker.entity.Payment;
import ru.fitnes.fitnestreaker.exception.ErrorType;
import ru.fitnes.fitnestreaker.exception.Exception;
import ru.fitnes.fitnestreaker.mapper.PaymentMapper;
import ru.fitnes.fitnestreaker.repository.PaymentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    public PaymentDto getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()-> new Exception(ErrorType.NOT_FOUND,"Payment with id: " + id + " not found."));
        return paymentMapper.toDto(payment);
    }

    public List<PaymentDto> getAll() {
        List<Payment> paymentList = paymentRepository.findAll();
        return paymentMapper.toListDto(paymentList);
    }

    public PaymentDto create(PaymentDto paymentDto) {
        Payment payment = paymentMapper.toEntity(paymentDto);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    public PaymentDto update(PaymentDto paymentDto, Long id) {
        Payment oldPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new Exception(ErrorType.NOT_FOUND,"Payment with id: " + id + " not found."));
        Payment newPayment = paymentMapper.toEntity(paymentDto);
        paymentMapper.merge(oldPayment, newPayment);
        Payment savedPayment = paymentRepository.save(oldPayment);
        return paymentMapper.toDto(savedPayment);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }
}
