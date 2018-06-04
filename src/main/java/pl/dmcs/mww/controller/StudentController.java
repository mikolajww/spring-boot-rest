package pl.dmcs.mww.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.mww.model.Grade;
import pl.dmcs.mww.model.Student;
import pl.dmcs.mww.model.request.AddGradeRequest;
import pl.dmcs.mww.model.request.AddStudentRequest;
import pl.dmcs.mww.repository.GradeRepository;
import pl.dmcs.mww.repository.StudentRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/students")
public class StudentController {

    private StudentRepository studentRepository;
    private GradeRepository gradeRepository;

    @Autowired public StudentController(StudentRepository studentRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public Student addStudent(@RequestBody AddStudentRequest addStudentRequest) {
        System.out.println("POST request");
        Student student = new Student();
        student.setName(addStudentRequest.getName());
        student.setSurname(addStudentRequest.getSurname());
        student.setIndexNr(addStudentRequest.getIndexNr());
        return studentRepository.save(student);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Student updateStudent(@PathVariable("id") long id, @RequestBody Student s) {
        System.out.println("PUT request");
        Student student = studentRepository.findById(id);
        if (student == null) {
            Student n = new Student();
            n.setName(s.getName());
            n.setSurname(s.getSurname());
            n.setIndexNr(s.getIndexNr());
            n.setGrades(s.getGrades());
            return studentRepository.save(n);
        }
        student.setName(s.getName());
        student.setSurname(s.getSurname());
        student.setGrades(s.getGrades());
        return student;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Student> deleteStudent (@PathVariable("id") long id) {
        System.out.println("DELETE request");
        Student student = studentRepository.findById(id);
        if (student == null) {
            System.out.println("Student not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        studentRepository.deleteById(id);
        for(Grade g: student.getGrades()) {
            gradeRepository.delete(g);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Student getStudent(@PathVariable("id") long id){
        return studentRepository.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    @RequestMapping(value="/{id}/grades", method = RequestMethod.GET)
    public List<Grade> getGrades(@PathVariable("id") long id){
        return studentRepository.findById(id).getGrades();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @RequestMapping(value="/{id}/grades", method = RequestMethod.POST)
    public Grade addGrade(@PathVariable("id") long id, @RequestBody AddGradeRequest g) {
        Grade grade = new Grade();
        grade.setGrade(g.getGrade());
        grade.setSubject(g.getSubject());
        grade.setWeight(g.getWeight());
        gradeRepository.save(grade);
        Student s = studentRepository.findById(id);
        s.getGrades().add(grade);
        studentRepository.save(s);
        return grade;
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @RequestMapping(value="/{id}/grades/{gradeId}", method = RequestMethod.PUT)
    public ResponseEntity<Grade> updateGrade(@PathVariable("id") long id, @PathVariable("gradeId") long gradeId, @RequestBody AddGradeRequest g) {
        System.out.println("PUT grade request");
        Student student = studentRepository.findById(id);
        if (student == null) {
            System.out.println("Student not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (student.getGrades().size() == 0) {
            Grade grade = new Grade();
            grade.setGrade(g.getGrade());
            grade.setSubject(g.getSubject());
            grade.setWeight(g.getWeight());
            gradeRepository.save(grade);
            Student s = studentRepository.findById(id);
            s.getGrades().add(grade);
            studentRepository.save(s);
            return new ResponseEntity<>(grade, HttpStatus.CREATED);
        }
        for (Grade grade : student.getGrades()) {
            if (grade.getId() == gradeId) {
                grade.setGrade(g.getGrade());
                grade.setWeight(g.getWeight());
                grade.setSubject(g.getSubject());
                gradeRepository.save(grade);
                studentRepository.save(student);
                return new ResponseEntity<>(grade, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @RequestMapping(value="/{id}/grades/{gradeId}", method = RequestMethod.DELETE)
    public ResponseEntity<Grade> deleteGrade (@PathVariable("id") long id, @PathVariable("gradeId") long gradeId) {
        System.out.println("DELETE grade request");
        Student student = studentRepository.findById(id);
        if (student == null) {
            System.out.println("Student not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Grade grade : student.getGrades()) {
            if (grade.getId() == gradeId) {
                student.getGrades().remove(grade);
                gradeRepository.delete(grade);
                studentRepository.save(student);
                return new ResponseEntity<>(grade, HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
