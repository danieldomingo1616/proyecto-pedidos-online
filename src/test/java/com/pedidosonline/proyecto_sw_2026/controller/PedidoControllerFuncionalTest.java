package com.pedidosonline.proyecto_sw_2026.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PedidoControllerFuncionalTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    // =========================================================
    // Pruebas Funcionales: POST /api/v1/pedido
    // =========================================================

    @Test
    public void CP1_CrearPedido_Exito() {
        // CE1 (Válida): Payload correcto
        String payloadValido = """
            {
                "idCliente": "CLI-001",
                "idComercio": "COM-001",
                "metodoPago": "TARJETA",
                "montoTotalSimulado": 25.50,
                "articulos": [{"idProducto": "PROD-1", "cantidad": 2}]
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(payloadValido)
        .when()
            .post("/api/v1/pedido")
        .then()
            .statusCode(200) // Tu controlador devuelve ResponseEntity.ok()
            .body("mensaje", containsString("esperando confirmación"));
    }

    @Test
    public void CP2_CrearPedido_FormatoInvalido() {
        // CE4 (Inválida): JSON malformado o vacío
        given()
            .contentType(ContentType.JSON)
            .body("{ JSON MALFORMADO }")
        .when()
            .post("/api/v1/pedido")
        .then()
            .statusCode(400); // Spring lanza Bad Request automáticamente
    }

    // =========================================================
    // Pruebas Funcionales: POST /api/v1/pedido/{id}/cancelar/confirmar
    // =========================================================

    @Test
    public void CP4_ConfirmarCancelacion_FaltaBody() {
        // CE2 (Inválida): Petición sin el ConfirmarCancelacionRequest en el Body
        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/v1/pedido/PED-1234/cancelar/confirmar")
        .then()
            .statusCode(400); // 400 Bad Request por faltar @RequestBody
    }

    @Test
    public void CP6_ConfirmarCancelacion_PedidoNoExiste() {
        // CE3 (Inválida): ID válido pero que no existe en BD
        String payloadCancelacion = """
            {
                "montoReembolsoAcordado": 12.50,
                "idComercio": "COM-001",
                "idCliente": "CLI-001"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(payloadCancelacion)
        .when()
            .post("/api/v1/pedido/PED-INEXISTENTE/cancelar/confirmar")
        .then()
            .statusCode(404); // Lanza tu ResponseStatusException(NOT_FOUND)
    }
}