package com.safeflight.backend.dto;

public class FareBreakdownDto {
	private Double baseFare;
    private Integer extraBaggage;
    private Double baggageCharge;
    private String baggageModel;
    private String discountLabel;
    private int discountPercentage;
    private Double discountAmount;
    private Double subtotal;
    private Double taxRate;
    private Double taxAmount;
    private Double totalFare;

    public FareBreakdownDto() {
    }

    // Getters and Setters
    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Integer getExtraBaggage() {
        return extraBaggage;
    }

    public void setExtraBaggage(Integer extraBaggage) {
        this.extraBaggage = extraBaggage;
    }

    public Double getBaggageCharge() {
        return baggageCharge;
    }

    public void setBaggageCharge(Double baggageCharge) {
        this.baggageCharge = baggageCharge;
    }

    public String getBaggageModel() {
        return baggageModel;
    }

    public void setBaggageModel(String baggageModel) {
        this.baggageModel = baggageModel;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    // Builder
    public static FareBreakdownDtoBuilder builder() {
        return new FareBreakdownDtoBuilder();
    }

    public static class FareBreakdownDtoBuilder {
        private final FareBreakdownDto dto = new FareBreakdownDto();

        public FareBreakdownDtoBuilder baseFare(Double v) {
            dto.baseFare = v;
            return this;
        }

        public FareBreakdownDtoBuilder extraBaggage(Integer v) {
            dto.extraBaggage = v;
            return this;
        }

        public FareBreakdownDtoBuilder baggageCharge(Double v) {
            dto.baggageCharge = v;
            return this;
        }

        public FareBreakdownDtoBuilder baggageModel(String v) {
            dto.baggageModel = v;
            return this;
        }

        public FareBreakdownDtoBuilder discountLabel(String v) {
            dto.discountLabel = v;
            return this;
        }

        public FareBreakdownDtoBuilder discountPercentage(int v) {
            dto.discountPercentage = v;
            return this;
        }

        public FareBreakdownDtoBuilder discountAmount(Double v) {
            dto.discountAmount = v;
            return this;
        }

        public FareBreakdownDtoBuilder subtotal(Double v) {
            dto.subtotal = v;
            return this;
        }

        public FareBreakdownDtoBuilder taxRate(Double v) {
            dto.taxRate = v;
            return this;
        }

        public FareBreakdownDtoBuilder taxAmount(Double v) {
            dto.taxAmount = v;
            return this;
        }

        public FareBreakdownDtoBuilder totalFare(Double v) {
            dto.totalFare = v;
            return this;
        }

        public FareBreakdownDto build() {
            return dto;
        }
    }
}
