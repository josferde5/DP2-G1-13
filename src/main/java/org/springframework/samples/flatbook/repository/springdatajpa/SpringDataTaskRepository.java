
package org.springframework.samples.flatbook.repository.springdatajpa;

import java.util.Set;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.flatbook.model.Task;
import org.springframework.samples.flatbook.repository.TaskRepository;

public interface SpringDataTaskRepository extends TaskRepository, Repository<Task, Integer> {

	@Override
	@Query("SELECT t FROM Task t WHERE t.creator.username = ?1 or t.asignee.username = ?1")
	Set<Task> findByParticipant(String username);

	@Override
	@Query("SELECT t FROM Task t LEFT JOIN FETCH t.asignee a LEFT JOIN FETCH t.creator c WHERE t.flat.id = ?1")
	Set<Task> findByFlatId(int id);
}
