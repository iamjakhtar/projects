package com.jdev.projects.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jdev.projects.model.Project;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Integer> {

}
