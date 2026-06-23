package cine.proyect.paymentservice.controller;

import cine.proyect.paymentservice.dto.PaymentRequestDTO;
import cine.proyect.paymentservice.model.Payment;
import cine.proyect.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/cine/payment")
@Tag(name = "PAYMENT", description = "API RELACIONADA A LA CREACION DE PAGOS")

@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @Operation(summary = "METODO POST CREATE PAYMENT", description = "Crea un nuevo pago.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Payment creado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. BookingId inválido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear el pago",
                    content = @Content
            )
    })
    public ResponseEntity<Payment> crearPago(@Valid @RequestBody PaymentRequestDTO dto) {
        Payment resultado = paymentService.procesarPago(dto);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND PAYMENT BY ID", description = "Busca un pago por id especifica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró el usuario.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<Payment> obtenerPago(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL PAYMENTS", description = "Lista todos los pagos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Payment.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Payment>> listarPagos() {
        return ResponseEntity.ok(paymentService.listarTodos());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "METODO DELETE PAYMENT", description = "Elimina un Pago existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operación exitosa. El pago fue eliminado correctamente. No se devuelve contenido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado. El ID solicitado no existe en el sistema.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo eliminar el usuario.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        paymentService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}