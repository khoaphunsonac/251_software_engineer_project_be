package HCMUT.TutorSytem.controller;

import HCMUT.TutorSytem.dto.DepartmentDTO;
import HCMUT.TutorSytem.model.Department;
import HCMUT.TutorSytem.payload.response.BaseResponse;
import HCMUT.TutorSytem.repo.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        List<DepartmentDTO> departmentDTOs = departments.stream()
                .map(department -> {
                    DepartmentDTO dto = new DepartmentDTO();
                    dto.setId(department.getId());
                    dto.setName(department.getName());
                    return dto;
                })
                .collect(Collectors.toList());

        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setMessage("Departments retrieved successfully");
        response.setData(departmentDTOs);
        return ResponseEntity.ok(response);
    }
}

