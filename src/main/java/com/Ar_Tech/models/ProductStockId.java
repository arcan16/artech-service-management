package com.Ar_Tech.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductStockId implements Serializable {

    private Long product;
    private Long warehouse;

    public ProductStockId() {}

    public ProductStockId(Long product, Long warehouse) {
        this.product = product;
        this.warehouse = warehouse;
    }

    // equals & hashCode (OBLIGATORIO)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductStockId)) return false;
        ProductStockId that = (ProductStockId) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(warehouse, that.warehouse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, warehouse);
    }
}
