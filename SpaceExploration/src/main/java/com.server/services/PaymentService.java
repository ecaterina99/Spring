package com.server.services;

import com.server.dto.PaymentDTO;
import com.server.models.Payment;
import com.server.repositories.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = new ModelMapper();
    }

    public PaymentDTO getPaymentById(int id) {
        return paymentRepository.findById(id)
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Finance not found with id: " + id));
    }

    public List<PaymentDTO> getAllPayments() {

        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .toList();
    }
}