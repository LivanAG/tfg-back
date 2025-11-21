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
public class PurchaseReportGenerator implements ReportGenerator {

    private final InventoryMovementRepository inventoryMovementRepository;

    public PurchaseReportGenerator(InventoryMovementRepository inventoryMovementRepository) {
        this.inventoryMovementRepository = inventoryMovementRepository;
    }

    @Override
    public byte[] generate(ReportRequestDTO request) {

        // 1️⃣ Crear Specification dinámica para compras
        Specification<InventoryMovement> spec = InventoryMovementSpecs.filterPurchases(
                request.getOwnerId(),
                request.getWarehouseId(),
                request.getCategoryId(),
                MovementType.IN, // Compras
                request.getDateFrom().atStartOfDay(),
                request.getDateTo().atTime(23, 59, 59)
        );

        // 2️⃣ Obtener movimientos según la spec
        List<InventoryMovement> purchases = inventoryMovementRepository.findAll(spec);

        // 3️⃣ Convertir cada detalle de movimiento a Map<String,Object> para PDF
        List<Map<String, Object>> rows = purchases.stream()
                .flatMap(movement -> movement.getDetails().stream()
                        .map(detail -> {
                            Map<String, Object> row = new java.util.HashMap<>();
                            row.put("Producto", detail.getProduct().getName());
                            row.put("Categoría", detail.getProduct().getCategory().getName());
                            row.put("Cantidad", detail.getQuantity());
                            row.put("Costo Unitario", detail.getUnitCost());
                            row.put("Valor Total", detail.getUnitCost() * detail.getQuantity());
                            row.put("Lote", detail.getLotNumber());
                            row.put("Fecha de Expiración", detail.getExpirationDate());
                            row.put("Referencia", movement.getReferenceDocument());
                            return row;
                        })
                )
                .collect(Collectors.toList());

        // 4️⃣ Definir columnas dinámicas
        List<String> columns = List.of(
                "Producto", "Categoría", "Cantidad", "Costo Unitario",
                "Valor Total", "Lote", "Fecha de Expiración", "Referencia"
        );

        // 5️⃣ Generar PDF usando PdfReportBuilder genérico
        return PdfReportBuilder.buildReport("Reporte de Compras", columns, rows);
    }
}
