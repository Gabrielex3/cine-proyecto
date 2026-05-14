package cine.proyect.paymentservice.service;

import cine.proyect.paymentservice.client.BookingClient;
import cine.proyect.paymentservice.dto.PaymentRequestDTO;
import cine.proyect.paymentservice.model.*;
import cine.proyect.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingClient bookingClient;
    @Transactional
    public Payment procesarPago(PaymentRequestDTO dto) {
        log.info("Validando reserva {} con booking-service", dto.getReservaId());
        bookingClient.buscarReservaPorId(dto.getReservaId());
        log.info("Iniciando proceso de pago para la reserva: {}", dto.getReservaId());
        Payment payment = new Payment();
        payment.setMonto(dto.getMonto());
        payment.setMoneda(dto.getMoneda());
        payment.setReservaId(dto.getReservaId());
        payment.setTimestamp(LocalDateTime.now());

        boolean esExitoso = new Random().nextDouble() < 0.7;
        payment.setEstado(esExitoso ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);
        Payment guardado = paymentRepository.save(payment);

        if (esExitoso) {
            log.info("Pago ID {} APROBADO exitosamente", guardado.getId());
        } else {
            log.warn("Pago ID {} RECHAZADO por la entidad bancaria simulada", guardado.getId());
        }
        return guardado;
    }

    public Payment buscarPorId(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El pago con ID " + id + " no existe."));
    }

    public List<Payment> listarTodos() {
        return paymentRepository.findAll();
    }

    public void eliminarPago(Long id) {
        log.info("Eliminando registro de pago ID: {}", id);
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("El pago con ID " + id + " no existe.");
        }
        paymentRepository.deleteById(id);
    }
}