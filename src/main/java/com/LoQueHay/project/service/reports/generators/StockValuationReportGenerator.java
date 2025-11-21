package com.LoQueHay.project.service.reports.generators;

import com.LoQueHay.project.dto.report_dtos.ReportRequestDTO;
import com.LoQueHay.project.model.ProductStock;
import com.LoQueHay.project.repository.ProductStockRepository;
import com.LoQueHay.project.service.reports.pdf.PdfReportBuilder;
import com.LoQueHay.project.service.reports.specifications.ProductStockSpecs;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StockValuationReportGenerator implements ReportGenerator {

    private final ProductStockRepository productStockRepository;

    public StockValuationReportGenerator(ProductStockRepository productStockRepository) {
        this.productStockRepository = productStockRepository;
    }

    @Override
    public byte[] generate(ReportRequestDTO request) {

        // 1️⃣ Aplicar Specification dinámica para stock (sin fechas)
        List<ProductStock> stocks = productStockRepository.findAll(
                ProductStockSpecs.filterStocks(
                        request.getOwnerId(),
                        request.getWarehouseId(),
                        request.getCategoryId(),
                        false
                )
        );

        // 2️⃣ Convertir cada stock a Map para PDF dinámico
        List<Map<String, Object>> rows = stocks.stream()
                .map(stock -> {
                    Map<String, Object> row = new java.util.HashMap<>();
                    row.put("Producto", stock.getProduct().getName());
                    row.put("Categoría", stock.getProduct().getCategory().getName());
                    row.put("Almacén", stock.getWarehouse().getName());
                    row.put("Cantidad", stock.getQuantity());
                    row.put("Lote", stock.getLotNumber());
                    row.put("Fecha de Expiración", stock.getExpirationDate());
                    return row;
                })
                .collect(Collectors.toList());

        // 3️⃣ Definir columnas
        List<String> columns = List.of(
                "Producto", "Categoría", "Almacén", "Cantidad", "Lote", "Fecha de Expiración"
        );

        // 4️⃣ Generar PDF usando PdfReportBuilder
        return PdfReportBuilder.buildReport("Reporte de Valoración de Stock", columns, rows);
    }
}
