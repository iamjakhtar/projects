package com.jdev.projects.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jdev.projects.Dtos.ProjectDto;
import com.jdev.projects.model.Project;
import com.jdev.projects.service.ProjectsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectsController {

    private final ProjectsService projectsService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = this.projectsService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("/projects/add-project")
    public ResponseEntity<Project> addNewProject(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam int budget,
            @RequestParam MultipartFile image) {
        try {
            Project project = this.projectsService.addProject(name, description, budget, image);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDto> getProjectWithId(@PathVariable("id") int id) {
        Project project = this.projectsService.getProjectById(id);

        if (project != null) {
            
            ProjectDto projectDto = new ProjectDto(
                project.getId(), 
                project.getName(), 
                project.getDescription(), 
                project.getBudget(), 
                project.getImageUrl());
      
            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<String> deleteProjectById(@PathVariable("id") int id) {
        String response = this.projectsService.deleteProject(id);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> editProject(
        @PathVariable("id") int id,
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam int budget,
        @RequestParam(value = "image" , required = false) MultipartFile image,
        @RequestParam(required = false) String imageUrl
    ) {
        
        try {
            Project updatedProjec = this.projectsService.editProjectById(id, name, description, budget, image, imageUrl);
            return new ResponseEntity<>(updatedProjec, HttpStatus.ACCEPTED);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
