package com.Ar_Tech.models;

import com.Ar_Tech.models.PartsProducts;
import com.Ar_Tech.models.Returns;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "return_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    @NotNull
    private Returns returnRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @NotNull
    private PartsProducts item;

    @Column(name = "quantity", nullable = false)
    @Min(value = 1)
    private Integer quantity;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
}