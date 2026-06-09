package cine.proyect.paymentservice.controller;

import cine.proyect.paymentservice.dto.PaymentRequestDTO;
import cine.proyect.paymentservice.model.Payment;
import cine.proyect.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cine/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> crearPago(@Valid @RequestBody PaymentRequestDTO dto) {
        Payment resultado = paymentService.procesarPago(dto);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> obtenerPago(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Payment>> listarPagos() {
        return ResponseEntity.ok(paymentService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        paymentService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}