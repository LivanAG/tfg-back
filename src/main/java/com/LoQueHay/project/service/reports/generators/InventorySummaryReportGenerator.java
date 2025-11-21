package com.LoQueHay.project.service.reports.generators;

import com.LoQueHay.project.dto.report_dtos.ReportRequestDTO;
import com.LoQueHay.project.model.Product;
import com.LoQueHay.project.repository.ProductRepository;
import com.LoQueHay.project.service.reports.pdf.PdfReportBuilder;
import com.LoQueHay.project.service.reports.specifications.ProductSpecs;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InventorySummaryReportGenerator implements ReportGenerator {

    private final ProductRepository productRepo;

    public InventorySummaryReportGenerator(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public byte[] generate(ReportRequestDTO request) {
        List<Product> products = productRepo.findAll(
                ProductSpecs.filterInventorySummary(
                        request.getOwnerId(),
                        request.getWarehouseId(),
                        request.getCategoryId()
                )
        );

        // Convertir cada stock a Map<String, Object> para PDF dinámico
        List<Map<String, Object>> rows = products.stream()
                .flatMap(p -> p.getStock().stream()
                        .filter(s -> request.getWarehouseId() == null ||
                                s.getWarehouse().getId().equals(request.getWarehouseId()))
                        .map(s -> {
                            Map<String, Object> row = new java.util.HashMap<>();
                            row.put("Producto", p.getName());
                            row.put("Categoría", p.getCategory().getName());
                            row.put("Almacén", s.getWarehouse().getName());
                            row.put("Cantidad", s.getQuantity());
                            row.put("Costo Unitario", s.getUnitCost());
                            row.put("Valor Total", s.getQuantity() * s.getUnitCost());
                            return row;
                        })
                ).collect(Collectors.toList());

        // Definir las columnas del reporte (orden importa)
        List<String> columns = List.of(
                "Producto", "Categoría", "Almacén", "Cantidad",
                "Costo Unitario", "Valor Total"
        );

        // Nombre del almacén para título (opcional)
        String warehouseName = request.getWarehouseId() == null ? "Todos" :
                products.stream()
                        .flatMap(p -> p.getStock().stream())
                        .filter(s -> s.getWarehouse().getId().equals(request.getWarehouseId()))
                        .map(s -> s.getWarehouse().getName())
                        .findFirst()
                        .orElse("Almacén");

        // Generar PDF dinámico
        return PdfReportBuilder.buildReport("Reporte de Inventario", columns, rows);
    }
}
