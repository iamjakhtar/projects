package com.jdev.projects.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jdev.projects.model.Project;
import com.jdev.projects.respository.ProjectsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final String uploadDir = "uploads/";

    public List<Project> getAllProjects() {
        List<Project> projects = this.projectsRepository.findAll();
        return projects;
    }

    public Project getProjectById(int id) {
        try {
            Optional<Project> project = this.projectsRepository.findById(id);
            return project.get();
        } catch (Exception e) {
            return null;
        }
    }

    public Project addProject(String name, String description, int budget, MultipartFile image) throws IOException {

        Project newProject = new Project();
        newProject.setName(name);
        newProject.setBudget(budget);
        newProject.setDescription(description);
        
        if (image != null && !image.isEmpty()) {
            String filePath = this.saveImage(image);
            newProject.setImageUrl(filePath.toString());
        }
        return this.projectsRepository.save(newProject);

    }

    public String deleteProject(int id) {
        try {
            this.projectsRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "Project with id: " + id + " has been deleted";
    }

    public Project editProjectById(int id, String name, String description, int budget, MultipartFile image, String imageUrl)
            throws IOException {

        Optional<Project> optionalProject = projectsRepository.findById(id);
        if (!optionalProject.isPresent()) {
            throw new RuntimeException("Project not found");
        }

        Project project = optionalProject.get();
        project.setName(name);
        project.setDescription(description);
        project.setBudget(budget);

        if (image != null && !image.isEmpty()) {
            String imagePath = this.saveImage(image);
            project.setImageUrl(imagePath);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            project.setImageUrl(imageUrl);
        }

        return projectsRepository.save(project);

    }

    private String saveImage(MultipartFile image) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }

   
}

