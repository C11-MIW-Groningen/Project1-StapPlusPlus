package nl.miwgroningen.ch11.stap.repositories;

import nl.miwgroningen.ch11.stap.model.LearningGoal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Thijs Harleman
 * Created at 09:18 on 13 Jun 2023
 * Purpose:
 */
public interface LearningGoalRepository extends JpaRepository<LearningGoal, Long> {
}
