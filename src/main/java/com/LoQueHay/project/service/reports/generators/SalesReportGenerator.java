package com.LoQueHay.project.service.reports.generators;

import com.LoQueHay.project.dto.report_dtos.ReportRequestDTO;
import com.LoQueHay.project.model.InventoryMovement;
import com.LoQueHay.project.model.MovementType;
import com.LoQueHay.project.repository.InventoryMovementRepository;
import com.LoQueHay.project.service.reports.pdf.PdfReportBuilder;
import com.LoQueHay.project.service.reports.specifications.InventoryMovementSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SalesReportGenerator implements ReportGenerator {

    private final InventoryMovementRepository inventoryMovementRepository;

    public SalesReportGenerator(InventoryMovementRepository inventoryMovementRepository) {
        this.inventoryMovementRepository = inventoryMovementRepository;
    }

    @Override
    public byte[] generate(ReportRequestDTO request) {

        // 1️⃣ Crear Specification dinámica
        Specification<InventoryMovement> spec = InventoryMovementSpecs.filterPurchases(
                request.getOwnerId(),
                request.getWarehouseId(),
                request.getCategoryId(),
                MovementType.OUT, // Ventas
                request.getDateFrom().atStartOfDay(),
                request.getDateTo().atTime(23, 59, 59)
        );

        // 2️⃣ Obtener movimientos según la spec
        List<InventoryMovement> sales = inventoryMovementRepository.findAll(spec);

        // 3️⃣ Convertir cada detalle de movimiento a Map<String,Object> para PDF
        List<Map<String, Object>> rows = sales.stream()
                .flatMap(movement -> movement.getDetails().stream()
                        .map(detail -> {
                            Map<String, Object> row = new java.util.HashMap<>();
                            row.put("Producto", detail.getProduct().getName());
                            row.put("Categoría", detail.getProduct().getCategory().getName());
                            row.put("Almacén", movement.getWarehouse().getName());
                            row.put("Cantidad", detail.getQuantity());
                            row.put("Precio Unitario Venta", detail.getSellPriceUnit());
                            row.put("Valor Total", detail.getSellPriceUnit() * detail.getQuantity());
                            row.put("Lote", detail.getLotNumber());
                            row.put("Fecha de Expiración", detail.getExpirationDate());
                            row.put("Referencia", movement.getReferenceDocument());
                            return row;
                        })
                )
                .collect(Collectors.toList());

        // 4️⃣ Definir columnas dinámicas
        List<String> columns = List.of(
                "Producto", "Categoría", "Almacén", "Cantidad",
                "Precio Unitario Venta", "Valor Total", "Lote",
                "Fecha de Expiración", "Referencia"
        );

        // 5️⃣ Generar PDF usando PdfReportBuilder genérico
        return PdfReportBuilder.buildReport("Reporte de Ventas", columns, rows);
    }
}
