package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ModelPackageItemRequest;
import com.group02.ev_maintenancesystem.dto.response.ModelPackageItemResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ModelPackageItemService {

    // Tạo mới cấu hình (Có validate Active và Duplicate)
    ModelPackageItemResponse createModelPackageItem(ModelPackageItemRequest request);

    // Cập nhật cấu hình
    ModelPackageItemResponse updateModelPackageItem(Long id, ModelPackageItemRequest request);

    // [MỚI] Cập nhật trạng thái hoạt động (Kích hoạt / Ngừng sử dụng)
    ModelPackageItemResponse updateStatus(Long id, Boolean isActive);

    // Xóa cấu hình (Cẩn thận khi dùng, nên ưu tiên updateStatus)
    void deleteModelPackageItem(Long id);

    // Lấy chi tiết 1 cấu hình
    ModelPackageItemResponse getModelPackageItemById(Long id);

    // Lấy tất cả (Thường dùng cho Admin)
    List<ModelPackageItemResponse> getAllModelPackageItems();

    // Lấy danh sách theo dòng xe (Dùng cho Admin/Staff xem cấu hình)
    List<ModelPackageItemResponse> getByVehicleModelId(Long vehicleModelId);

    // Lấy danh sách theo dòng xe và mốc Km cụ thể (Dùng để show chi tiết gói)
    List<ModelPackageItemResponse> getByVehicleModelAndMilestoneKm(Long vehicleModelId, Integer milestoneKm);

    // Tính tổng tiền dự kiến cho một mốc bảo dưỡng
    BigDecimal getMilestoneTotalPrice(Long vehicleModelId, Integer milestoneKm);

    // Sao chép cấu hình từ xe này sang xe khác (Tiện ích cho Admin)
    void cloneConfiguration(Long sourceModelId, Long targetModelId);
}