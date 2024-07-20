package com.jdev.projects.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jdev.projects.exceptions.BadRequestException;
import com.jdev.projects.exceptions.ProjectNotFoundException;
import com.jdev.projects.model.Project;
import com.jdev.projects.respository.ProjectsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final String uploadDir = "uploads/";
    @Value("${app.base-url}")
    private String baseUrl;

    public List<Project> getAllProjects() {
        List<Project> projects = this.projectsRepository.findAll();
        return projects;
    }

    public Project getProjectById(int id) throws ProjectNotFoundException {
        Project project = this.projectsRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " not found"));

        return transformImageUrl(project);
    }

    public Project addProject(String name, String description, int budget, MultipartFile image)
            throws IOException, BadRequestException {

        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Name is required.");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new BadRequestException("Description is required.");
        }

        if (budget <= 0) {
            throw new BadRequestException("Budget must be a positive number.");
        }

        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Image is required.");
        }

        Project newProject = new Project();
        newProject.setName(name);
        newProject.setBudget(budget);
        newProject.setDescription(description);

        if (image != null && !image.isEmpty()) {
            try {
                String filePath = this.saveImage(image);
                newProject.setImageUrl(filePath.toString());
            } catch (IOException e) {
                throw new IOException("Error saving image.");
            }
        }
        return this.projectsRepository.save(transformImageUrl(newProject));

    }

    public String deleteProject(int id) {
        if (!this.projectsRepository.existsById(id)) {
            throw new ProjectNotFoundException("Project with id: " + id + " not found");
        }

        this.projectsRepository.deleteById(id);
        return "Project with id: " + id + " has been deleted";

    }

    public Project editProjectById(int id, String name, String description, int budget, MultipartFile image,
            String imageUrl)
            throws IOException {

        Project project = projectsRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " not found"));

        project.setName(name);
        project.setDescription(description);
        project.setBudget(budget);

        try {
            if (image != null && !image.isEmpty()) {
                String imagePath = this.saveImage(image);
                project.setImageUrl(imagePath);
            } else if (imageUrl != null && !imageUrl.isEmpty()) {
                project.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new IOException("Error saving image/imageUrl.");
        }

        return projectsRepository.save(project);

    }

    private String saveImage(MultipartFile image) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString().replace("\\", "/");
    }

    private Project transformImageUrl(Project project) {
        String imageUrl = project.getImageUrl();
        if (imageUrl != null && !imageUrl.startsWith("http")) {
            String fullImageUrl = baseUrl + "/" + project.getImageUrl();
            project.setImageUrl(fullImageUrl);
        }
        return project;
    }

}
