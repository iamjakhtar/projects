package com.jdev.projects.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProjectDto {
    private int id;
    private String name;
    private String description;
    private int budget;
    private String imageUrl;
}
