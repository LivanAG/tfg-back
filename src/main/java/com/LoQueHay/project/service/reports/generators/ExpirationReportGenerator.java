package com.LoQueHay.project.service.reports.generators;

import com.LoQueHay.project.dto.report_dtos.ReportRequestDTO;
import com.LoQueHay.project.model.ProductStock;
import com.LoQueHay.project.repository.ProductStockRepository;
import com.LoQueHay.project.service.reports.pdf.PdfReportBuilder;
import com.LoQueHay.project.service.reports.specifications.ProductStockSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class ExpirationReportGenerator implements ReportGenerator {

    private final ProductStockRepository productStockRepository;

    public ExpirationReportGenerator(ProductStockRepository productStockRepository) {
        this.productStockRepository = productStockRepository;
    }

    @Override
    public byte[] generate(ReportRequestDTO request) {

        // 1️⃣ Aplicar Specification dinámica
        List<ProductStock> expiringStocks = productStockRepository.findAll(
                ProductStockSpecs.filterStocks(
                        request.getOwnerId(),
                        request.getWarehouseId(),
                        request.getCategoryId(),
                        true
                )
        );

        // 2️⃣ Convertir a Map para PDF
        List<Map<String, Object>> rows = expiringStocks.stream()
                .map(stock -> {
                    Map<String, Object> row = new java.util.HashMap<>();
                    row.put("Producto", stock.getProduct().getName());
                    row.put("Categoría", stock.getProduct().getCategory().getName());
                    row.put("Almacén", stock.getWarehouse().getName());
                    row.put("Cantidad", stock.getQuantity());
                    row.put("Costo Unitario", stock.getUnitCost());
                    row.put("Valor Total", stock.getUnitCost() * stock.getQuantity());
                    row.put("Fecha de Vencimiento", stock.getExpirationDate());
                    return row;
                })
                .collect(Collectors.toList());

        // 3️⃣ Columnas
        List<String> columns = List.of(
                "Producto", "Categoría", "Almacén", "Cantidad",
                "Costo Unitario", "Valor Total", "Fecha de Vencimiento"
        );

        // 4️⃣ Generar PDF
        return PdfReportBuilder.buildReport("Reporte de Productos Próximos a Vencer", columns, rows);
    }
}
