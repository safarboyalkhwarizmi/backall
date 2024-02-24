package uz.backall.products; // Assuming ProductResponseDTO is in this package

public class ProductResponseDTO {
  private Long id;
  private String serialNumber;
  private String name;
  private Long brandId;

  // Constructor is package-private, accessible only within the same package
  public ProductResponseDTO(Long id, String serialNumber, String name, Long brandId) {
    this.id = id;
    this.serialNumber = serialNumber;
    this.name = name;
    this.brandId = brandId;
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getBrandId() {
    return brandId;
  }

  public void setBrandId(Long brandId) {
    this.brandId = brandId;
  }
}