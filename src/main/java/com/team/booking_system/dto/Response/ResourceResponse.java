package com.team.booking_system.dto.Response;

import com.team.booking_system.entity.Resource;
import lombok.Data;

@Data
public class ResourceResponse {
    private Long id;
    private String name;
    private String type;
    private String description;
    private Integer capacity;
    private boolean active;

    public static ResourceResponse from(Resource r) {
        ResourceResponse dto = new ResourceResponse();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setType(r.getType());
        dto.setDescription(r.getDescription());
        dto.setCapacity(r.getCapacity());
        dto.setActive(r.isActive());
        return dto;
    }
}