package pl.dmcs.mww.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import pl.dmcs.mww.model.Grade;
import pl.dmcs.mww.model.Student;

@Repository
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public interface GradeRepository extends JpaRepository<Grade,Long> {
    Grade findById(long id);
}